package com.cs.core.rdbms.entity.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.SystemLevelIds;
import com.cs.core.data.Text;
import com.cs.core.data.TextArchive;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordNotificationDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordNotificationDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.services.resolvers.DynamicViewDAS;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.google.common.primitives.Longs;


/**
 * This special interface regroup methods for entity properties management
 *
 * @author vallee
 */
public class BaseEntityDAS extends RDBMSDataAccessService {

  static final String         Q_GET_CONTEXTUAL                            = "select contextualObjectIID, contextCode, cxtTags,"
      + " lower(cxtTimeRange) as cxtStartTime, upper(cxtTimeRange) as cxtEndTime"
      + " from pxp.contextualObject where contextualObjectIID = ?";
  
  static final String         Q_DELETE_ALL_CLASSIFIERLINK                 = "DELETE FROM pxp.baseentityclassifierlink "
      + "WHERE baseentityiid = ? ";
  static final String         Q_GET_CLASSIFIERS                           = "select otherClassifierIID as classifierIID from pxp.baseEntityClassifierLink where baseentityiid = ?";
  static final String         Q_GET_BASEENTITY_CONTEXTUAL_LINKED_ENTITIES = "select becl.classifierIID as classifierIID, becl.catalogcode as catalogcode, becl.organizationCode as organizationCode, "
      + "becl.baseEntityID as baseEntityID, becl.basetype as basetype, becl.baseLocaleID as baseLocaleID, "
      + "becl.baseEntityIID as baseEntityIID, becl.baseEntityName as baseEntityName from pxp.BaseEntityContextualLinkedEntities becl join pxp.baseentity be "
      + "on becl.baseentityiid = be.baseentityiid and be.ismerged != true where becl.contextualobjectiid=?";
  static final String         Q_GET_ALL_CHILD_ENTITES                              = "select baseentityiid from pxp.BaseEntity where parentiid = ?";
  static final String         Q_GET_ALL_CHILD_ENTITES_WITH_ORIGIN_BASE_ENTITY_IIDS = "select baseentityiid, originbaseentityiid  from pxp.BaseEntity where parentiid = ?";
  
  private static final String Q_REMOVEV_VALUE_RECORDS                     = "DELETE from pxp.valuerecord where  valueiid in ";
  
  // Query for language inheritance notification
  private static final String PROPERTY_VIEW_NAME                          = "propertyViewName";
  private static final String Q_GET_LANGUAGE_NOTIFIED_PROPERTIES_IIDS     = "select * from " + PROPERTY_VIEW_NAME + " where entityiid = ? ";
  private static final String Q_WHERE_PROPERTY_IIDS                       = " and PropertyIID in (?)";
  
  static final String         Q_DELETE_CLASSIFIERLINK                              = "DELETE FROM pxp.baseentityclassifierlink "
      + "WHERE baseentityiid = ? " + "and otherclassifieriid IN ";
  private static final String Q_GET_CLASSIFIER_IIDS                                = "SELECT classifieriid from pxp.classifierconfig where classifierCode IN ";
  
  //Query to fetch all attribute variants related to a property id of a base entity
  private static final String Q_GET_ATTRIBUTE_VARIANT_IIDS_BY_PROPERTY_IIDS = "SELECT vr.valueiid,co.contextualobjectiid,co.contextcode from pxp.valuerecord vr join"
      + " pxp.contextualobject co on vr.contextualobjectiid = co.contextualobjectiid where ";
  
  private static final String Q_INSERT_INTO_BASE_ENTITY_ARCHIVE = "insert into pxp.baseentityarchive (entityiid, objectArchive, assetObjectKey) values (?,?,?)";
  
  private static final String Q_INSERT_INTO_PURGED_ASSET= "insert into pxp.assetstobepurged (assetObjectKey,thumbKey,previewImageKey,type) values (?,?,?,?)";
  
  static final String         Q_GET_DEST_ENTITY_USING_ORIGIN_BASE_ENTITY_IID = "select baseentityiid from pxp.BaseEntity where originbaseentityiid = ? and catalogcode = ? ";
 
