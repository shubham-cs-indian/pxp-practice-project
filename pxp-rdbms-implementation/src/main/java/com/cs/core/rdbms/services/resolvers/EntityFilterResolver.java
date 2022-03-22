package com.cs.core.rdbms.services.resolvers;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.scope.CSECompoundEntityFilter;
import com.cs.core.csexpress.scope.CSEEntityByClassifierFilter;
import com.cs.core.csexpress.scope.CSEEntityByCollectionFilter;
import com.cs.core.csexpress.scope.CSEEntityByContextFilter;
import com.cs.core.csexpress.scope.CSEEntityByDuplicateFilter;
import com.cs.core.csexpress.scope.CSEEntityByExpiryFilter;
import com.cs.core.csexpress.scope.CSEEntityByPropertyFilter;
import com.cs.core.csexpress.scope.CSEEntityByQualityFilter;
import com.cs.core.csexpress.scope.CSEEntityByRelationshipFilter;
import com.cs.core.csexpress.scope.CSEEntityByTranslationFilter;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope.ScopeBaseType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * a resolver of entity filter expression as complementary to search resolver
 * @author vallee
 */
class EntityFilterResolver extends AbstractSearchEntityResolver {
  
  protected String localeExpression = " and (localeid %LOCALES% or localeid = 'NAL')";
  /**
   * Create a new service interface for entity filter resolution
   *
   * @param connection current connection
   */
  public EntityFilterResolver( 
          RDBMSConnection connection, Set<String> catalogCodes, Set<ScopeBaseType> baseTypes, Set<String> organizationCodes, Set<Long> baseEntityIIDs, Set<String> endpointCodes) {
    super( connection, catalogCodes, 
            baseTypes.stream()
                    .map(ScopeBaseType::getBaseType)
                    .collect( Collectors.toSet()), "tmpScope", organizationCodes, baseEntityIIDs, endpointCodes);
  }
  
  private String resolveByRelationshipFilter(CSEEntityByRelationshipFilter entityFilter) throws RDBMSException, SQLException, CSFormatException {

    boolean complement = entityFilter.getComplement();
    
    ICSEProperty property = entityFilter.getProperty();
    ICSECouplingSource propertyOwner = entityFilter.getPropertyOwner();
    long iid = propertyOwner.toEntity().getIID();
    
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByCode(property.getCode());
    
    String selectTable =  String.format("pxp.allrelationside%d", property.getSide() == 1 ? 1 : 2);
    
    String searchQuery = String.format("select sideentityiid as baseentityiid from %s where propertyiid = %d and " 
    + "entityiid = %d ", selectTable, propertyDTO.getIID(), iid);
    String createTable = tempTableDas.createTable(searchQuery);
    if(complement) {
      createTable = tempTableDas.excludeResults(getBaseQueryResult(), createTable);
    }
    return createTable;
  }
  
  /**
   * Entry point for filter resolution
   * @param entityFilter
   * @return the temporary table that contains the corresponding base entity IIDs
   * @throws RDBMSException
   * @throws SQLException 
   * @throws CSFormatException 
   */
  public String resolve(ICSEEntityFilterNode entityFilter, List<String> localeIDs) throws RDBMSException, SQLException, CSFormatException {
	if (entityFilter == null) {
        return tempTableDas.createTable(baseQuery);
	}
    
	switch (entityFilter.getType() ) {
      case compound:
        return resolveCompoundFilter( (CSECompoundEntityFilter)entityFilter, localeIDs);
      case byClassifier:
        return resolveByClassifierFilter( (CSEEntityByClassifierFilter)entityFilter);
      case byProperty:
        return resolveByPropertyFilter( (CSEEntityByPropertyFilter)entityFilter, localeIDs);
      case byContext:
        return resolveByContextFilter( (CSEEntityByContextFilter)entityFilter);
      case byQuality:
        return resolveByQualityFilter( (CSEEntityByQualityFilter)entityFilter, localeIDs);
      case byRelationship:
        return resolveByRelationshipFilter((CSEEntityByRelationshipFilter)entityFilter);
      case byCollection:
        return resolveByCollectionFilter((CSEEntityByCollectionFilter)entityFilter);
      case byExpiry:
        return resolveByExpiryFilter((CSEEntityByExpiryFilter) entityFilter);
      case byDuplicate:
        return resolveByDuplicateFilter((CSEEntityByDuplicateFilter) entityFilter);
      case byTranslation:
        return resolveByTranslationFilter((CSEEntityByTranslationFilter) entityFilter);
      
  }
    throw new RDBMSException( 100, "Programm", "Undefined entity filter type: " + entityFilter.getType());
  }

/**
   * Transform a list of parameters into a SQL condition in / not in ( x, y, z)
   * @param parameters
   * @param notFilter
   * @return the SQL expression
   */
  public <T> String  toSQLSearchExpression( Collection<T> parameters, boolean notFilter) {
    if ( parameters.size() == 1 ) {
      return String.format( "%s '%s'", 
              notFilter ? "<> " : "= ", parameters.iterator().next());
    } // else
    return String.format( "%s (%s)", 
              notFilter ? "not in " : "in ",
              Text.join( ",", parameters.stream()
                      .map(parameter -> Text.escapeStringWithQuotes(parameter.toString()))
                      .collect( Collectors.toSet())));
  }
 
