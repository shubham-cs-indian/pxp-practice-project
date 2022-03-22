package com.cs.core.rdbms.services.resolvers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.dto.RuleResultDTO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.rdbms.rsearch.dto.ValueCountDTO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;
import com.cs.core.rdbms.services.records.TagsRecordDAS;
import com.cs.core.rdbms.services.records.ValueRecordDAS;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.google.common.collect.Sets;

/**
 * Renderer that resolves the properties that need to be shown in search result.
 * 
 * @author Roshani
 */
public class SearchRenderer extends RDBMSDataAccessService {
  
  public final ICSESearch             search;

  protected static final List<String> SORTING_CHARACTERISTICS = new ArrayList<>();
  protected static final List<String> CONTEXTUAL_SORTING = new ArrayList<>();

  static {
    SORTING_CHARACTERISTICS.add("baseentityid");
    SORTING_CHARACTERISTICS.add("baseentityname");
    SORTING_CHARACTERISTICS.add("baselocaleid");
    SORTING_CHARACTERISTICS.add("createduseriid");
    SORTING_CHARACTERISTICS.add("createtime");
    SORTING_CHARACTERISTICS.add("lastmodifieduseriid");
    SORTING_CHARACTERISTICS.add("lastmodifiedtime");

    CONTEXTUAL_SORTING.add(CommonConstants.FROM_PROPERTY);
    CONTEXTUAL_SORTING.add(CommonConstants.TO_PROPERTY);
  }

  /**
   * Create a new service interface for search resolution
   *
   * @param connection
   * @param search
   */
  public SearchRenderer(RDBMSConnection connection, ICSESearch search)
  {
    super(connection);
    this.search = search;
  }
  
  private void fillBaseEntity(Collection<PropertyDTO> properties, IResultSetParser resultSetParser,
      List<BaseEntityDTO> baseEntities, LocaleCatalogDAO catalogDAO)
      throws SQLException, RDBMSException, CSFormatException
  {
    BaseEntityDTO baseEntity = new BaseEntityDTO();
    long baseEntityIID = resultSetParser.getLong("baseEntityIID");
    baseEntity.mapFromBaseEntityTrackingWithName(resultSetParser);
    // Load value records
    Set<IPropertyRecordDTO> records = ValueRecordDAS.loadRecords( currentConnection,
        catalogDAO, baseEntityIID, properties.toArray(new PropertyDTO[0]));
    // Load tags records
    Set<IPropertyRecordDTO> tagRecords = TagsRecordDAS.loadRecords( currentConnection,
        catalogDAO, baseEntityIID, properties.toArray(new PropertyDTO[0]));
    records.addAll(tagRecords);
    
    baseEntity.setPropertyRecords(records.toArray(new IPropertyRecordDTO[0]));
    catalogDAO.loadLocaleIds(baseEntity);
    // add baseEntity into list
    baseEntities.add(baseEntity);
  }
  
  private static final String     Q_GET_PROPERTYRECORD    = "Select * from pxp.propertyConfig where propertyCode in (?)";
  private Collection<PropertyDTO> getPropertiesForEvaluation()
      throws RDBMSException, SQLException
  {
    ICSECalculationNode evaluation = search.getEvaluation();
    if (evaluation == null) {
      return new ArrayList<>();
    }
    Set<ICSERecordOperand> filterRecords = evaluation.getRecords();
    Set<String> propertyCodes = filterRecords.stream()
        .map(x -> x.getProperty().getCode())
        .collect(Collectors.toSet());
    
    String getPropertyQuery = Q_GET_PROPERTYRECORD.replace("?", Text.join(",", propertyCodes, "'%s'"));
    PreparedStatement statement = currentConnection.prepareStatement(getPropertyQuery);
    IResultSetParser resultSetParser = driver.getResultSetParser(statement.executeQuery());
    
    Collection<PropertyDTO> properties = new ArrayList<PropertyDTO>();
    while (resultSetParser.next()) {
      PropertyDTO property = new PropertyDTO(resultSetParser);
      properties.add(property);
    }
    return properties;
  }
  
