package com.cs.core.rdbms.process.dao;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.services.resolvers.DynamicViewDAS;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Provide queries against views: - BaseEntityTracking -
 * BaseEntityTrackingWithName - BaseEntityTrackingWithName joined with
 * locale-name tables
 *
 * @author vallee
 */
public class AllEntitiesByContextIIDSelectorDAS extends AllEntitiesAbstractDAS {
  
  private final String contextCode;
  
  private final long   parentIID;
  
  private final String Q_ENTITY_COUNT    = "select distinct count(*) as nbEntities from pxp.BaseEntityWithContextualData e ";
  
  private final String Q_ENTITY_CHILDREN = "select  e.baseentityiid as baseentityiid, baseentityid,catalogcode, classifieriid, "
      + "basetype, childlevel, parentiid, embeddedtype, topparentiid, baselocaleid, defaultimageiid, e.contextualobjectiid as contextualobjectiid"
      + ", contextCode, cxtStartTime, cxtEndTime, cxtRanges, cxtTagvalueCodes, cxtlinkedBaseEntitiyIIDs "
      + ", sourcecatalogCode from pxp.BaseEntityWithContextualData e";
  
  private final String Q_QUERY_VALUES    = "select val.propertyIID, val.entityIID, val.recordStatus, val.couplingtype, val.couplingbehavior, val.coupling, val.masterEntityIID, val.masterPropertyIID,    v.valueiid, v.localeid, \r\n"
      + "val.contextualobjectiid, v.value, val.ashtml, val.asnumber, val.unitsymbol, val.calculation from (select * from VALUE_VIEW  where entityiid  CONDITION1 ) "
      + " v join (select * from pxp.AllValueRecord where entityiid CONDITION2 ) "
      + " val on val.valueIID = v.valueIID and ((val.localeID is null and v.localeID is null) or val.localeID = v.localeID)";
  
  private final String Q_QUERY_TAGS      = "select * from pxp.AllTagsRecord val where entityiid ";
  
  private final String Q_CONDITION       = " where e.ismerged != true and e.parentIID= ? and e.contextcode= ? and e.childlevel >= 2 and e.catalogCode = ? ";
  
  /**
   * Initialize this service for operations
   *
   * @param connection
   *          is the RDBMS connection to be currently used
   * @param localeCatalog
   *          is the locale catalog in which the operations take place
   * @param localeInheritanceSchema
   * @param contextCode
   * @param parentIID
   * @param baseTypeCode
   */
  public AllEntitiesByContextIIDSelectorDAS( RDBMSConnection connection,
      LocaleCatalogDTO localeCatalog, List<String> localeInheritanceSchema, int baseTypeCode,
      String contextCode, long parentIID)
  {
    super( connection, localeCatalog, localeInheritanceSchema, baseTypeCode);
    this.contextCode = contextCode;
    this.parentIID = parentIID;
  }
  