  public BaseEntityDAS(RDBMSConnection connection) {
    super( connection);
  }

  /**
   * @param contextualObjectIID
   * @return specifically loaded contextual object
   * @throws SQLException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public ContextualDataDTO loadContextualObject(long contextualObjectIID)
          throws SQLException, RDBMSException, CSFormatException {
    PreparedStatement query = currentConnection.prepareStatement(Q_GET_CONTEXTUAL);
    query.setLong(1, contextualObjectIID);
    ContextualDataDTO data = new ContextualDataDTO();
    IResultSetParser rs = driver.getResultSetParser(query.executeQuery());
    if (rs.next()) {
      data = new ContextualDataDTO(rs);
    }
    return data;
  }

  public String createClonedID(long baseEntityIID) throws SQLException, RDBMSException {
    IResultSetParser result = driver
            .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.STRING, "pxp.fn_newClonedBaseEntityID")
            .setInput(ParameterType.IID, baseEntityIID)
            .execute();
    return result.getString();
  }

  /**
   * @param entityIID
   * @param classifierIDs the other classifiers to be removed
   * @throws SQLException
   * @throws RDBMSException 
   */
  public void removeOtherClassifiers(long entityIID, Set<String> classifierIDs) throws SQLException, RDBMSException
  {
    StringBuffer query = new StringBuffer(Q_DELETE_CLASSIFIERLINK);
    query.append("(").append(Q_GET_CLASSIFIER_IIDS).append(prepareIn(classifierIDs)).append(")");
    PreparedStatement ps = currentConnection.prepareStatement(query.toString());
    ps.setLong(1, entityIID);
    ps.executeUpdate();
    
  }
  
  private String prepareIn(Set<String> ids) {
    StringBuffer buffer = new StringBuffer("(");
    boolean first = true;
    for (String id: ids)
    {
      if (first) {
        buffer.append("'").append(id).append("'");
        first = false;
      }
      else
        buffer.append(",'").append(id).append("'");
    }
    return buffer.append(")").toString();
  }
  
  public void removeAllOtherClassifiers(long entityIID) throws SQLException {
    QueryRunner query = new QueryRunner();
    query.update(currentConnection.getConnection(), Q_DELETE_ALL_CLASSIFIERLINK, entityIID);
  }

  /**
   * @param entityIID
   * @param classifierIIDs
   * @throws SQLException
   * @throws RDBMSException
   */
  public void addOtherClassifiers(long entityIID, Set<Long> classifierIIDs)
          throws SQLException, RDBMSException {
    driver.getProcedure(currentConnection, "pxp.sp_addClassifiers")
            .setInput(ParameterType.IID, entityIID)
            .setInput(ParameterType.IID_ARRAY, classifierIIDs)
            .execute();
  }

  /**
   * @param entityIID
   * @return the query return of other classifiers
   * @throws RDBMSException
   * @throws SQLException
   */
  IResultSetParser getOtherClassifiers(long entityIID) throws RDBMSException, SQLException {
    PreparedStatement cs = currentConnection.prepareStatement(Q_GET_CLASSIFIERS);
    cs.setLong(1, entityIID);
    return driver.getResultSetParser(cs.executeQuery());
  }

  /**
   * @param entity content with contextual data to update
   */
  private void updateContextualData(BaseEntityDTO entity) throws SQLException, RDBMSException {
    
    ContextualDataDTO contextualDataDTO = (ContextualDataDTO) entity.getContextualObject();
    String cxtTags = contextualDataDTO.getHStoreFormat();
    driver.getProcedure(currentConnection, "pxp.sp_updateBaseEntityContextualData")
        .setInput(ParameterType.IID, entity.getBaseEntityIID())
        .setInput(ParameterType.LONG, contextualDataDTO.getContextStartTime())
        .setInput(ParameterType.LONG, contextualDataDTO.getContextEndTime())
        .setInput(ParameterType.STRING, cxtTags.isEmpty() ? null : cxtTags)
        .setInput(ParameterType.IID_ARRAY, contextualDataDTO.getLinkedBaseEntityIIDs()).execute();
  }

  
  