  private static final String Q_GET_BASEENTITY          = "select b.*, b.baseEntityBaseName as baseEntityName  from"
      + " (select e.*, COALESCE (rv.value, 'extensionentity')  as baseentitybasename from pxp.BaseEntityTrackingFullContent"
      + " e left join pxp.valuerecord rv on rv.entityIID = e.baseentityIID and e.ismerged != true and %s and rv.propertyIID = 200) b ";
  private static final String Q_GET_CONDITION_FOR_ORDER = "where b.baseEntityIID in (%s) order by position(baseentityiid::text in '%s')";
  private static final String Q_GET_LocaleIDs_FOR_BaseEntity = "Select count(*) from pxp.baseentitylocaleidlink where baseentityiid = %s and localeid = '%s'";

  /**
   * 
   * @param baseEntityIIDs iids of base entities that need to be rendered
   * @return DTO's of base entity that need to be returned.
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws SQLException
   */
  public List<BaseEntityDTO> renderSearchEntities(Collection<Long> baseEntityIIDs) throws CSFormatException, RDBMSException, SQLException
  {
    Collection<PropertyDTO> properties = getPropertiesForEvaluation();
    List<BaseEntityDTO> baseEntities = new ArrayList<>();
    if(baseEntityIIDs.isEmpty()) {
      return baseEntities;
    } 
    
    //if language translation is present get content Name in that language, else get content Name in base locale.
    List<String> localeIDs = search.getScope().getLocaleIDs();
    for(Long baseEntityIID: baseEntityIIDs) {
      String languageCondition = null;
      PreparedStatement prepareStatement = currentConnection.prepareStatement(String.format(Q_GET_LocaleIDs_FOR_BaseEntity, baseEntityIID, localeIDs.get(0)));
      IResultSetParser resultSetParser = driver.getResultSetParser(prepareStatement.executeQuery());
      long count  = 0;
      while(resultSetParser.next()) {
        count = resultSetParser.getLong("count");
      }
      if(count > 0) {
        languageCondition = " rv.localeID = '"+ localeIDs.get(0)+"'";
      }
      else {
        languageCondition = " rv.localeID = e.baseLocaleID";
      }
      
      String query = String.format(Q_GET_BASEENTITY, languageCondition) + String.format(Q_GET_CONDITION_FOR_ORDER, baseEntityIID, baseEntityIID);
      prepareStatement = currentConnection.prepareStatement(query);
      resultSetParser = driver.getResultSetParser(prepareStatement.executeQuery());
      
      LocaleCatalogDTO localCatalogDTO = new LocaleCatalogDTO(new LocaleCatalogDTO(), localeIDs);
      LocaleCatalogDAO catalogDAO = new LocaleCatalogDAO(new UserSessionDTO(), localCatalogDTO);
      
      while (resultSetParser.next()) {
        fillBaseEntity(properties, resultSetParser, baseEntities, catalogDAO);
      }
    }
    return baseEntities;
  }
  
  /**
   * 
   * @param mainQuery query that contains selection as well as ordering if applicable.
   * @param paginationQuery offset and limit part of query
   * @return iids of base entities that qualify the combination of queries in parameter.
   * @throws RDBMSException
   * @throws SQLException
   */
  public Set<Long> getBaseEntities(String mainQuery, String paginationQuery)
      throws RDBMSException, SQLException
  {
    PreparedStatement statement = currentConnection.prepareStatement(String.format("%s %s", mainQuery, paginationQuery));
    IResultSetParser rs = driver.getResultSetParser(statement.executeQuery()); 
    Set<Long> entityIIDs = new LinkedHashSet<>();
    while (rs.next()) 
      entityIIDs.add(rs.getLong("baseEntityIID"));
    return entityIIDs;
  }
  
