package com.cs.core.rdbms.process.dao;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Provide queries against views: - BaseEntityTracking -
 * BaseEntityTrackingWithName - BaseEntityTrackingWithName joined with
 * locale-name tables
 *
 * @author vallee
 */
public class AllEntitiesByNameLikeSelectorDAS extends AllEntitiesAbstractDAS {
  
  private static final String Q_ENTITY_COUNT                   = "select count(*) as nbEntities "
      + "from pxp.BaseEntityTrackingFullContentWithBaseName e " + "join " + PROPERTY_VIEW_NAME
      + " v on v.entityIID = e.baseEntityIID and e.ismerged != true and  and v.propertyIID = "
      + StandardProperty.nameattribute.getIID();
  private static final String Q_ENTITY_TRACKING_WITH_NAME_TMPL = "select "
      + "e.*, coalesce( v.value, e.baseEntityBaseName) as baseEntityName "
      + "from pxp.BaseEntityTrackingFullContentWithBaseName e " + "join " + PROPERTY_VIEW_NAME
      + " v on v.entityIID = e.baseEntityIID and e.ismerged != true and v.propertyIID = "
      + StandardProperty.nameattribute.getIID();
  private static final String WHERE_ENTITIES_NAME_LIKE         = " where e.childLevel = 1 and e.baseType = ? and e.catalogCode = ? "
      + " and coalesce( v.value, e.baseEntityBaseName) like ?";
  private static final String JOIN_ON_PROPERTY_VIEW_TMPL       = " left join " + PROPERTY_VIEW_NAME
      + " p on p.entityIID = e.baseEntityIID and p.propertyIID = %d";
  private static final String FILTER_BY_CLASS                  = "left join pxp.baseEntityClassifierLink boc on boc.baseentityiid = e.baseentityiid ";
  private static final String WHERE_FILTER_BY_CLASSIFIERIID    = " and (boc.otherclassifieriid in ( ? ) or e.classifieriid in ( ? ))";
  
  /**
   * Initialize this service for operations
   *
   * @param connection
   *          is the RDBMS connection to be currently used
   * @param localeCatalog
   *          is the locale catalog in which the operations take place
   * @param localeInheritanceSchema
   * @param baseTypeCode
   */
  public AllEntitiesByNameLikeSelectorDAS( RDBMSConnection connection,
      LocaleCatalogDTO localeCatalog, List<String> localeInheritanceSchema, int baseTypeCode)
  {
    super( connection, localeCatalog, localeInheritanceSchema, baseTypeCode);
  }
  
  /**
   * Return the number of entities responding to the search criteria
   *
   * @param searchText
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public long queryCount(String searchText) throws RDBMSException, SQLException
  {
    String localeQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_COUNT + WHERE_ENTITIES_NAME_LIKE);
    PreparedStatement statement = currentConnection.prepareStatement(localeQuery);
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    statement.setString(3, searchText + "%");
    ResultSet rs = statement.executeQuery();
    return (rs.next() ? rs.getLong("nbEntities") : 0);
  }
  
  /**
   * prepare the query for Name Search by catalog, baseType and search text
   *
   * @param baseTypeCode
   *          to search on baseType
   * @param finalQuery
   * @param searchText
   *          search on Name property value
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  private IResultSetParser queryEntitiesByName(StringBuffer finalQuery, String searchText)
      throws RDBMSException, SQLException
  {
    PreparedStatement statement = currentConnection.prepareStatement(finalQuery.toString());
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    statement.setString(3, searchText + "%");
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * Return a result set cursor on entity to be queried by catalog , basetype
   * and searchtext
   *
   * @param offsetExpression
   *          defines the offset expression for cursor navigation
   * @param orderingDirection
   *          the ordering ASC or DESC for cursor navigation
   * @param orderingFields
   *          the characteristics used by cursor navigation for ordering results
   * @param searchText
   * @return a result set on the cursor returned by the query
   * @throws SQLException
   * @throws RDBMSException
   */
  public IResultSetParser queryEntitiesByNameCharacteristic(String offsetExpression,
      IRDBMSOrderedCursor.OrderDirection orderingDirection, List<String> orderingFields,
      String searchText) throws SQLException, RDBMSException
  {
    
    String allEntitiesByNameQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_TRACKING_WITH_NAME_TMPL + WHERE_ENTITIES_NAME_LIKE);
    StringBuffer finalQuery = AllEntitiesAbstractDAS.appendOrderingFieldaAndOffset(offsetExpression,
        orderingDirection, orderingFields, allEntitiesByNameQuery);
    return queryEntitiesByName(finalQuery, searchText);
  }
  
  /**
   * Return a result set cursor on entity to be queried by catalog, baseType and
   * search text
   *
   * @param offsetExpression
   *          defines the offset expression for cursor navigation
   * @param orderingDirection
   *          the ordering ASC or DESC for cursor navigation
   * @param orderingProperty
   *          the characteristics used by cursor navigation for ordering results
   * @param searchText
   *          search on name value
   * @return a result set on the cursor returned by the query
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser queryEntitiesByName(String offsetExpression,
      IRDBMSOrderedCursor.OrderDirection orderingDirection, PropertyDTO orderingProperty,
      String searchText) throws RDBMSException, SQLException
  {
    
    String allEntitiesByNameQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_TRACKING_WITH_NAME_TMPL
            + String.format(JOIN_ON_PROPERTY_VIEW_TMPL, orderingProperty.getIID())
            + WHERE_ENTITIES_NAME_LIKE);
    StringBuffer finalQuery = new StringBuffer(allEntitiesByNameQuery).append(" order by p.value ")
        .append(orderingDirection)
        .append(" ")
        .append(offsetExpression);
    return queryEntitiesByName(finalQuery, searchText);
  }
  
  public IResultSetParser queryEntitiesFilterByClass(String offsetExpression,
      OrderDirection orderingDirection, List<String> orderingFields,
      List<IClassifierDTO> classifierDTOs, String searchText) throws RDBMSException, SQLException
  {
    String allEntitiesQueryByclassID = prepareTrackingQueryWithViewName(
        Q_ENTITY_TRACKING_WITH_NAME_TMPL + FILTER_BY_CLASS + WHERE_ENTITIES_NAME_LIKE);
    String queryWithFilterClass = queryWithFilterClass(classifierDTOs,
        WHERE_FILTER_BY_CLASSIFIERIID);
    
    StringBuffer finalQuery = AllEntitiesAbstractDAS.appendOrderingFieldaAndOffset(offsetExpression,
        orderingDirection, orderingFields, allEntitiesQueryByclassID + queryWithFilterClass);
    return queryEntitiesByName(finalQuery, searchText);
  }
  
  public long queryCountFilterByClass(String searchText, List<IClassifierDTO> classifierDTOs)
      throws RDBMSException, SQLException
  {
    String finalQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_COUNT + FILTER_BY_CLASS + WHERE_ENTITIES_NAME_LIKE);
    String queryWithFilterClass = queryWithFilterClass(classifierDTOs,
        WHERE_FILTER_BY_CLASSIFIERIID);
    PreparedStatement statement = currentConnection
        .prepareStatement(finalQuery + queryWithFilterClass);
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    statement.setString(3, searchText + "%");
    ResultSet rs = statement.executeQuery();
    return (rs.next() ? rs.getLong("nbEntities") : 0);
  }
}