  /**
   * prapare conditional query for context with contextcode , starttime and endtime in it
   *  if multiple context exist in entityFilter then it will add OR  between multiple context
   * 
   * @param entityFilter
   * @return conditional query
   */
  private String prepareConditionQueryForContext(CSEEntityByContextFilter entityFilter)
  {
    Collection<ICSEObject> contexts = entityFilter.getContexts();
    if (contexts.isEmpty()) {
      return "";
    }
    String query = " and ";
    int contextSize = contexts.size();
    if (contextSize > 1)
      query = query + " (";
    int index = 0;
    for (ICSEObject context : contexts) {
      // incase of multiple context, add OR operator between multiple context
      if (index > 0)
        query = query + " or ";
      String code = context.getCode();
      String startTime = context.getSpecification(Keyword.$start);
      String endTime = context.getSpecification(Keyword.$end);
      query = String.format(" %s ( co.contextCode = '%s'", query, code);
      // The parentheses '[]' indicate whether the lower and upper bounds are exclusive or inclusive
      if (!startTime.isEmpty() && !endTime.isEmpty())
        query = String.format(" %s and co.cxttimerange && int8range(%s, %s,'[]')", query,
            Long.parseLong(startTime), Long.parseLong(endTime));
      query = query + ")";
      index++;
    }
    if (contextSize > 1)
      query = query + ")";
    return query;
  }

  /**
   * @return the list of classifiers into their IIDs correspondence
   */
  public Set<Long> toClassifierIIDs( CSEEntityByClassifierFilter filter) {
    return filter.getClassifiers().stream()
        .filter( (ICSECouplingSource classif) -> { 
          return classif.isClassifier() && !classif.isPredefined(); 
        })
        .mapToLong(new ToLongFunction<ICSECouplingSource>() {
      @Override
      public long applyAsLong(ICSECouplingSource classifier) {
        long iid;
        try {
          iid = classifier.toClassifier().getIID();
          if ( iid == 0 ) {
            IClassifierDTO classDto = ConfigurationDAO.instance().getClassifierByCode( currentConnection,
                            classifier.toClassifier().getCode());
            iid = classDto.getIID();
          }
        } catch (CSFormatException | SQLException | RDBMSException ex) {
          RDBMSLogger.instance().exception(ex);
          iid = 0;
        }
        return iid;
      }
    })
        .boxed()
        .filter( iid -> { return iid > 0; } )
        .collect( Collectors.toSet());
  }

  private String prepareTempTableForClassifiers(Set<Long> classifierIIDs, Set<Long> taxonomies, Boolean canBeEmpty)
      throws RDBMSException, SQLException
  {
    String cond1 = classifierIIDs.isEmpty() ? "" : String.format("b.otherClassifierIID in (%s)", Text.join(",", classifierIIDs));
    String cond2 = taxonomies.isEmpty() ? "" : String.format("c.hierarchyIIDs && ARRAY[%s]", Text.join(",", taxonomies, "%s::bigint"));
    if(canBeEmpty){
      //condition for permission;
      cond2 = String.format("%s or c.hierarchyIIDs is null", cond2);
    }
    String condition = List.of(cond1, cond2).stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(" or "));

    String join = canBeEmpty ? "left join" : "join";
    String searchQuery = String.format(
        "%s %s pxp.baseEntityClassifierLink b on b.baseEntityIID = a.baseEntityIID and a.ismerged != true %s pxp.classifierConfig c "
            + " on c.classifierIID = b.otherClassifierIID where %s and (%s) ",
        Text.getBefore(baseQuery, "where"), join, join, Text.getAfter(baseQuery, "where"), condition);