  /**
   * Construct the order by expression and add the required joins to it.
   * 
   * @param baseQuery The baseQuery to get entities
   * @param orderBy  A map that contains the direction of sorting(ASC, DESC) against the key of property on which it should be sorted.
   * @param isBaseEntityTableJoined if the base entity table is already joined to the baseQuery or not.
   * @param whereCondition 
   * @return  Query containing the  
   * @throws RDBMSException 
   * @throws SQLException
   */
  public String getOrderByExpression(String baseQuery, Map<String, OrderDirection> orderBy, boolean isBaseEntityTableJoined, String whereCondition)
      throws RDBMSException, SQLException
  {
    if(orderBy.isEmpty()) {
      return baseQuery;
    }
    Boolean isContextualTableJoined = false;
    StringBuilder mainQuery = new StringBuilder(baseQuery);
    StringBuilder orderStatement = new StringBuilder();
    String separator = ",";
    
    DynamicViewDAS viewDas = new DynamicViewDAS(currentConnection, search.getScope().getLocaleIDs());
    String valueRecordView = viewDas.createDynamicValueView();
    
    int joinTableCount = 0;
    for (Entry<String, OrderDirection> order : orderBy.entrySet()) {
      if (CONTEXTUAL_SORTING.contains(order.getKey())) {
        if (!isBaseEntityTableJoined) {
          mainQuery.append(" left join pxp.baseentity be on a.baseentityiid = be.baseentityiid and be.ismerged != true ");
          isBaseEntityTableJoined = true;
        }
        if (!isContextualTableJoined) {
          mainQuery.append(" join pxp.contextualobject ctx on ctx.contextualobjectiid = be.contextualobjectiid ");
          isContextualTableJoined = true;
        }
        orderStatement.append(
            String.format("%s(cxttimerange) %s ", order.getKey().equals(CommonConstants.FROM) ? "lower" : "upper", order.getValue().toString()));
      }
      else {
        IPropertyDTO property = ConfigurationDAO.instance().getPropertyByCode(order.getKey());
        String propertyColumn = getPropertyColumn(property);
        // if base entity join is not already present, then it should be added  for evaluation of flatfields
        if (SORTING_CHARACTERISTICS.contains(propertyColumn)) {
          if (!isBaseEntityTableJoined) {
            mainQuery.append(" left join pxp.baseentity be where a.baseentityiid = be.baseentityiid ");
            isBaseEntityTableJoined = true;
          }
          orderStatement.append(String.format("%s %s", propertyColumn, order.getValue()));
        }
        else {
          long propertyIID = property.getPropertyIID();
          mainQuery.append(
              String.format(" left join %s a_%d on (a_%d.entityiid = a.baseentityiid and a_%d.propertyiid = %d) ", valueRecordView,
                  joinTableCount, joinTableCount, joinTableCount, propertyIID));
          orderStatement.append(String.format("a_%d.%s %s", joinTableCount, propertyColumn, order.getValue()));
          joinTableCount++;
        }
        orderStatement.append(separator);
      }
    }
    orderStatement.setLength(orderStatement.length() - separator.length());
    mainQuery.append(whereCondition);
    mainQuery.append(String.format(" order by %s", orderStatement));
    return mainQuery.toString();
  }
  
  /**
   * 
   * @param property property that should be mapped
   * @return field in relational database corresponding to given property
   */
  public static String getPropertyColumn(IPropertyDTO property)
  {
    switch (property.getCode()) {
      case "createdonattribute":
        return "createtime";
      case "createdbyattribute":
        return "createuseriid";
      case "lastmodifiedattribute":
        return "lastmodifiedtime";
      case "lastmodifiedbyattribute":
        return "lastmodifieduseriid";
      default:
        return "value";
    }
  }
  
