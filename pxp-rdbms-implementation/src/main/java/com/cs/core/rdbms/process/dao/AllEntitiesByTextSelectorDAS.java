package com.cs.core.rdbms.process.dao;

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
public class AllEntitiesByTextSelectorDAS extends AllEntitiesAbstractDAS {
  
  public static final String  Q_ENTITY_COUNT                = "select count ( distinct be.baseEntityIID ) as nbEntities from pxp.BaseEntityTrackingFullContentWithBaseName be "
      + "join " + AllEntitiesAbstractDAS.PROPERTY_VIEW_NAME
      + " v on v.entityIID = be.baseEntityIID and be.ismerged != true ";
  private static final String Q_ENTITY_BY_TEXT_SEARCH       = "select be.*, coalesce( v.value, be.baseEntityBaseName) as baseEntityName"
      + " from pxp.BaseEntityTrackingFullContentWithBaseName be " + "join "
      + AllEntitiesAbstractDAS.PROPERTY_VIEW_NAME + " v on v.entityIID = be.baseEntityIID and be.ismerged != true ";
  private static final String WHERE_IN_CATALOG              = "where be.baseType = ? and be.catalogCode = ? and v.value like ? ";
  private static final String JOIN_ON_PROPERTY_VIEW_TMPL    = " left join " + PROPERTY_VIEW_NAME
      + " p on p.entityIID = be.baseEntityIID and p.propertyIID = %d";
  private static final String FILTER_BY_CLASS               = "left join pxp.baseEntityClassifierLink boc on boc.baseentityiid = be.baseentityiid ";
  private static final String WHERE_FILTER_BY_CLASSIFIERIID = "and (boc.otherclassifieriid in ( ? ) or be.classifieriid in ( ? ))";
  
  /**
   * Initialize this service for operations
   *
   * @param driver
   * @param connection
   *          is the RDBMS connection to be currently used
   * @param localeCatalog
   *          is the locale catalog in which the operations take place
   * @param localeInheritanceSchema
   * @param baseTypeCode
   */
  public AllEntitiesByTextSelectorDAS(RDBMSConnection connection,
      LocaleCatalogDTO localeCatalog, List<String> localeInheritanceSchema, int baseTypeCode)
  {
    super( connection, localeCatalog, localeInheritanceSchema, baseTypeCode);
  }
  
  /**
   * @param searchText
   * @return the number of all entities in current catalog and base type
   * @throws RDBMSException
   * @throws java.sql.SQLException
   */
  public long queryCount(String searchText) throws RDBMSException, SQLException
  {
    String finalQuery = prepareTrackingQueryWithViewName(Q_ENTITY_COUNT + WHERE_IN_CATALOG);
    PreparedStatement statement = currentConnection.prepareStatement(finalQuery);
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    statement.setString(3, "%" + searchText + "%");
    
    ResultSet rs = statement.executeQuery();
    return (rs.next() ? rs.getLong("nbEntities") : 0);
  }
  
  /**
   * prepare the query for textSearch by catalog and search text
   *
   * @param allEntitiesByTextQuery
   * @param searchText
   *          search on text type property value
   * @return ResultSet on the cursor returned by the query
   * @throws SQLException
   * @throws RDBMSException
   */
  private IResultSetParser queryEntitiesByText(StringBuffer allEntitiesByTextQuery,
      String searchText) throws SQLException, RDBMSException
  {
    PreparedStatement statement = currentConnection
        .prepareStatement(allEntitiesByTextQuery.toString());
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    statement.setString(3, "%" + searchText + "%");
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * Return a result set cursor on entity to be queried by catalog and search
   * text
   *
   * @param offsetExpression
   *          defines the offset expression for cursor navigation
   * @param orderingDirection
   *          the ordering ASC or DESC for cursor navigation
   * @param orderingFields
   *          the characteristics used by cursor navigation for ordering results
   * @param searchText
   *          search on text type property value
   * @return a result set on the cursor returned by the query
   * @throws SQLException
   * @throws RDBMSException
   */
  public IResultSetParser queryEntitiesByTextCharacteristic(String offsetExpression,
      IRDBMSOrderedCursor.OrderDirection orderingDirection, List<String> orderingFields,
      String searchText) throws SQLException, RDBMSException
  {
    
    String allEntitiesQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_BY_TEXT_SEARCH + WHERE_IN_CATALOG);
    StringBuffer finalQuery = AllEntitiesAbstractDAS.appendOrderingFieldaAndOffset(offsetExpression,
        orderingDirection, orderingFields, allEntitiesQuery);
    return queryEntitiesByText(finalQuery, searchText);
  }
  
  /**
   * * Return a result set cursor on entity to be queried by catalog and base
   * entity and order by an entity property
   *
   * @param offsetExpression
   *          defines the offset expression for cursor navigation
   * @param orderingDirection
   *          the ordering ASC or DESC for cursor navigation
   * @param orderingProperty
   *          the loaded property on which to order
   * @param searchText
   *          search on text type property value
   * @return a result set on the cursor returned by the query
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser queryEntitiesByTextProperty(String offsetExpression,
      IRDBMSOrderedCursor.OrderDirection orderingDirection, PropertyDTO orderingProperty,
      String searchText) throws RDBMSException, SQLException
  {
    
    String allEntitiesQuery = prepareTrackingQueryWithViewName(Q_ENTITY_BY_TEXT_SEARCH
        + String.format(JOIN_ON_PROPERTY_VIEW_TMPL, orderingProperty.getIID()) + WHERE_IN_CATALOG);
    
    StringBuffer finalQuery = new StringBuffer(allEntitiesQuery).append(" order by p.value ")
        .append(orderingDirection)
        .append(" ")
        .append(offsetExpression);
    return queryEntitiesByText(finalQuery, searchText);
  }
  
  public IResultSetParser queryEntitiesFilterByClass(String offsetExpression,
      OrderDirection orderingDirection, List<String> orderingFields,
      List<IClassifierDTO> classifierDTOs, String searchText) throws RDBMSException, SQLException
  {
    String allEntitiesQueryByclassID = prepareTrackingQueryWithViewName(
        Q_ENTITY_BY_TEXT_SEARCH + FILTER_BY_CLASS + WHERE_IN_CATALOG);
    String queryWithFilterClass = queryWithFilterClass(classifierDTOs,
        WHERE_FILTER_BY_CLASSIFIERIID);
    
    StringBuffer finalQuery = AllEntitiesAbstractDAS.appendOrderingFieldaAndOffset(offsetExpression,
        orderingDirection, orderingFields, allEntitiesQueryByclassID + queryWithFilterClass);
    return queryEntitiesByText(finalQuery, searchText);
  }
  
  public long queryCountFilterByClass(String searchText, List<IClassifierDTO> classifierDTOs)
      throws RDBMSException, SQLException
  {
    String finalQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_COUNT + FILTER_BY_CLASS + WHERE_IN_CATALOG);
    String queryWithFilterClass = queryWithFilterClass(classifierDTOs,
        WHERE_FILTER_BY_CLASSIFIERIID);
    PreparedStatement statement = currentConnection
        .prepareStatement(finalQuery + queryWithFilterClass);
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    statement.setString(3, "%" + searchText + "%");
    ResultSet rs = statement.executeQuery();
    return (rs.next() ? rs.getLong("nbEntities") : 0);
  }
}
