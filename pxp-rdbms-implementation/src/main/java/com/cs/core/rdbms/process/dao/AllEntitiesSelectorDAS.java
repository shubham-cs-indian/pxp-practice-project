package com.cs.core.rdbms.process.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Provide queries against views: - BaseEntityTracking -
 * BaseEntityTrackingWithName - BaseEntityTrackingWithName joined with
 * locale-name tables
 *
 * @author vallee
 */
public class AllEntitiesSelectorDAS extends AllEntitiesAbstractDAS {
  
  private static final String Q_ENTITY_COUNT                   = "SELECT count(*) as nbEntities "
      + "FROM pxp.BaseEntity e ";
  private static final String Q_ENTITY_TRACKING_WITH_NAME_TMPL = "select "
      + "e.*, coalesce( v.value, e.baseEntityBaseName) as baseEntityName "
      + "from pxp.BaseEntityTrackingFullContentWithBaseName e " + "left join pxp.allvaluerecord v"
      + " on v.entityIID = e.baseentityIID and e.ismerged != true and v.localeid = ? and v.propertyIID = "
      + StandardProperty.nameattribute.getIID();
  private static final String WHERE_ALL_ENTITIES               = " where e.childLevel = 1 and e.ismerged != true and e.baseType = ? and e.catalogCode = ?";
  private static final String WHERE_COUNT_FILTER_BY_CLASSIID   = " and (boc.otherclassifierIID in ( ? ) or e.classifierIID in ( ? )) ";
  private static final String Q_FILTER_ENTITY_NOT_IN           = "and e.baseEntityIID not in (?)";
  private static final String WHERE_ENTITY_BY_ID               = " where e.childLevel >= 1 and e.baseentityID = ? and e.catalogCode = ? and e.organizationcode=  ? ";
  private static final String WHERE_ENTITY_BY_IID              = " where e.baseentityIID = ?";
  private static final String FILTER_BY_CLASS                  = " left join pxp.baseEntityClassifierLink boc on boc.baseentityiid = e.baseentityiid ";
  private static final String WHERE_ALL_ENTITIES_WITH_CHILDREN = " where e.childLevel > 0 and e.baseType = ? and e.catalogCode = ?";
  
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
  public AllEntitiesSelectorDAS( RDBMSConnection connection,
      LocaleCatalogDTO localeCatalog, List<String> localeInheritanceSchema, int baseTypeCode)
  {
    super( connection, localeCatalog, localeInheritanceSchema, baseTypeCode);
  }
  
  /**
   * @return the number of all entities in current catalog and base type
   * @throws RDBMSException
   * @throws java.sql.SQLException
   */
  public long queryCount() throws RDBMSException, SQLException
  {
    String finalQuery = Q_ENTITY_COUNT + WHERE_ALL_ENTITIES;
    return getCount(finalQuery);
  }
  
  public long queryCountFilterByClass(List<IClassifierDTO> classifierDTOs)
      throws RDBMSException, SQLException
  {
    String allEntitiesQueryByclassID = Q_ENTITY_COUNT + FILTER_BY_CLASS
        + WHERE_ALL_ENTITIES_WITH_CHILDREN;
    String queryWithFilterClass = queryWithFilterClass(classifierDTOs,
        WHERE_COUNT_FILTER_BY_CLASSIID);
    
    return getCount(allEntitiesQueryByclassID + queryWithFilterClass);
  }
  
  public long queryCountFilterByClassForRelation(List<IClassifierDTO> classifierDTOs,
      List<Long> sideBaseEntityIIDs) throws RDBMSException, SQLException
  {
    String allEntitiesQueryByclassID = Q_ENTITY_COUNT + FILTER_BY_CLASS
        + WHERE_ALL_ENTITIES_WITH_CHILDREN;
    String queryWithFilterClass = queryWithFilterClass(classifierDTOs,
        WHERE_COUNT_FILTER_BY_CLASSIID);
    String queryWithNotIN = queryWithNotIN(sideBaseEntityIIDs, Q_FILTER_ENTITY_NOT_IN);
    
    return getCount(allEntitiesQueryByclassID + queryWithFilterClass + queryWithNotIN);
  }
  
  private long getCount(String finalQuery) throws RDBMSException, SQLException
  {
    PreparedStatement statement = currentConnection.prepareStatement(finalQuery);
    statement.setInt(1, baseType);
    statement.setString(2, localeCatalog.getCatalogCode());
    ResultSet rs = statement.executeQuery();
    return (rs.next() ? rs.getLong("nbEntities") : 0);
  }
  
  /**
   * Return an object tracking and name information
   *
   * @param objectID
   *          the ID of the entity to be queried
   * @return a result set that corresponds to entity information
   * @throws SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IResultSetParser queryEntityByID(String objectID) throws SQLException, RDBMSException
  {
    // Prepare the corresponding query:
    /*String entityQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_TRACKING_WITH_NAME_TMPL + WHERE_ENTITY_BY_ID);*/
    String entityQuery = Q_ENTITY_TRACKING_WITH_NAME_TMPL + WHERE_ENTITY_BY_ID;
    String currentLanguageCode = localeInheritanceSchema.get(0);
    PreparedStatement statement = currentConnection.prepareStatement(entityQuery);
    statement.setString(1, currentLanguageCode);
    statement.setString(2, objectID);
    statement.setString(3, localeCatalog.getCatalogCode());
    statement.setString(4, localeCatalog.getOrganizationCode());
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * Return an object tracking and name information
   *
   * @param objectIID
   *          the IID of the entity to be queried
   * @return a result set that corresponds to entity information
   * @throws SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IResultSetParser queryEntityByIID(long objectIID) throws SQLException, RDBMSException
  {
    // Prepare the corresponding query:
    /*String entityQuery = prepareTrackingQueryWithViewName(
        Q_ENTITY_TRACKING_WITH_NAME_TMPL + WHERE_ENTITY_BY_IID);*/
    String entityQuery = Q_ENTITY_TRACKING_WITH_NAME_TMPL + WHERE_ENTITY_BY_IID;
    String currentLanguageCode = localeInheritanceSchema.get(0);
    PreparedStatement statement = currentConnection.prepareStatement(entityQuery);
    statement.setString(1, currentLanguageCode);
    statement.setLong(2, objectIID);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  public IResultSetParser queryEntitiesByIIDList(String objectIIDList, String technicalVariantIIdList,
      boolean shouldFetchMasterAssets) throws SQLException, RDBMSException
  {
    StringBuilder masterAssetClause = new StringBuilder(" e.baseentityIID in ( ");
    masterAssetClause.append(objectIIDList).append(")");
    
    StringBuilder variantAssetClause = new StringBuilder(" (e.parentIId in ( ");
    variantAssetClause.append(objectIIDList).append(") and c.classifiercode in (")
    .append(technicalVariantIIdList).append(")) ");
    
    StringBuilder query = new StringBuilder(Q_ENTITY_TRACKING_WITH_NAME_TMPL)
        .append(" left join pxp.classifierconfig c on e.classifieriid = c.classifieriid")
        .append(" where ");
    if(shouldFetchMasterAssets) {
      query.append(masterAssetClause).append(" or ");
    }
    query.append(variantAssetClause);
    //String entityQuery = prepareTrackingQueryWithViewName(query.toString());
    String currentLanguageCode = localeInheritanceSchema.get(0);
    PreparedStatement statement = currentConnection.prepareStatement(query.toString());
    statement.setString(1, currentLanguageCode);
    return driver.getResultSetParser(statement.executeQuery());
  }

  
}