  /**
   * 
   * @param allowChildren: should contextual base entities be returned
   * @param baseQuery: baseQuery for getting all base entities along with temporary table.
   * @return query with selection 
   */
  public String getQueryWithSelectField(boolean allowChildren, String baseQuery)
  {
    if (allowChildren) {
      return baseQuery.replace("?", "a.baseentityiid");
    }
    else {
      // need to join for topparentiid
      String baseQueryWithSelection = baseQuery.replace("?", "coalesce(be.topparentiid, be.baseentityiid) as baseentityiid");
      return String.format("%s left join pxp.baseentity be on a.baseentityiid = be.baseentityiid and be.ismerged != true ",baseQueryWithSelection);
    }
  }
  
  
  private static final String Q_CLASSIFIER_COUNT_QUERY = "select cc.classifiercode, count(distinct(%s)) from %s a "
      + "join pxp.baseentity be on be.baseentityiid = a.baseentityiid and be.ismerged != true "
      + "left join pxp.baseentityclassifierlink becl on a.baseentityiid = becl.baseentityiid " + "join pxp.classifierconfig cc "
      + "on be.classifieriid = cc.classifieriid or becl.otherclassifieriid = cc.classifieriid where cc.classifierCode in (%s) %s group by cc.classifieriid";
/**
 * 
 * @param allowChildren if evaluation is on children or not.
 * @param classifierIds classifiers whose count needs to be processed.
 * @return
 * @throws RDBMSException
 * @throws SQLException 
 * @throws CSFormatException 
 */
  public Map<String, Long> getClassCount(Boolean allowChildren, List<String> classifierIds)
      throws RDBMSException, SQLException, CSFormatException
  {
    Map<String, Long> classifierCount = new HashMap<>(); 
    SearchResolver searchResolver = new SearchResolver(currentConnection);
    String queryTable = searchResolver.evaluateTotalResult(search.getScope(), search.getEvaluation());
    String countStatement = allowChildren ? "be.baseentityiid" : "coalesce(be.topparentiid,be.baseentityiid)";
    String query = String.format(Q_CLASSIFIER_COUNT_QUERY, countStatement, queryTable, Text.join(",", classifierIds, "'%s'"), allowChildren.equals(true) ? "":" and be.childlevel = 1 ");
    
    PreparedStatement prepareStatement = currentConnection.prepareStatement(query);    
    IResultSetParser result = driver.getResultSetParser(prepareStatement.executeQuery());
    while (result.next()) {
      long count = result.getLong("count");
      String classifierCode = result.getString("classifierCode");
      classifierCount.put(classifierCode, count);
    }
    return classifierCount;
  }
  
/**
 * 
 * @param allowChildren if children should be evaluated on basis of their parent or not.
 * @param inheritanceSchema language inheritance schema
 * @param properties  properties to get count of
 * @return
 * @throws RDBMSException
 * @throws SQLException 
 * @throws CSFormatException 
 */
  public List<IValueCountDTO> getPropertyCount(Boolean allowChildren,
      Collection<IPropertyDTO> properties, List<String> inheritanceSchema)
      throws RDBMSException, SQLException, CSFormatException
  {
    List<IValueCountDTO> propertyCounts = new ArrayList<>();
    for (IPropertyDTO property : properties) {
      
      SearchResolver searchResolver = new SearchResolver( currentConnection);
      String queryTable = searchResolver.evaluateTotalResult(search.getScope(), search.getEvaluation());
      
      IValueCountDTO valueCountDTO = new ValueCountDTO(property);
      SuperType superType = property.getSuperType();
      if (superType.equals(SuperType.ATTRIBUTE)) {
        valueCountDTO.fillValueVScount(getAttributeCount(allowChildren, inheritanceSchema, property, queryTable));
      }
      else {
        valueCountDTO.fillValueVScount(getTagCount(allowChildren, property, queryTable));
      }
      propertyCounts.add(valueCountDTO);
    }
    return propertyCounts;
  }
  
 /**
  * @param  allowChildren should the evaluation should be on the basis of the child content or not.
  * @param property the tag group of which the count needs to be evaluated.
  * @param queryTable  the temporary table acquired after applying any temporary table if necessary.
  * @return tagValueVSCount the value of the tag vs the count of times it appears.
  * @throws RDBMSException
 * @throws SQLException 
  */
  private static final String Q_TAG_COUNT_QUERY = "select count(distinct(%s)) from %s a join pxp.baseEntity be on be.baseEntityIID = a.baseEntityIID and be.ismerged != true"
     + " join pxp.allTagsRecord tag "
     + " on tag.entityIID = %s and (hstore(usrTags) -> '%s')::int = 100 ";