    return tempTableDas.createTable(searchQuery);
  }
  
  private String prepareTempTableForClassifiers(Set<Long> classifierIIDs, Set<Long> taxonomies, Boolean canBeEmpty, Boolean hasNot)
      throws RDBMSException, SQLException
  {
    String cond2 = "";
    String cond1 = classifierIIDs.isEmpty() ? "" : String.format("b.otherClassifierIID in (%s)", Text.join(",", classifierIIDs));
    if(hasNot) {
      cond2 = taxonomies.isEmpty() ? "" : String.format("a.baseentityiid not in (Select baseentityiid from pxp.baseentityclassifierlink where otherclassifieriid in (%s))", Text.join(",", taxonomies, "%s::bigint"));
    }
    else {
     cond2 = taxonomies.isEmpty() ? "" : String.format("(c.hierarchyIIDs && ARRAY[%s])", Text.join(",", taxonomies, "%s::bigint"));
    }
    if(canBeEmpty){
      //condition for permission;
      cond2 = String.format("(%s or c.hierarchyIIDs is null)", cond2);
    }
    String condition = List.of(cond1, cond2).stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(" or "));

    String join = canBeEmpty ? "left join" : "join";
    String searchQuery = String.format(
        "%s %s pxp.baseEntityClassifierLink b on b.baseEntityIID = a.baseEntityIID and a.ismerged != true %s pxp.classifierConfig c "
            + " on c.classifierIID = b.otherClassifierIID where %s and %s ",
        Text.getBefore(baseQuery, "where"), join, join, Text.getAfter(baseQuery, "where"), condition);

    return tempTableDas.createTable(searchQuery);
  }

  private String resolveOtherClassifiers(CSEEntityByClassifierFilter filter) throws RDBMSException, SQLException {
    Set<Long> classes = new HashSet<>();
    Set<Long> taxonomies = new HashSet<>();

    Boolean canBeEmpty = false;
    for( ICSECouplingSource classifier  : filter.getClassifiers()){
      String code = ((CSECouplingSource) classifier).getSource().getCode();
      if(code.equals("$empty")){
         canBeEmpty = true;
         continue;
      }
      IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(code);
      switch(classifierDTO.getClassifierType()){
        case CLASS:
          classes.add(classifierDTO.getClassifierIID());
          break;
        case TAXONOMY:
        case MINOR_TAXONOMY:
          taxonomies.add(classifierDTO.getClassifierIID());
          break;
      }
    }
    String response = prepareTempTableForClassifiers( classes, taxonomies, canBeEmpty, filter.hasNot());
    return response;
  }

  /**
   * @param filter
   * @return the query string that search base entity IIDs by nature classifiers
   * @throws RDBMSException 
   */
  private String resolveByNatureClassifier(CSEEntityByClassifierFilter filter) throws RDBMSException, SQLException {
    String searchQuery;
    Predefined owner = filter.getObject().toPredefined();
    Set<Long> classifierIIDs = toClassifierIIDs( filter);
    String conditionStr = toSQLSearchExpression( classifierIIDs, filter.hasNot());
    if ( owner == Predefined.$entity ) {
      searchQuery = baseQuery + " and a.classifierIID " + conditionStr;
    } else {
      String couplingField = "";
      switch (owner) {
        case $parent:
          couplingField = "parentIID";
          break;
        case $top:
          couplingField = "topParentIID";
          break;
        case $origin:
          couplingField = "originBaseEntityIID";
          break;
        case NONE:
        default:
          throw new RDBMSException( 100, "Program", 
                  "search by nature classifier not supported for: " + filter.getObject());
      }
      if ( filter.containsNatureClass() )
        conditionStr = conditionStr.replaceFirst("\\(", "(a.classifierIID,");
      searchQuery = String.format( 
          "%s join pxp.baseentity b on b.baseentityiid = a.%s and a.ismerged != true and where %s and b.classifierIID %s",
          Text.getBefore(baseQuery, "where"), couplingField, 
          Text.getAfter(baseQuery, "where"), conditionStr);
    }
    return tempTableDas.createTable(searchQuery);
  }

  /**
   * @param filter
   * @return the temporary table with results
   * @throws RDBMSException 
   */
  private String resolveByClassifierFilter(CSEEntityByClassifierFilter filter) throws RDBMSException, SQLException {
    switch( filter.getOperator() ) {
      case is:
        return resolveByNatureClassifier( filter);
      case under:
      case in:
        return resolveOtherClassifiers( filter);
    }
    throw new RDBMSException( 100, "Program", "unsupporter operation case: " + filter.getOperator());
  }
  
  /**
   * @return the list of properties into their IIDs correspondence
   */
  public Set<Long> toPropertyIIDs( CSEEntityByPropertyFilter filter) {
    return filter.getProperties().stream()
        .mapToLong( (ICSEProperty property) -> {
          long iid = property.getIID();
          if ( iid == 0 ) {
            try { 
              IPropertyDTO propDto = ConfigurationDAO.instance().getPropertyByCode( currentConnection, property.getCode());
                iid = propDto.getIID();
            } catch ( SQLException | RDBMSException ex) {
              RDBMSLogger.instance().exception( ex);
              iid = 0;
            }
          }
          return iid;
        })
        .boxed()
        .filter( iid -> { return iid > 0; } )
        .collect( Collectors.toSet());
  }
  
  /**
   * @param filter
   * @param localeIDs 
   * @return the query that consists in selecting baseentity IIDs on property condition
   */
  private String resolveByPropertyFilter(CSEEntityByPropertyFilter filter, List<String> localeIDs) {
    Set<Long> propertyIIDs = toPropertyIIDs( filter);
    String conditionStr = toSQLSearchExpression( propertyIIDs, filter.hasNot());
    return  String.format( 
          "%s join pxp.allrecordwithstatus b on b.entityiid = a.baseentityIID "
                  + " where %s and b.propertyIID %s",
          Text.getBefore(baseQuery, "where"), 
          Text.getAfter(baseQuery, "where"), conditionStr);
  }

  /**
   * 
   * @param entityFilter filter
   * @return the query that consists in selecting baseentity IIDs on context condition
   * @throws RDBMSException 
   * @throws SQLException 
   * @throws CSFormatException 
   */
  private String resolveByContextFilter(CSEEntityByContextFilter entityFilter) throws SQLException, RDBMSException, CSFormatException {
    
    long parentIID = entityFilter.getContexts().iterator().next().getIID();
    String conditionQuery = prepareConditionQueryForContext(entityFilter);
    String searchQuery =  String.format( 
          "%s join pxp.contextualobject co on a.contextualobjectiid = co.contextualobjectiid "
                  + " where %s  and a.parentiid = %s %s", 
          Text.getBefore(baseQuery, "where"), 
          Text.getAfter(baseQuery, "where"), parentIID , conditionQuery);
    return tempTableDas.createTable(searchQuery);
  }

  /**
   * 
   * @param entityFilter filter
   * @return the query that consists in selecting baseentity IIDs on qualityflag condition
   * @throws RDBMSException 
   * @throws SQLException 
   */
  private String resolveByQualityFilter(CSEEntityByQualityFilter entityFilter, List<String> localeIDs) throws SQLException, RDBMSException
  {
    Set<Integer> flagOrdinals = entityFilter.getFlags()
        .stream()
        .map(QualityFlag::ordinal)
        .collect(Collectors.toSet());
    
    boolean hasNot = entityFilter.hasNot();
    String conditionStr = String.format("b.qualityflag %s", toSQLSearchExpression(flagOrdinals, false));
    String localeQuery = !localeIDs.isEmpty() ? localeExpression.replace("%LOCALES%", toSQLSearchExpression(localeIDs, false)) : "";
    
    String searchQuery = "";
    if (hasNot) {
      String ruleQuery = String.format("select b.baseentityIID from pxp.baseentityqualityrulelink b where %s %s", conditionStr, localeQuery);
      searchQuery = String.format("%s except %s", baseQuery, ruleQuery);
    }
    else {
      searchQuery = String.format("%s join pxp.baseentityqualityrulelink b on b.baseentityIID = a.baseentityIID and a.ismerged != true " + " where %s and %s %s",
          Text.getBefore(baseQuery, "where"), Text.getAfter(baseQuery, "where"), conditionStr, localeQuery);
    }
    
    return tempTableDas.createTable(searchQuery);
  }

  /**
   *
   * @param filter combination of filters
   * @param localeIds localeIds
   * @return resulting temporary table
   * @throws RDBMSException
   * @throws SQLException if query is incorrectly formed
   * @throws CSFormatException if any issue in decoupling of csexpression
   */
  private String resolveCompoundFilter(CSECompoundEntityFilter filter, List<String> localeIds) throws RDBMSException, SQLException, CSFormatException {
    String resultTable;
    String leftResultTable = resolve( filter.getOperands()[0],localeIds);
    if ( filter.getOperands()[1] == null ) {
      resultTable = leftResultTable;
    } else {
      String rightResultTable = resolve( filter.getOperands()[1], localeIds);
      ICSECalculation.Operator op = filter.getLogicalOperator();
      resultTable = resolveLogicalOperator( op, leftResultTable, rightResultTable);
    }
    if ( filter.hasNot() )
      resultTable = tempTableDas.excludeResults( getBaseQueryResult(), resultTable);
    return resultTable;
  }

  /**
   *
   * @param entityFilter Filter by collection
   * @return Temptable to be returned.
   * @throws RDBMSException
   * @throws SQLException
   */
  private String resolveByCollectionFilter(CSEEntityByCollectionFilter entityFilter) throws SQLException, RDBMSException
  {

    ICSEObject collection = entityFilter.getCollection();
    long iid = collection.getIID();

    String searchQuery = String.format("select cbl.baseentityiid from pxp.collectionbaseentitylink cbl join pxp.baseentity be on cbl.baseentityiid = be.baseentityiid and be.ismerged != true where collectioniid = %d", iid);
    String createTable = tempTableDas.createTable(searchQuery);
    return createTable;
  }

  /**
   *
   * @param entityFilter Filter by Expiry
   * @return Temptable to be returned.
   * @throws RDBMSException
   * @throws SQLException
   */
  private String resolveByExpiryFilter(CSEEntityByExpiryFilter entityFilter) throws SQLException, RDBMSException
  {
    Boolean isExpired = entityFilter.getIsExpired();
    String searchQuery = String.format("%s where %s and isExpired = %s",Text.getBefore(baseQuery, "where"), Text.getAfter(baseQuery, "where"),
        isExpired);
    String createTable = tempTableDas.createTable(searchQuery);
    return createTable;
  }
  
  /**
  *
  * @param entityFilter Filter by IsDuplicate
  * @return Temptable to be returned.
  * @throws RDBMSException
  * @throws SQLException
  */
 private String resolveByDuplicateFilter(CSEEntityByDuplicateFilter entityFilter) throws SQLException, RDBMSException
 {
   Boolean isDuplicate = entityFilter.getIsDuplicate();
   String searchQuery = String.format("%s where %s and isDuplicate = %s",Text.getBefore(baseQuery, "where"), Text.getAfter(baseQuery, "where"),
       isDuplicate);
   String createTable = tempTableDas.createTable(searchQuery);
   return createTable;
 }
 
  /**
   *
   * @param entityFilter Filter by translation
   * @return Temptable to be returned.
   * @throws RDBMSException
   * @throws SQLException
   */
  private String resolveByTranslationFilter(CSEEntityByTranslationFilter entityFilter) throws SQLException, RDBMSException
  {
    List<String> localeIDs = entityFilter.getTranslations().stream().map(translation -> translation.getCode()).collect(Collectors.toList());
    String searchQuery = "select distinct bell.baseentityiid from pxp.baseentitylocaleidlink bell join pxp.baseentity be on bell.baseentityiid = be.baseentityiid and be.ismerged != true ";
    if (!localeIDs.isEmpty() && localeIDs.size() == 1) {
      searchQuery = String.format("%s where bell.localeid = %s", searchQuery, "'" + localeIDs.get(0) +"'");
    }
    else {
      searchQuery = String.format("%s where bell.blocaleid %s ", searchQuery, toSQLSearchExpression(localeIDs, false));
    }
    
    if (!baseEntityIIDs.isEmpty()) {
      if (!localeIDs.isEmpty()) {
        searchQuery = String.format("%s and bell.baseentityiid in (%s)", searchQuery, Text.join(",", baseEntityIIDs));
      }
      else {
        searchQuery = String.format("%s where bell.baseentityiid in (%s)", searchQuery, Text.join(",", baseEntityIIDs));
      }
    }
    
    return tempTableDas.createTable(searchQuery);
  }
  
}