  /**
   * @param entity
   * @param l
   * @return the new entity IID
   * @throws SQLException
   * @throws RDBMSException
   */
  void createBaseEntity(BaseEntityDTO entity, long userIID) throws SQLException, RDBMSException {
    IResultSetParser result = driver
        .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.IID, "pxp.fn_createBaseEntity")
        .setInput(ParameterType.LONG, entity.getBaseEntityIID())
        .setInput(ParameterType.STRING, entity.getBaseEntityID())
        .setInput(ParameterType.STRING, entity.getContextualObject().getContextCode().isEmpty() ? null : entity.getContextualObject().getContextCode())
        .setInput(ParameterType.STRING, entity.getLocaleCatalog().getCatalogCode())
        .setInput(ParameterType.STRING, entity.getLocaleCatalog().getOrganizationCode())
        .setInput(ParameterType.IID, entity.getNatureClassifier().getIID())
        .setInput(ParameterType.INT, entity.getBaseType().ordinal())
        .setInput(ParameterType.INT, entity.getChildLevel())
        .setInput(ParameterType.STRING, entity.getSourceCatalogCode().isEmpty() ? null : entity.getSourceCatalogCode())
        .setInput(ParameterType.STRING, entity.getLocaleCatalog().getLocaleID())
        .setInput(ParameterType.LONG, entity.getParentIID() == 0 ? null : entity.getParentIID())
        .setInput(ParameterType.LONG, entity.getTopParentIID() == 0 ? null : entity.getTopParentIID())
        .setInput(ParameterType.LONG, entity.getDefaultImageIID() == 0 ? null : entity.getDefaultImageIID())
        .setInput(ParameterType.LONG, entity.getOriginBaseEntityIID() == 0 ? null : entity.getOriginBaseEntityIID())
        .setInput(ParameterType.STRING, entity.getHashCode().isEmpty() ? null : entity.getHashCode())
        .setInput(ParameterType.JSON, entity.getEntityExtension().isEmpty() ? null : entity.getEntityExtension().toString())
        .setInput(ParameterType.LONG, System.currentTimeMillis()).setInput(ParameterType.IID, userIID)
        .setInput(ParameterType.BOOLEAN, entity.isClone())
        .setInput(ParameterType.STRING, entity.getEndpointCode().isEmpty() ? null : entity.getEndpointCode())
        .setInput(ParameterType.STRING, entity.getSourceOrganizationCode())
        .execute();
    entity.setIID(result.getIID());
    // Update contextual data
    if (!entity.getContextualObject()
            .isNull()) {
      updateContextualData(entity);
    }
    addLanguageTranslation(entity, Arrays.asList(entity.getBaseLocaleID()));
  }

  /**
   * @param entity the content that must be updated
   * @throws SQLException
   * @throws RDBMSException
   */
  void updateBaseEntity(BaseEntityDTO entity, long userIID) throws SQLException, RDBMSException {
    if (entity.getBaseEntityIID() == 0) {
      throw new RDBMSException(100, "Inconsistent usage",
              "cannot update an entity without IID - Entity ID: " + entity.getBaseEntityID());
    }
    if (entity.isChanged()) {
      driver.getProcedure(currentConnection, "sp_updateBaseEntity")
              .setInput(ParameterType.IID, entity.getIID())
              .setInput(ParameterType.IID,
                      entity.getDefaultImageIID() == 0 ? null : entity.getDefaultImageIID())
              .setInput(ParameterType.STRING, entity.getHashCode()
                      .isEmpty() ? null : entity.getHashCode())
              .setInput(ParameterType.JSON, entity.getEntityExtension().isEmpty() ? null : entity.getEntityExtension().toString())
              .setInput(ParameterType.LONG, System.currentTimeMillis())
              .setInput(ParameterType.IID, userIID)
              .execute();
    } else {
      driver.getProcedure(currentConnection, "sp_updateBaseEntityTracking")
              .setInput(ParameterType.IID, entity.getIID())
              .setInput(ParameterType.LONG, System.currentTimeMillis())
              .setInput(ParameterType.IID, userIID)
              .execute();
    }
    if (!entity.getContextualObject()
            .isNull() && entity.getContextualObject()
                    .isChanged()) {
      updateContextualData(entity); // Update contextual data
    }
  }

  public void removeValueRecords(IValueRecordDTO[] records) throws RDBMSException, SQLException {
    StringBuffer query = new StringBuffer(Q_REMOVEV_VALUE_RECORDS);
    query.append("(")
            .append( Text.getStringSequence(records.length, ',', "?"))
            .append(")");
    PreparedStatement prepareStatement = currentConnection.prepareStatement(query.toString());
    for (int i = 0; i < records.length; i++) {
      prepareStatement.setLong(1 + i, records[i].getValueIID());
    }
    prepareStatement.execute();
  }

  IResultSetParser getContextualLinkedEntities(long contexualObjectIID)
          throws RDBMSException, SQLException {
    PreparedStatement cs = currentConnection
            .prepareStatement(Q_GET_BASEENTITY_CONTEXTUAL_LINKED_ENTITIES);
    cs.setLong(1, contexualObjectIID);
    return driver.getResultSetParser(cs.executeQuery());
  }

  public long[] getAllChildrens(long parentIID) throws RDBMSException, SQLException {
    PreparedStatement cs = currentConnection.prepareStatement(Q_GET_ALL_CHILD_ENTITES);
    cs.setLong(1, parentIID);
    IResultSetParser resultSet = driver.getResultSetParser(cs.executeQuery());
    ArrayList<Long> childIIDs = new ArrayList<>();
    while (resultSet.next()) {
      childIIDs.add(resultSet.getLong(1));
    }
    return Longs.toArray(childIIDs);
  }
  
  public Map<Long, Long> getAllChildrensOriginWithBaseEntityIID(long parentIID) throws RDBMSException, SQLException {
    PreparedStatement cs = currentConnection.prepareStatement(Q_GET_ALL_CHILD_ENTITES_WITH_ORIGIN_BASE_ENTITY_IIDS);
    cs.setLong(1, parentIID);
    IResultSetParser resultSet = driver.getResultSetParser(cs.executeQuery());
    Map<Long,Long> childIIDWithOriginIID = new HashMap<>();
    while (resultSet.next()) {
      childIIDWithOriginIID.put(resultSet.getLong(1), resultSet.getLong(2));
    }
    return childIIDWithOriginIID;
  }
  
  public void updateOriginBaseEntityIId(long originBaseEntityIId, long baseEntityIId) throws RDBMSException, SQLException
  {
    String query = "update  pxp.baseentity set originbaseentityiid ="+ originBaseEntityIId +" where baseentityiid = " +baseEntityIId;
    QueryRunner queryRunner = new QueryRunner();
    queryRunner.update(currentConnection.getConnection(), query);
  }
  
  public Map<Long, IValueRecordNotificationDTO> getLanguageNotifiedProperties(long baseEntityIID,
      List<String> localeInheritanceSchema, Set<Long> propertyIIDs, Map<Long, IValueRecordNotificationDTO> notificationProperties, String localeID) throws RDBMSException, SQLException
  {
    List<String> localeInheritance = new ArrayList<>(localeInheritanceSchema);
    localeInheritance.remove(localeID);
    DynamicViewDAS propertyService = new DynamicViewDAS( currentConnection,localeInheritance);
    String viewName = propertyService.createDynamicValueView();
    String propertyIIDsQuery = Text.join(",", propertyIIDs);
    String queryPropertyBYIIDS = Q_WHERE_PROPERTY_IIDS.replace("?", propertyIIDsQuery);
    
    // Prepare the corresponding query:
    String query = Q_GET_LANGUAGE_NOTIFIED_PROPERTIES_IIDS.replace(PROPERTY_VIEW_NAME, viewName) + queryPropertyBYIIDS;
    PreparedStatement cs = currentConnection.prepareStatement(query);
    cs.setLong(1, baseEntityIID);
    
    IResultSetParser resultSet = driver.getResultSetParser(cs.executeQuery());
   
    while (resultSet.next()) {
      IValueRecordNotificationDTO notificationDTO = new ValueRecordNotificationDTO();
      long propertyIID = resultSet.getLong("propertyIID");
      
      notificationDTO.setPropertyIID(propertyIID);
      notificationDTO.setLocaleID(resultSet.getString("localeID"));
      notificationDTO.setValue(resultSet.getString("value"));
      notificationDTO.setAsHTML(resultSet.getString("ashtml"));
      
      notificationProperties.put(propertyIID, notificationDTO);
    }
    return notificationProperties;
  }
  
  public IResultSetParser getContextualObjectIIdsForVariantsToDelete(long assetInstanceId,
      List<String> autoCreateContextCodes) throws RDBMSException, SQLException {
    StringBuilder query = new StringBuilder("select co.contextualobjectiid from pxp.contextualobject co join "
        + " pxp.baseentity be ON be.contextualObjectIID = co.contextualObjectIID where be.parentIID = ")
        .append(assetInstanceId)
        .append(" and co.contextCode in (")
        .append(Text.join(",", autoCreateContextCodes, "'%s'"))
        .append(")");
    PreparedStatement ps = currentConnection.prepareStatement(query);
    return driver.getResultSetParser(ps.executeQuery());
  }
  
  public IResultSetParser getDuplicateAssetsIfExist(String hashKey, long idToExclude,
      String physicalCatalogId, String organizationId, String endpointId) throws SQLException, RDBMSException {
    organizationId = organizationId.equals("-1") ? IStandardConfig.STANDARD_ORGANIZATION_RCODE : organizationId;
    StringBuilder query = new StringBuilder("Select baseentityiid from pxp.baseentity where ismerged != true and hashcode = '")
        .append(hashKey)
        .append("' and catalogcode = '").append(physicalCatalogId)
        .append("' and organizationcode = '").append(organizationId).append("'")
        .append(" and NOT (embeddedtype IS NOT NULL and parentiid IS NULL)")
        .append(" and baseentityiid <> ").append(idToExclude);
    PreparedStatement ps = currentConnection.prepareStatement(query);
    return driver.getResultSetParser(ps.executeQuery());
  }
  
  public IResultSetParser updateAssetExpiryStatus(long currentTimeStamp)
      throws RDBMSException, SQLException
  {
    int baseType = BaseType.ASSET.ordinal();
    StringBuilder queryUpdateExpiryStatus = new StringBuilder("Update pxp.baseentity ")
        .append(" SET isExpired = true Where baseentityiid IN (")
        .append(" Select baseentityiid From  pxp.baseentity As entity ")
        .append(" JOIN pxp.valuerecord on entityiid = baseentityiid ")
        .append(" JOIN pxp.propertyconfig USING(propertyiid)")
        .append(" WHERE propertyiid IN ( ")
        .append(" Select propertyiid FROM pxp.propertyconfig WHERE propertycode = '")
        .append(SystemLevelIds.END_DATE_ATTRIBUTE)
        .append("' ) ")
        .append(" AND basetype = ").append(baseType)
        .append(" AND isExpired = false AND ")
        .append(currentTimeStamp)
        .append(" > value::bigint ) RETURNING baseentityiid ");
    PreparedStatement statement = currentConnection.prepareStatement(queryUpdateExpiryStatus);
    return driver.getResultSetParser(statement.executeQuery());
  }

  public void updateExpiryStatusForAssetByIID(long baseEntityIID, boolean status) throws SQLException, RDBMSException
  {
    StringBuilder queryForUpdateExpiryStatus = new StringBuilder("UPDATE pxp.baseentity ")
        .append("SET isExpired = ")
        .append(status)
        .append(" where baseentityiid = ")
        .append(baseEntityIID);
    
    PreparedStatement statement = currentConnection.prepareStatement(queryForUpdateExpiryStatus);
    statement.executeUpdate();
  }
  
  /**
   * create language translation and update baseEntity (last modified)
   * 
   * @param entity
   * @param userIID
   * @param localeID
   * @throws SQLException
   * @throws RDBMSException
   */
  public void createLanguageTranslation(BaseEntityDTO entity, long userIID, String localeID) throws SQLException, RDBMSException
  {
    
    addLanguageTranslation(entity, Arrays.asList(localeID));
    updateBaseEntity(entity, userIID);
  }
  
  /**
   * create Language Translation (add entry in baseentitylocaleidlink table)
   * 
   * @param entity BaseEntityDTO.
   * @param localeIDs list of locale ids
   * @throws SQLException
   * @throws RDBMSException
   */
  public void addLanguageTranslation(BaseEntityDTO entity, List<String> localeIds) throws SQLException, RDBMSException {
   
    driver.getProcedure(currentConnection, "pxp.sp_addLanguageTranslation")
    .setInput(ParameterType.IID, entity.getBaseEntityIID())
    .setInput(ParameterType.STRING_ARRAY, localeIds)
    .execute();
  }
  
  public IResultSetParser getIIdsExistInLocale(List<Long> baseEntityIIds, String localeId)
      throws RDBMSException, SQLException
  {
    StringBuilder queryForUpdateExpiryStatus = new StringBuilder("Select be.baseentityiid baseentityiid from pxp.baseentity be,"
        + " pxp.baseentitylocaleidlink bll where be.baseentityiid = bll.baseentityiid and be.ismerged != true and bll.localeid = '")
        .append(localeId)
        .append("' and be.baseentityiid IN (")
        .append(Text.join(",", baseEntityIIds))
        .append(")");
    
    PreparedStatement statement = currentConnection.prepareStatement(queryForUpdateExpiryStatus);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  public IResultSetParser filterIIdsByClassifierIIds(List<Long> baseEntityIIds, List<Long> classifierIIds)
      throws RDBMSException, SQLException
  {
    String classifierIIdsString = Text.join(",", classifierIIds);
    StringBuilder queryForUpdateExpiryStatus = new StringBuilder("Select distinct be.baseentityiid from pxp.baseentity be "
        + "LEFT JOIN pxp.baseentityclassifierlink bcl ON be.baseentityiid = bcl.baseentityiid where be.ismerged != true and be.baseentityiid IN (")
        .append(Text.join(",", baseEntityIIds))
        .append(")")
        .append(" and (bcl.otherclassifieriid IN (")
        .append(classifierIIdsString)
        .append(") OR be.classifieriid IN (")
        .append(classifierIIdsString)
        .append("))");
    
    PreparedStatement statement = currentConnection.prepareStatement(queryForUpdateExpiryStatus);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  public void markAssetsDuplicateByIIds(Set<Long> baseEntityIIds, boolean isDuplicate) throws RDBMSException, SQLException {
    StringBuilder queryToUpdateDuplicateStatus = new StringBuilder("UPDATE pxp.baseentity "
        + "SET isDuplicate = ")
        .append(isDuplicate)
        .append(" where baseentityiid IN (")
        .append(Text.join(",", baseEntityIIds))
        .append(")");
    
    PreparedStatement statement = currentConnection.prepareStatement(queryToUpdateDuplicateStatus);
    statement.executeUpdate();
  }
  
  /** Get all duplicate assets in baseentity table and return iids
   * @param organizationId
   * @return
   * @throws SQLException
   * @throws RDBMSException
   */
  public IResultSetParser detectAllDuplicateAssets(String organizationId) throws SQLException, RDBMSException {
    organizationId = organizationId.equals("-1") ? IStandardConfig.STANDARD_ORGANIZATION_RCODE : organizationId;
    StringBuilder query = new StringBuilder("SELECT baseentityiid FROM pxp.baseentity a WHERE (SELECT COUNT(hashcode) FROM pxp.baseentity b WHERE a.hashcode = b.hashcode and b.ismerged != true) > 1")
        .append(" and organizationcode = '").append(organizationId).append("'");
    PreparedStatement ps = currentConnection.prepareStatement(query);
    return driver.getResultSetParser(ps.executeQuery());
  }
  
  /**
   * Handle already deleted duplicate asset by setting isDuplicate = false
   * @param baseEntityIIds
   * @throws RDBMSException
   * @throws SQLException
   */
  public void handleDeletedDuplicateAssets(Set<Long> baseEntityIIds) throws RDBMSException, SQLException {
    StringBuilder queryToUpdateDuplicateStatus = new StringBuilder("Update pxp.baseentity set isduplicate = false where baseentityiid not in (")
        .append(Text.join(",", baseEntityIIds))
        .append(")");
    PreparedStatement statement = currentConnection.prepareStatement(queryToUpdateDuplicateStatus);
    statement.executeUpdate();
  }

  /**
   * Returns Duplicate Asset Instance Ids list
   */
  public IResultSetParser getAllDuplicateAssets(int size, String physicalCatalogId,
      String organisationId, String endPointId, String sortOrder) throws SQLException, RDBMSException
  {
    int baseType = BaseType.ASSET.ordinal();
    StringBuilder queryForDuplicateBaseEntities = new StringBuilder("Select baseentityiid from ( ")
        .append(" Select Distinct on(hashcode) hashcode, lastmodifiedtime, baseentityiid ")
        .append(" from pxp.baseentity where isDuplicate = true and ismerged != true and catalogcode='")
        .append(physicalCatalogId)
        .append("' and organizationcode='")
        .append(organisationId)
        .append("' and basetype = ")
        .append(baseType)
        .append(" ORDER BY hashcode, lastmodifiedtime ")
        .append(sortOrder)
        .append(" ) entities limit ")
        .append(size);
    PreparedStatement statement = currentConnection.prepareStatement(queryForDuplicateBaseEntities);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  public Long getNewDefaultImage(Long entityIID) throws SQLException, RDBMSException {
    IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(SystemLevelIds.STANDARD_ARTICLE_ASSET_RELATIONSHIP_ID);
    IResultSetParser result = driver.getFunction(currentConnection,RDBMSAbstractFunction.ResultType.IID, "pxp.fn_getNewDefaultImage")
    .setInput(ParameterType.IID, entityIID)
    .setInput(ParameterType.IID, propertyByCode.getIID())
    .setInput(ParameterType.INT, IBaseEntityIDDTO.BaseType.ASSET.ordinal())
    .execute();
    return result.getIID();
  }

  /**
   * Returns the baseentityiids of TIVs according to passed parentIId and classifierCode.
   */
  public IResultSetParser getIIdsByParentIIdAndClassifierCode(long parentIId, String classifierCode)
      throws RDBMSException, SQLException
  {
    StringBuilder query = new StringBuilder("Select distinct be.baseentityiid from pxp.baseentity be, "
            + "pxp.classifierconfig cc where be.classifieriid = cc.classifieriid and be.parentiid = ")
                .append(parentIId)
                .append(" and cc.classifiercode = '")
                .append(classifierCode)
                .append("'");
    
    PreparedStatement statement = currentConnection.prepareStatement(query);
    return driver.getResultSetParser(statement.executeQuery());
  }

  public List<IValueRecordDTO> getAllAttributeVariantsValueIIDSByPropertyIID(String attributeId, List<Long> contextIds, BaseEntityDAO baseEntityDAO) throws RDBMSException {
    List<IValueRecordDTO> toBeDeletedValueRecords = new ArrayList<IValueRecordDTO>();
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByCode(attributeId);
    Long propertyIId = propertyDTO.getPropertyIID();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currConnection) -> { 
        StringBuilder query = new StringBuilder(Q_GET_ATTRIBUTE_VARIANT_IIDS_BY_PROPERTY_IIDS).append("vr.propertyiid = ").append(propertyIId).
        append(" and vr.entityiid = ").append(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID()).append(" and co.contextcode in ('").append(Text.join("','", contextIds)).append("')");
    PreparedStatement statement = currConnection.prepareStatement(query.toString());
    IResultSetParser resultSet = driver.getResultSetParser(statement.executeQuery());
    while (resultSet.next()) {
      IValueRecordDTO newValueRecordDTO = baseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "").build();
      ((ValueRecordDTO)newValueRecordDTO).setValueIID(resultSet.getLong("valueiid"));
      ((ValueRecordDTO)newValueRecordDTO).setContextualData(new ContextualDataDTO(resultSet.getLong("contextualObjectIID"), resultSet.getString("contextcode"), 0L, 0L));
      toBeDeletedValueRecords.add(newValueRecordDTO);
      }
    });
    return toBeDeletedValueRecords;
  }
  
  public void moveToArchive(long entityIID, String pxon, String assetObjectKey)
      throws SQLException, RDBMSException, CSFormatException {
    
    byte[] archivedContent = TextArchive.zip(pxon);
    
    StringBuffer query = new StringBuffer(Q_INSERT_INTO_BASE_ENTITY_ARCHIVE);
    PreparedStatement ps = currentConnection.prepareStatement(query.toString());
    ps.setLong(1, entityIID);
    ps.setBytes(2, archivedContent);
    ps.setString(3, assetObjectKey.isEmpty() ? null : assetObjectKey );
    ps.executeUpdate();

 } 
  
  /**
   * Insert entry into assettobepurged table for purging.
   * @param baseEntityIID
   * @param type
   * @param assetObjectKey
   * @param thumbKey
   * @param previewImageKey
   * @throws SQLException
   * @throws RDBMSException
   */
  public void moveAssetToPurge(long baseEntityIID, String type, String assetObjectKey, String thumbKey, String previewImageKey)
      throws SQLException, RDBMSException
  {
    PreparedStatement ps = currentConnection.prepareStatement(Q_INSERT_INTO_PURGED_ASSET);
    ps.setString(1, assetObjectKey);
    ps.setString(2, thumbKey.isEmpty() ? null : thumbKey);
    ps.setString(3, previewImageKey.isEmpty() ? null : previewImageKey);
    ps.setString(4, type);
    ps.executeUpdate();
  }
  
  /**
   * Get auto created TIV details  for purging.
   * @param iid
   * @param contextIIDsString
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public IResultSetParser prepareAssetPurgeDTOListForVariants(Long iid, String contextIIDsString) throws RDBMSException, SQLException
  {
    StringBuilder getAllTechnicalVariantsQuery = new StringBuilder("Select baseentityiid, entityextension ->>'type' container, ")
        .append("entityextension ->>'thumbKey' thumbKey, ")
        .append("entityextension ->>'assetObjectKey' assetObjectKey, ")
        .append("entityextension ->>'previewImageKey' previewImageKey ")
        .append("From pxp.baseentity ")
        .append("JOIN pxp.contextualobject USING(contextualobjectiid) ")
        .append("Where ")
        .append("contextualobjectiid IN ( ")
        .append(contextIIDsString)
        .append(") AND parentiid = ")
        .append(iid)
        .append(" AND entityextension IS NOT NULL");
    PreparedStatement stmt = currentConnection.prepareStatement(getAllTechnicalVariantsQuery.toString());
    return driver.getResultSetParser(stmt.executeQuery());
  }
  
  /**
   * Get Target entity using Origin base entity
   * @param originBaseEntity
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  public long getTargetEntityFromOriginBaseEntity(Long originBaseEntity, String catalogcode) throws RDBMSException, SQLException
  {
    Long target = 0L;
    PreparedStatement cs = currentConnection.prepareStatement(Q_GET_DEST_ENTITY_USING_ORIGIN_BASE_ENTITY_IID);
    cs.setLong(1, originBaseEntity);
    cs.setString(2, catalogcode);
    IResultSetParser resultSet = driver.getResultSetParser(cs.executeQuery());
    if (resultSet.next()) {
      target = resultSet.getLong(1);
    }
    return target;
  }
}