  private static final String Q_CONTEXT_TAG_COUNT_QUERY = "select count(distinct(%s)) from %s a join pxp.baseEntity be on be.baseEntityIID = a.baseEntityIID and be.ismerged != true"
      + " join pxp.contextualObject co "
      + " on co.contextualObjectIID = be.contextualObjectIID and (hstore(cxtTags) -> '%s')::int = 100 ";
  private Map<String, Long> getTagCount(Boolean allowChildren, IPropertyDTO property, String queryTable) throws RDBMSException, SQLException
  {
   
    Map<String, Long> propertyCount = new HashMap<>();
    long propertyIID = property.getPropertyIID();
    String tagValueQuery = String.format("select tagValueCode from pxp.tagValueConfig where propertyIID= %s", propertyIID);
    PreparedStatement prepareStatement = currentConnection.prepareStatement(tagValueQuery);
    IResultSetParser result = driver.getResultSetParser(prepareStatement.executeQuery());
    List<String> tagValueCodes = new ArrayList<>();
    while (result.next()) {
      String value = result.getString("tagValueCode");
      tagValueCodes.add(value);
    }
    String countStatement = allowChildren ? "be.baseEntityIID" : "coalesce(be.topParentIID,be.baseEntityIID)";

    String finalCountQuery = null;
    for(String tagValueCode : tagValueCodes) {
      String tagCountQuery =  String.format(Q_TAG_COUNT_QUERY, countStatement, queryTable, countStatement,tagValueCode);
      if (allowChildren) {
        String contextTagCountQuery = String.format(Q_CONTEXT_TAG_COUNT_QUERY, countStatement, queryTable, tagValueCode);
        finalCountQuery = String.format("select ((%s) + (%s)) as count", tagCountQuery, contextTagCountQuery);
      }
      else {
        finalCountQuery = tagCountQuery;
      }
      prepareStatement = currentConnection.prepareStatement(finalCountQuery);
      result = driver.getResultSetParser(prepareStatement.executeQuery());  
      while (result.next()) {
        long count = result.getLong("count");
        if(count != 0) {
          propertyCount.put(tagValueCode, count);
        }
      }
    }
    return propertyCount;
  }
  
  private static final String Q_PROPERTY_COUNT_QUERY = "select  b.%s as value, count(distinct(%s)) from %s a "
      + "join pxp.baseEntity be on be.baseEntityIID = a.baseEntityIID and be.ismerged != true "
      + "join %s b on b.propertyIID = %s  and b.entityIID = %s "
      + "where b.%s is not null  and %s group by b.%s";
  private Map<String, Long> getAttributeCount(Boolean allowChildren, List<String> inheritanceSchema, IPropertyDTO property, String queryTable)
      throws RDBMSException, SQLException
  {
    Map<String, Long> propertyCount = new HashMap<>();
    LiteralType literalType = property.getPropertyType().getLiteralType();
    boolean isAttributeNumeric = Arrays.asList(LiteralType.Date, LiteralType.Number).contains(literalType);
    boolean isTrackingProperty = IStandardConfig.isTrackingProperty(property.getIID(), property.getCode());
    
    String valueRecordView = "pxp.allValueRecord";
    if (!isAttributeNumeric && !isTrackingProperty) {
      DynamicViewDAS viewDas = new DynamicViewDAS( currentConnection, inheritanceSchema);
      valueRecordView = viewDas.createDynamicValueView();
    }
    String countStatement = allowChildren ? "be.baseEntityIID" : "coalesce(be.topParentIID, be.baseEntityIID)";
    String comparisonFormat = isAttributeNumeric ? "asNumber" : "value";
    String noValueQuery = " b.value != ''";
    String query = String.format(Q_PROPERTY_COUNT_QUERY, comparisonFormat, countStatement,
        queryTable, valueRecordView, property.getPropertyIID(), countStatement, comparisonFormat,
        noValueQuery, comparisonFormat);

    PreparedStatement prepareStatement = currentConnection.prepareStatement(query);
    IResultSetParser result = driver.getResultSetParser(prepareStatement.executeQuery());
    
    while (result.next()) {
      long count = result.getLong("count");
      String value = result.getString("value");
      propertyCount.put(value, count);
    }
    return propertyCount;
  }
  
  private static final String RULE_DASHBOARD         = "SELECT COALESCE(beqrl.qualityflag, 3) AS qualityflag, count(distinct(be.baseentityiid)) "
      + "AS count, array_agg(be.baseentityiid) AS baseentityiids FROM pxp.baseentityqualityrulelink beqrl RIGHT JOIN %s be "
      + "ON beqrl.baseentityiid = be.baseentityiid and localeid IN ('%s', 'NAL') %s GROUP BY beqrl.qualityflag";
  