  /**
   * Return the number of entities responding to the search criteria
   *
   * @param searchText
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public long queryCount(List<IValueRecordDTO> filteringValueDTO,
      List<ITagsRecordDTO> filteringTagDTO) throws RDBMSException, SQLException
  {
    String filterQuery = prepareFilterQueryForCount(new StringBuilder(Q_ENTITY_COUNT),
        filteringValueDTO, filteringTagDTO);
    
    PreparedStatement statement = currentConnection.prepareStatement(filterQuery);
    statement.setLong(1, parentIID);
    statement.setString(2, contextCode);
    statement.setString(3, localeCatalog.getCatalogCode());
    ResultSet rs = statement.executeQuery();
    return (rs.next() ? rs.getLong("nbEntities") : 0);
  }
  
  /**
   * return entities responding to the search criteria
   *
   * @param filteringValueDTO
   * @param filteringTagDTO
   * @param orderingDirection
   * @param orderingProperty
   * @param offsetExpression
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser queryEntities(List<IValueRecordDTO> filteringValueDTO,
      List<ITagsRecordDTO> filteringTagDTO, OrderDirection orderingDirection,
      IPropertyDTO orderingProperty, String offsetExpression) throws RDBMSException, SQLException
  {
    String filterQuery = prepareFilterQueryForEntities(new StringBuilder(Q_ENTITY_CHILDREN),
        filteringValueDTO, filteringTagDTO, orderingDirection, orderingProperty, offsetExpression);
    
    PreparedStatement statement = currentConnection.prepareStatement(filterQuery);
    statement.setLong(1, parentIID);
    statement.setString(2, contextCode);
    statement.setString(3, localeCatalog.getCatalogCode());
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * return all value records for specified baseEntityIIDs
   *
   * @param baseEntityIIDs
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser queryValueRocordsForEntities(Set<Long> baseEntityIIDs)
      throws RDBMSException, SQLException
  {
    DynamicViewDAS viewDAS = new DynamicViewDAS( currentConnection, localeInheritanceSchema);
    String viewName = viewDAS.createDynamicValueView();
    
    String queryBuildertemp = Q_QUERY_VALUES.replaceFirst("VALUE_VIEW", viewName);
    
    String inQuery = prepareInQuery(baseEntityIIDs);
    
    queryBuildertemp = queryBuildertemp.replace("CONDITION1", inQuery);
    queryBuildertemp = queryBuildertemp.replace("CONDITION2", inQuery);
    
    PreparedStatement statement = currentConnection.prepareStatement(queryBuildertemp.toString());
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * return All tag records for specified baseEntityIIDs
   *
   * @param baseEntityIIDs
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser queryTagsRocordsForEntities(Set<Long> baseEntityIIDs)
      throws RDBMSException, SQLException
  {
    String query = Q_QUERY_TAGS + prepareInQuery(baseEntityIIDs);
    PreparedStatement statement = currentConnection.prepareStatement(query);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  private String prepareInQuery(Set<Long> baseEntityIIDs)
  {
    int index = 0;
    StringBuilder whereCondition = new StringBuilder("");
    for (Long baseEntityIID : baseEntityIIDs) {
      if (index == 0) {
        whereCondition.append(" in (" + baseEntityIID);
      }
      else {
        whereCondition.append(", " + baseEntityIID);
      }
      index++;
    }
    if (index != 0) {
      whereCondition.append(" )");
    }
    return whereCondition.toString();
  }

  private String prepareFilterQueryForEntities(
      StringBuilder query,
      List<IValueRecordDTO> filteringValueDTO,
      List<ITagsRecordDTO> filteringTagDTO,
      OrderDirection orderingDirection,
      IPropertyDTO orderingProperty,
      String offsetExpression)
      throws RDBMSException, SQLException {
    int count;
    String viewName = "";
    boolean orderingPropertyAddedInQuery = false;
    StringBuilder whereCondition = new StringBuilder(Q_CONDITION);
    StringBuilder orderBy = new StringBuilder(" order by ");
    long orderingPropertyIID = 0l;
    
    if (orderingProperty != null) {
      orderingPropertyIID = orderingProperty.getPropertyIID();
    }
    else {
      orderingPropertyIID = StandardProperty.nameattribute.getIID();
    }
    if (filteringValueDTO != null && filteringValueDTO.size() > 0) {
      count = 0;
      DynamicViewDAS dynamicViewDAS = new DynamicViewDAS( currentConnection,
          localeInheritanceSchema);
      viewName = dynamicViewDAS.createDynamicValueView();
      for (IValueRecordDTO recordDTO : filteringValueDTO) {
        query.append(
            " join " + viewName + " pr" + count + " on pr" + count + ".entityiid=e.baseentityiid");
        whereCondition.append(" and pr" + count + ".propertyiid = " + recordDTO.getProperty()
            .getPropertyIID() + " and pr" + count + ".value = '" + recordDTO.getValue() + "'");
        if (orderingPropertyIID == recordDTO.getProperty()
            .getPropertyIID()) {
          orderingPropertyAddedInQuery = true;
          orderBy.append(" pr" + count + ".value ");
        }
        count++;
      }
    }
    if (filteringTagDTO != null && filteringTagDTO.size() > 0) {
      count = 0;
      for (ITagsRecordDTO tagsDTO : filteringTagDTO) {
        int index = 0;
        query.append(" join pxp.TagRecordWithPropertyRecord  tr" + count + " on tr" + count
            + ".baseentityiid=e.baseentityiid");
        for (ITagDTO tagDTO : tagsDTO.getTags()) {
          if (index == 0)
            whereCondition.append(" and (");
          else {
            whereCondition.append(" or ");
          }
          whereCondition.append("( tr" + count + ".tagvalueiid = " + tagDTO.getTagValueCode()
              + " and tr" + count + ".relevance = " + tagDTO.getRange() + " )");
          index++;
        }
        if (index > 0) {
          whereCondition.append(" )");
        }
        count++;
      }
    }
    if (!orderingPropertyAddedInQuery) {
      if (viewName.isEmpty()) {
        DynamicViewDAS dynamicViewDAS = new DynamicViewDAS( currentConnection,
            localeInheritanceSchema);
        viewName = dynamicViewDAS.createDynamicValueView();
      }
      query.append(" left join " + viewName + " op on op.entityiid=e.baseentityiid ");
      whereCondition.append(" and op.propertyIID = " + orderingPropertyIID);
      orderBy.append("op.value ");
    }
    orderBy.append(orderingDirection != null ? orderingDirection : OrderDirection.ASC);
    return query.append(whereCondition)
        .append(orderBy)
        .append(" " + offsetExpression)
        .toString();
  }
  
  private String prepareFilterQueryForCount(StringBuilder query,
      List<IValueRecordDTO> filteringValueDTO, List<ITagsRecordDTO> filteringTagDTO)
      throws RDBMSException, SQLException
  {
    int count;
    StringBuilder whereCondition = new StringBuilder(Q_CONDITION);
    if (filteringValueDTO != null && filteringValueDTO.size() > 0) {
      count = 0;
      DynamicViewDAS dynamicViewDAS = new DynamicViewDAS( currentConnection,
          localeInheritanceSchema);
      String viewName = dynamicViewDAS.createDynamicValueView();
      for (IValueRecordDTO recordDTO : filteringValueDTO) {
        query.append(
            " join " + viewName + " pr" + count + " on pr" + count + ".entityiid=e.baseentityiid");
        whereCondition.append(" and pr" + count + ".propertyiid = " + recordDTO.getProperty()
            .getPropertyIID() + " and pr" + count + ".value = '" + recordDTO.getValue() + "'");
        count++;
      }
    }
    if (filteringTagDTO != null && filteringTagDTO.size() > 0) {
      count = 0;
      for (ITagsRecordDTO tagsDTO : filteringTagDTO) {
        int index = 0;
        query.append(" join pxp.TagRecordWithPropertyRecord  tr" + count + " on tr" + count
            + ".baseentityiid=e.baseentityiid");
        for (ITagDTO tagDTO : tagsDTO.getTags()) {
          if (index == 0)
            whereCondition.append(" and (");
          else {
            whereCondition.append(" or ");
          }
          whereCondition.append("( tr" + count + ".tagvalueiid = " + tagDTO.getTagValueCode()
              + " and tr" + count + ".relevance = " + tagDTO.getRange() + " )");
          index++;
        }
        if (index > 0) {
          whereCondition.append(" )");
        }
        count++;
      }
    }
    return query.append(whereCondition)
        .toString();
  }
}