  private static final String Q_TOTAL_COUNT          = " select count(be.baseentityiid) as count from %s ";
  private static final String Q_NOT_ALLOWED_CHILDREN = " %s a join pxp.baseentity be on a.baseentityiid = be.baseentityiid and be.ismerged != true ";
  
  public IRuleResultDTO getRuleResult(boolean allowChildren, String localId) throws RDBMSException, SQLException, CSFormatException
  {
    SearchResolver  sr = new SearchResolver( currentConnection);
    String queryTable = sr.evaluateTotalResult(search.getScope(), search.getEvaluation());

    RuleResultDTO ruleResult = new RuleResultDTO(RuleType.dataquality);
    
    String allowChildrenJoin = "";
    if (!allowChildren) {
      allowChildrenJoin = " join pxp.baseentity base on be.baseentityiid = base.baseentityiid where childlevel = 1 AND base.ismerged != true"; 
    }
    String ruleCount = String.format(RULE_DASHBOARD, queryTable, localId, allowChildrenJoin);
    PreparedStatement stmt = currentConnection.prepareStatement(ruleCount);
    stmt.execute();
    IResultSetParser result = driver.getResultSetParser(stmt.getResultSet());
     
    while (result.next()) {
      int flag = result.getInt("qualityflag");
      Long[] baseentityiids = result.getLongArray("baseentityiids");
      long count = result.getLong("count");
      ruleResult.fillDataQualityViolations(QualityFlag.valueOf(flag),
          Sets.newHashSet(baseentityiids), count);
    }
     
   //TO GET TOTAL COUNT 
    String querySource = String.format(Q_NOT_ALLOWED_CHILDREN, queryTable);
    String totalCount = String.format(Q_TOTAL_COUNT, querySource);
    if (!allowChildren) {
      totalCount = totalCount + " where childLevel = 1 ";
    }
    stmt = currentConnection.prepareStatement(totalCount); 
    stmt.execute();
    result = driver.getResultSetParser(stmt.getResultSet());
    result.next();
    ruleResult.setTotalNumberOfEntities(result.getLong("count"));
    return ruleResult;
  }

  private static final String Q_TAXONOMY_COUNT_QUERY = "select x.id, count(distinct(%s)) from %s be %s join pxp.baseEntityClassifierLink becl "
      +" on be.baseEntityIID = becl.baseEntityIID join pxp.classifierConfig cc on becl.otherClassifierIID = cc.classifierIID "
      +" join (select unnest(ARRAY[%s]) as id  ) x on x.id = ANY(cc.hierarchyIIDS) group by x.id";

  public Map<String, Long> getTaxonomyCount(Boolean allowChildren, List<String> classifierCodes)
      throws CSFormatException, SQLException, RDBMSException
  {
    Map<Long, String> IIDVsClassifier = new HashMap<>();
    for(String classifierCode : classifierCodes){
      IClassifierDTO classifierByCode = ConfigurationDAO.instance().getClassifierByCode(classifierCode);
      IIDVsClassifier.put(classifierByCode.getClassifierIID(), classifierCode);
    }

    SearchResolver searchResolver = new SearchResolver(currentConnection);
    String queryTable = searchResolver.evaluateTotalResult(search.getScope(), search.getEvaluation());
    String countStatement = allowChildren ? "be.baseEntityIID" : "coalesce(a.topParentIID, be.baseEntityIID)";
    String extraJoin =  allowChildren ? "" : "join pxp.baseEntity a on a.baseEntityIID = be.baseEntityIID and a.ismerged != true";
    String query = String.format(Q_TAXONOMY_COUNT_QUERY, countStatement, queryTable, extraJoin, Text.join(",", IIDVsClassifier.keySet(), "%s::bigint"));

    Map<String, Long> classifierCount = new HashMap<>();
    PreparedStatement prepareStatement = currentConnection.prepareStatement(query);
    IResultSetParser result = driver.getResultSetParser(prepareStatement.executeQuery());
    while (result.next()) {
      long count = result.getLong("count");
      Long iid = result.getLong("id");
      classifierCount.put(IIDVsClassifier.get(iid), count);
    }
    return classifierCount;
  }
}
