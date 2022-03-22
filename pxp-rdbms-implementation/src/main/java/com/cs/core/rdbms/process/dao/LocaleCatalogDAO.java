package com.cs.core.rdbms.process.dao;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.constants.SystemLevelIds;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.coupling.CSECoupling;
import com.cs.core.csexpress.scope.CSEScope;
import com.cs.core.data.Text;
import com.cs.core.data.TextArchive;
import com.cs.core.elastic.dao.GoldenRecordBucketSearchDAO;
import com.cs.core.elastic.dao.SearchDAO;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.collection.dao.CollectionDAO;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.*;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.coupling.dao.CouplingDAO;
import com.cs.core.rdbms.coupling.dto.CouplingDTO.CouplingDTOBuilder;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTOBuilder;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAS;
import com.cs.core.rdbms.entity.dao.EntityEventDAS;
import com.cs.core.rdbms.entity.dto.*;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO.BaseEntityDTOBuilder;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO.BaseEntityIDDTOBuilder;
import com.cs.core.rdbms.entity.dto.CollectionDTO.CollectionDTOBuilder;
import com.cs.core.rdbms.entity.dto.TaxonomyInheritanceDTO.TaxonomyInheritanceDTOBuilder;
import com.cs.core.rdbms.entity.dto.UniquenessViolationDTO.UniquenessViolationBuilder;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.dto.RuleResultDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.rdbms.relationship.dao.RelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.rsearch.idto.IValueCountDTO;
import com.cs.core.rdbms.services.records.RecordDASFactory;
import com.cs.core.rdbms.services.records.RelationsSetDAS;
import com.cs.core.rdbms.services.records.TagsRecordDAS;
import com.cs.core.rdbms.services.records.ValueRecordDAS;
import com.cs.core.rdbms.services.resolvers.SearchRenderer;
import com.cs.core.rdbms.services.resolvers.SearchResolver;
import com.cs.core.rdbms.task.dao.TaskRecordDAO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.taxonomyinheritance.dao.TaxonomyInheritanceDAO;
import com.cs.core.rdbms.taxonomyinheritance.dao.TaxonomyInheritanceDAS;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.rdbms.uniquenessViolation.dao.UniquenessViolationDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dao.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents data access from and within a PXP locale catalog
 *
 * @author vallee
 */
public class LocaleCatalogDAO implements ILocaleCatalogDAO {

  private static final String     Q_ALL_RECORDS              = "select * from pxp.AllRecordWithStatus where entityIID = ? "
      + "and recordStatus <> " + IPropertyRecordDTO.RecordStatus.NOTIFIED.ordinal();
  private final IConfigurationDAO configDAO                  = RDBMSAppDriverManager.getDriver().newConfigurationDAO();
  private final UserSessionDTO    userSessionDTO;
  private final LocaleCatalogDTO  localeCatalog;
  private static final String     GET_ALL_LOCALE_IDS         = "select localeid from pxp.baseentitylocaleidlink where baseentityiid = ";  
  private static final String     Q_ALL_PROPERTIES_BY_LOCALE = "select * from pxp.AllValueRecord where entityiid = ? and localeid = ?";
  private static final String     Q_GET_ALL_VALUE_RECORDS    = "select * from pxp.AllValueRecord where propertyiid = ? and entityiid = ?";
  
  private static final String     Q_GET_ENTITY_ID_BY_IID      = "select baseentityid from pxp.baseentity where baseentityiid = ? and catalogcode = ?";
  
  private static final String     Q_GET_ENTITY_IID_BY_ID     = "select baseentityiid from pxp.baseentity where baseentityid = ? and catalogcode = ? and organizationcode = ?";
  
  private static final String     Q_DI_GET_ENTITY_IID_BY_ID    = "select baseentityiid from pxp.baseentity where baseentityid = ? and catalogcode = ? and organizationcode = ? and endpointcode = ?";

  private static final String     Q_GET_ENTITY_FROM_ARCHIVE = " select * from pxp.baseentityarchive where entityiid in (%s)";
  private static final String     Q_DELETE_ENTITY_FROM_ARCHIVE = " Delete from pxp.baseentityarchive where entityiid in (%s)";
  private static final String     Q_DELETE_ALL_REVISIONS       = "Delete from pxp.objectrevision where objectiid in (%s) ";
  private static final String     Q_GET_ENTITY_VS_OTHER_CLASSIFIER_CODES = "SELECT becl.baseentityiid , cc.classifiercode, cc.classifiertype FROM pxp.baseentityclassifierlink AS becl JOIN pxp.classifierconfig AS cc "
      + "ON becl.otherclassifieriid = cc.classifieriid WHERE becl.baseentityiid IN ( %s )";

  private static final String     Q_ENTITY_VS_OTHER_CLASSIFIER = "SELECT cc.classifierIID, cc.classifiercode, cc.classifiertype FROM pxp.baseentityclassifierlink AS becl JOIN pxp.classifierconfig AS cc "
      + "ON becl.otherclassifieriid = cc.classifieriid WHERE becl.baseentityiid = ( %s )";
  private static final String     Q_GET_BASE_ENTITY_BY_IID               = "select * from pxp.baseentity where baseentityiid = ?";
  private static final String     Q_GET_BASE_ENTITYS_BY_IIDS               = "select * from pxp.baseentity where baseentityiid in(%s)";

  private static final String     Q_GET_BASE_ENTITY_BY_ID               = "select * from pxp.baseentity where baseentityid = ? and catalogcode = ? and organizationcode = ? ";
  
  private static final String     Q_GET_OTHER_SIDE_IIDS        = "SELECT side1entityiid,side2entityiid from pxp.relation where side1entityiid = ( %s ) or  side2entityiid = ( %s )";
  private static final String     Q_GET_ENTITY_EXTENSION_BY_IIDS  = "select baseentityiid, entityextension from pxp.baseentity where baseentityiid in (%s)"
      + "and basetype = " + BaseType.ASSET.ordinal();
  
  /**
   * Create the DAO from user session and locale catalog information
   *
   * @param pUserSessionDTO
   * @param pLocalCatalog
   */
  public LocaleCatalogDAO(IUserSessionDTO pUserSessionDTO, ILocaleCatalogDTO pLocalCatalog)
  {
    userSessionDTO = (UserSessionDTO) pUserSessionDTO;
    localeCatalog = (LocaleCatalogDTO) pLocalCatalog;
  }

  /**
   * @return the default root locale catalog (considered as global)
   */
  public static ILocaleCatalogDTO getDefaultRootLocaleCatalog()
  {
    return new LocaleCatalogDTO("", IStandardConfig.StandardCatalog.pim.toString(), IStandardConfig.STANDARD_ORGANIZATION_CODE);
  }

  @Override public ILocaleCatalogDTO getLocaleCatalogDTO()
  {
    return localeCatalog;
  }

  @Override
  public IUserSessionDTO getUserSessionDTO()
  {
    return userSessionDTO;
  }

  @SuppressWarnings("rawtypes") @Override
  public IBaseEntityIDDTOBuilder newBaseEntityIDDTOBuilder(String baseEntityID, BaseType baseType, IClassifierDTO natureClass)
  {
    IBaseEntityIDDTOBuilder entityDTO = new BaseEntityIDDTOBuilder(baseEntityID, baseType, localeCatalog.getLocaleID(), localeCatalog,
        (ClassifierDTO) natureClass);
    return entityDTO;
  }

  @Override public IBaseEntityDTOBuilder newBaseEntityDTOBuilder(String baseEntityID, BaseType baseType, IClassifierDTO natureClass)
  {

    return new BaseEntityDTOBuilder(baseEntityID, baseType, localeCatalog.getLocaleID(),
        new CatalogDTO(localeCatalog.getCatalogCode(), localeCatalog.getOrganizationCode()), (ClassifierDTO) natureClass);
  }

  @Override public IPropertyDTO newPropertyDTO(long propertyIID, String propertyCode, IPropertyDTO.PropertyType propertyType)
      throws RDBMSException
  {
    return configDAO.createProperty(propertyCode, propertyType);
  }

  /**
   * @param propertyIID
   * @return the property DTO
   * @throws RDBMSException
   */
  public IPropertyDTO getPropertyByIID(long propertyIID) throws RDBMSException
  {
    return configDAO.getPropertyByIID(propertyIID);
  }

  @Override public IClassifierDTO newClassifierDTO(long classifierIID, String classifierCode, IClassifierDTO.ClassifierType classType)
      throws RDBMSException
  {
    return configDAO.createClassifier(classifierCode, classType);
  }

  /**
   * @param classifierIID
   * @return the classifier DTO
   * @throws RDBMSException
   */
  public IClassifierDTO getClassifierByIID(long classifierIID) throws RDBMSException
  {
    return configDAO.getClassifierByIID(classifierIID);
  }

  @Override public IContextDTO newContextDTO(String contextCode, IContextDTO.ContextType type) throws RDBMSException
  {
    return configDAO.createContext(contextCode, type);
  }

  @Override
  public ICollectionDTOBuilder newCollectionDTOBuilder(CollectionType collectionType, String collectionCode, String organizationCode)
      throws RDBMSException, CSFormatException
  {
    ICollectionDTOBuilder collectionDTO = new CollectionDTOBuilder(collectionType, collectionCode, localeCatalog.getCatalogCode(),organizationCode);
    return collectionDTO;
  }

  @Override public ITaskDTO newTaskDTO(String taskCode, TaskType taskType) throws RDBMSException
  {
    return configDAO.createTask(taskCode, taskType);
  }

  @Override public IBaseEntityDAO openBaseEntity(IBaseEntityIDDTO entity) throws RDBMSException
  {
    // Check the entity is aligned with current locale catalog DTO
    LocaleCatalogDTO entityLocaleCatalog = (LocaleCatalogDTO) entity.getLocaleCatalog();
    if (!entityLocaleCatalog.getCatalogCode().equals(this.localeCatalog.getCatalogCode())) {
      throw new RDBMSException(4000, "Consistency",
          "This locale catalog doesn't match entity from " + entityLocaleCatalog.getCatalogCode() + " <> " + this.localeCatalog.getCatalogCode());
    }

    return new BaseEntityDAO(this, userSessionDTO, entity);
  }

  @Override public IBaseEntityDAO openAnyBaseEntity(IBaseEntityIDDTO entity) throws RDBMSException
  {
    // Check the entity is aligned with current locale catalog DTO
    LocaleCatalogDTO entityLocaleCatalog = (LocaleCatalogDTO) entity.getLocaleCatalog();
    if (!entityLocaleCatalog.getCatalogCode().equals(this.localeCatalog.getCatalogCode())) {
      // open the catalog corresponding to the catalog code in current locale ID
      LocaleCatalogDAO entityCatalog = new LocaleCatalogDAO(this.userSessionDTO,
          new LocaleCatalogDTO(entity.getBaseLocaleID(), entity.getLocaleCatalog().getCatalogCode(),
              entity.getLocaleCatalog().getOrganizationCode()));
      return new BaseEntityDAO(entityCatalog, userSessionDTO, entity);
    }
    return new BaseEntityDAO(this, userSessionDTO, entity);
  }

  @Override public ICollectionDAO openCollection() throws RDBMSException
  {
    return new CollectionDAO(userSessionDTO.getUserIID(), localeCatalog.getCatalogCode());
  }

  @Override public ITaskRecordDAO openTaskDAO()
  {
    return new TaskRecordDAO(userSessionDTO.getUserIID());
  }

  @Override public IRuleCatalogDAO openRuleDAO()
  {
    return new RuleCatalogDAO(this);
  }

  @Override public void applyLocaleInheritanceSchema(List<String> localeInheritance)
  {
    localeCatalog.setLocaleInheritanceSchema(localeInheritance);
  }

  /**
   * Access to the current locale inheritance schema for retrieving data
   *
   * @return the current locale inheritance including the locale currently considered
   */
  public List<String> getLocaleInheritanceSchema()
  {
    return localeCatalog.getLocaleInheritanceSchema(); // if consistent then returns as declared
  }

  @Override public IBaseEntityDTO getEntityByIID(long objectIID) throws RDBMSException
  {
    // Base Entity DTO to return
    BaseEntityDTO entity = new BaseEntityDTO();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      AllEntitiesSelectorDAS entitySelectorDas = new AllEntitiesSelectorDAS(currentConn, localeCatalog,
          localeCatalog.getLocaleInheritanceSchema(), IBaseEntityIDDTO.BaseType.UNDEFINED.ordinal());
      IResultSetParser rs = entitySelectorDas.queryEntityByIID(objectIID);
      if (rs.next()) {
        entity.mapFromBaseEntityTrackingWithName(rs);
        CatalogDTO catalog = new CatalogDTO(rs);
        entity.setLocaleCatalog(new LocaleCatalogDTO(rs.getString("baselocaleID"), catalog));
      }
    });
    this.loadLocaleIds(entity);
  RDBMSLogger.instance().info("getEntityBYIID");
  return entity.isNull() ? null : entity;
  }


  @Override
  public IBaseEntityDTO getBaseEntityDTOByIID(long objectIID) throws RDBMSException
  {
    // Base Entity DTO to return
    BaseEntityDTO entity = new BaseEntityDTO();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement prepareStatement = currentConn.prepareStatement(Q_GET_BASE_ENTITY_BY_IID);
      prepareStatement.setLong(1, objectIID);
      IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());
      if (result.next()) {
        entity.mapFromBaseEntityForImport(result);
      }
    });
    return entity.isNull() ? null : entity;
  }
  
  @Override
  public List<IBaseEntityDTO> getBaseEntityDTOsByIIDs(Set<Long> objectIIDs) throws RDBMSException
  {
    List<IBaseEntityDTO> baseEntityDTOs = new ArrayList<>();
    if(objectIIDs.isEmpty()) {
      return baseEntityDTOs;
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        String query = String.format(Q_GET_BASE_ENTITYS_BY_IIDS, Text.join(",", objectIIDs));
        PreparedStatement prepareStatement = currentConn.prepareStatement(query);
        IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());
        while (result.next()) {
          BaseEntityDTO entity = new BaseEntityDTO();
          entity.mapFromBaseEntityForImport(result);
          baseEntityDTOs.add(entity);
        }
    });
    return baseEntityDTOs;
  }
  
  @Override
  public IBaseEntityDTO getBaseEntityDTOByID(String objectID) throws RDBMSException
  {
    // Base Entity DTO to return
    BaseEntityDTO entity = new BaseEntityDTO();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement prepareStatement = currentConn.prepareStatement(Q_GET_BASE_ENTITY_BY_ID);
      prepareStatement.setString(1, objectID);
      prepareStatement.setString(2, localeCatalog.getCatalogCode());
      prepareStatement.setString(3, localeCatalog.getOrganizationCode());
      
      IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());
      if (result.next()) {
        entity.mapFromBaseEntityForImport(result);
      }
    });
    
    return entity.isNull() ? null : entity;
  }
  
  @Override public IBaseEntityDTO loadLocaleIds(IBaseEntityDTO baseEntityDTO) throws RDBMSException
  {
    List<String> localeIds = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(GET_ALL_LOCALE_IDS + baseEntityDTO.getBaseEntityIID());
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      while (result.next()) {
        localeIds.add(result.getString("localeid"));
      }
    });
    baseEntityDTO.setLocaleIds(localeIds);
    return baseEntityDTO;
  }

  @Override
  public List<IBaseEntityDTO> getAllEntitiesByIIDList(String objectIIDList, String technicalVariantIIdList, boolean shouldFetchMasterAssets)
      throws RDBMSException
  {

    List<IBaseEntityDTO> entityList = new ArrayList<IBaseEntityDTO>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      AllEntitiesSelectorDAS entitySelectorDas = new AllEntitiesSelectorDAS(currentConn, localeCatalog,
          localeCatalog.getLocaleInheritanceSchema(), IBaseEntityIDDTO.BaseType.UNDEFINED.ordinal());
      IResultSetParser rs = entitySelectorDas.queryEntitiesByIIDList(objectIIDList, technicalVariantIIdList, shouldFetchMasterAssets);
      while (rs.next()) {
        IBaseEntityDTO entity = new BaseEntityDTO();
        ((BaseEntityDTO) entity).mapFromBaseEntityTrackingWithName(rs);
        entityList.add(entity);
      }
    });
    return entityList;
  }

  @Override
  public String getEntityIdByIID(long entityIID) throws RDBMSException
  {
    final String[] entityID = new String[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement prepareStatement = currentConn.prepareStatement(Q_GET_ENTITY_ID_BY_IID);
      prepareStatement.setLong(1, entityIID);
      prepareStatement.setString(2, localeCatalog.getCatalogCode());
      IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());

      if (result.next()) {
        entityID[0] = result.getString("baseentityid");
      }
    });
    return entityID[0];
  }

  @Override
  public long getEntityIIDByID(String id, String endpointCode) throws RDBMSException
  {
    final Long[] entityIID = new Long[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String queryStmt;
      if(localeCatalog.getCatalogCode().equalsIgnoreCase("dataIntegration"))
        queryStmt = Q_DI_GET_ENTITY_IID_BY_ID;
      else
        queryStmt = Q_GET_ENTITY_IID_BY_ID;
      PreparedStatement prepareStatement = currentConn.prepareStatement(queryStmt);
      prepareStatement.setString(1, id);
      prepareStatement.setString(2, localeCatalog.getCatalogCode());
      prepareStatement.setString(3, localeCatalog.getOrganizationCode());
      if(localeCatalog.getCatalogCode().equalsIgnoreCase("dataIntegration"))
        prepareStatement.setString(4, endpointCode);
      IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());
      if (result.next()) {
        entityIID[0] = result.getLong("baseentityiid");
      }
      else {
        entityIID[0] = 0l;
      }
    });
    return entityIID[0];
  }

  @Override public IBaseEntityDTO getEntityByID(String objectID) throws RDBMSException
  {
    // Base Entity DTO to return
    BaseEntityDTO entity = new BaseEntityDTO();
    entity.setLocaleCatalog(localeCatalog);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      AllEntitiesSelectorDAS entitySelectorDas = new AllEntitiesSelectorDAS(currentConn, localeCatalog,
          localeCatalog.getLocaleInheritanceSchema(), IBaseEntityIDDTO.BaseType.UNDEFINED.ordinal());
      IResultSetParser rs = entitySelectorDas.queryEntityByID(objectID);
      if (rs.next()) {
        entity.mapFromBaseEntityTrackingWithName(rs);
      }
    });
    return entity.isNull() ? null : entity;
  }

  @Override public Collection<IPropertyDTO> getAllEntityProperties(long objectIID) throws RDBMSException
  {
    Set<IPropertyDTO> properties = new HashSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(Q_ALL_RECORDS);
      query.setLong(1, objectIID);
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      while (result.next()) {
        IPropertyDTO property = configDAO.getPropertyByIID(result.getLong("propertyIID"));
        int side = result.getInt("side");
        if (side > 0) {
          property.setRelationSide(IPropertyDTO.RelationSide.valueOf(side));
        }
        properties.add(property);
      }
    });
    return properties;
  }

  @Override public Set<IValueRecordDTO> getAllEntityPropertiesByLocaleId(long entityiid, String localeid) throws RDBMSException
  {
    Set<IValueRecordDTO> properties = new HashSet<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(Q_ALL_PROPERTIES_BY_LOCALE);
      query.setLong(1, entityiid);
      query.setString(2, localeid);
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      while (result.next()) {
        IPropertyDTO property = configDAO.getPropertyByIID(result.getLong("propertyIID"));
        properties.add(new ValueRecordDTO(result, property));
      }
    });
    return properties;
  }
  
  @Override
  public IBaseEntityDTO cloneEntityByIID(long originBaseEntityIID, Set<IClassifierDTO> classifiers,IPropertyDTO... properties)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    //cloning of entity and if variant present clone it too
    IBaseEntityDAO cloneBaseEntityDAO = createCloneOfEntity(originBaseEntityIID, classifiers, 0l, properties, null);
    return cloneBaseEntityDAO.getBaseEntityDTO();
  }

  private void createCloneOfVariant(List<Long> childrenVaraintIIDs, IBaseEntityDAO cloneBaseEntityDAO, long parentEntityId)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    if (parentEntityId != 0) {
      this.openBaseEntity(this.getEntityByIID(parentEntityId)).addChildren(EmbeddedType.CONTEXTUAL_CLASS,
          cloneBaseEntityDAO.getBaseEntityDTO());
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      for (long childVariant : childrenVaraintIIDs) {
        ContextualDataDTO cxtObject = new BaseEntityDAS(currentConn).loadContextualObject(childVariant);
        if (cxtObject.getContext().getContextType().equals(ContextType.EMBEDDED_VARIANT)) {
          IPropertyDTO[] propertiesToClone = this.getAllEntityProperties(childVariant).toArray(new IPropertyDTO[0]);
          try {
            this.createCloneOfEntity(childVariant, new HashSet<>(), cloneBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
                propertiesToClone,cxtObject);
          }
          catch (CSInitializationException e) {
            e.printStackTrace();
            RDBMSLogger.instance().exception(e);
          }
        }
      }
    });
  }

  private IBaseEntityDAO createCloneOfEntity(long originBaseEntityIID, Set<IClassifierDTO> classifiers, long topParentEntityId, IPropertyDTO[] properties, ContextualDataDTO cxtObject) throws RDBMSException, CSFormatException, CSInitializationException
  {
    IBaseEntityDTO originalBaseEntityDTO = this.getEntityByIID(originBaseEntityIID);
    IBaseEntityDAO originBaseEntityDAO = this.openBaseEntity(originalBaseEntityDTO);
    // load all property records -> required for determining the RecordStatus
    IBaseEntityDTO baseEntity = originBaseEntityDAO.loadPropertyRecords(properties);
    // force the locale ID to be the one obtained from the base entity in case of cloning
    String originBaseLocaleID = originBaseEntityDAO.getBaseEntityDTO().getBaseLocaleID();
    localeCatalog.setLocaleID(originBaseLocaleID);
    // generate new id on basis of originEntityId
    final String[] clonedEntityID = new String[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      clonedEntityID[0] = new BaseEntityDAS(currentConn).createClonedID(originBaseEntityIID);
    });
    
    
    List<Long> childrenVaraintIIDs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      List<Long> variantIids = Arrays.asList(ArrayUtils.toObject(new BaseEntityDAS( currentConn).getAllChildrens(originalBaseEntityDTO.getBaseEntityIID())));
      childrenVaraintIIDs.addAll(variantIids);
    });
    IBaseEntityDAO cloneBaseEntityDAO = this.openTargetBaseEntityDAO( clonedEntityID[0], originBaseEntityDAO, true, originalBaseEntityDTO.getEndpointCode());
    List<IPropertyDTO> updatedProperties =new ArrayList<>(Arrays.asList(properties));

    updatedProperties.removeIf(item -> item.getPropertyType().equals(PropertyType.NATURE_RELATIONSHIP)
        || item.getPropertyCode().equals(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID));
    
    cloneProperties(originBaseEntityDAO, cloneBaseEntityDAO, "Clone of ", cxtObject, updatedProperties.toArray(new IPropertyDTO[0]));
   
    Set<IClassifierDTO> updatedClassifiers = classifiers.isEmpty() ? new HashSet<IClassifierDTO>(originBaseEntityDAO.getClassifiers()) : classifiers;  
    
    // Golden record class can be applied
    // Only while creating golden record through match & merge section.
    removeGoldenArticleClassifier(updatedClassifiers);
    cloneBaseEntityDAO.addClassifiers(updatedClassifiers.toArray(new IClassifierDTO[0]));

    cloneBaseEntityDAO.loadPropertyRecords(properties);

    List<String> originLocaleIds = originBaseEntityDAO.getBaseEntityDTO().getLocaleIds();
    // cloneBaseEntity is already created with originBaseLocaleId. so avoiding it for creating translations.
    originLocaleIds.remove(originBaseLocaleID);
    cloneBaseEntityDAO.createLanguageTranslation(originLocaleIds);

    // add embedded children to parent entity
    createCloneOfVariant(childrenVaraintIIDs, cloneBaseEntityDAO, topParentEntityId);
    return cloneBaseEntityDAO;
  }

  @Override 
  public IBaseEntityDTO createManualLinkedVariant(long originBaseEntityIID, IClassifierDTO natureClassifier,
      Set<IClassifierDTO> classifiers, String linkedVariantEntityID, BaseType linkedVariantBaseType, IPropertyDTO... properties)
      throws RDBMSException, CSFormatException
  {
    IBaseEntityDAO originBaseEntityDAO = this.openBaseEntity(this.getEntityByIID(originBaseEntityIID));
    // load all property records -> required for determining the RecordStatus
    originBaseEntityDAO.loadPropertyRecords(properties);

    localeCatalog.setLocaleID(originBaseEntityDAO.getBaseEntityDTO().getBaseLocaleID());

    IBaseEntityDTO targetBaseEntityDTO = newBaseEntityDTOBuilder(linkedVariantEntityID, linkedVariantBaseType, natureClassifier).build();
    //    ((BaseEntityDTO) targetBaseEntityDTO).setOriginBaseEntityIID(originBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID());

    IBaseEntityDAO linkedVariantBaseEntity = this.openBaseEntity(targetBaseEntityDTO);
    // force the locale ID to be the one obtained from the base entity in case of cloning
    localeCatalog.setLocaleID(originBaseEntityDAO.getBaseEntityDTO().getBaseLocaleID());
    clonePropertiesForVariant(originBaseEntityDAO, linkedVariantBaseEntity, "Variant of ", properties);
    removeGoldenArticleClassifier(classifiers);
    linkedVariantBaseEntity.addClassifiers(classifiers.toArray(new ClassifierDTO[0]));
    List<String> localeIds = originBaseEntityDAO.getBaseEntityDTO().getLocaleIds();
    localeIds.remove(originBaseEntityDAO.getBaseEntityDTO().getBaseLocaleID());
    linkedVariantBaseEntity.createLanguageTranslation(localeIds);
    return linkedVariantBaseEntity.loadPropertyRecords(properties);
  }

  public void cloneProperties(IBaseEntityDAO originBaseEntityDAO, IBaseEntityDAO cloneBaseEntityDAO, String prefix,
    ContextualDataDTO cxtObject, IPropertyDTO... properties) throws RDBMSException, CSFormatException
  {
    Map<String, List<IPropertyRecordDTO>> originalRecords = getOriginPropertyRecords(originBaseEntityDAO, properties);
    boolean isDefaultImageApplicable = false;
    List<IPropertyRecordDTO> propertyRecords = new ArrayList<>();

    for (IPropertyDTO property : properties) {
      List<IPropertyRecordDTO> originPropertyRecords = originalRecords.get(property.getCode());
      if (originPropertyRecords == null) {
        RDBMSLogger.instance().warn("Cannot clone property IID: %d as not found in origin", property.getIID());
        continue;
      }
      for(IPropertyRecordDTO originPropertyRecord : originPropertyRecords) {
        
      if (!isDefaultImageApplicable && property.getPropertyType().equals(PropertyType.RELATIONSHIP) && property.getPropertyCode()
          .equals(IStandardConfig.StandardProperty.standardArticleAssetRelationship.name())) {
        isDefaultImageApplicable = true;
      }

      PropertyRecordDTO propertyRecord = (PropertyRecordDTO) createPropertyRecordDTO(originPropertyRecord, cloneBaseEntityDAO);

        if (originPropertyRecord.getProperty().getIID() == StandardProperty.nameattribute.getIID()) {
          // handling for name attribute
          String cloneName = ((IValueRecordDTO) originPropertyRecord).getValue();
          String existingLanguageCode = "";
          if (cloneName.contains("_") && cloneName.length() > 6) {
            existingLanguageCode = cloneName.substring(cloneName.substring(0, cloneName.lastIndexOf("_")).lastIndexOf("_") + 1);
          }
          if ((originBaseEntityDAO.getBaseEntityDTO().getLocaleIds()).contains(existingLanguageCode)) {
            ((IValueRecordDTO) propertyRecord).setValue(prefix + cloneName.replace(existingLanguageCode, localeCatalog.getLocaleID()));
          }
          else if (cxtObject != null && cxtObject.getContext().getContextType().equals(ContextType.EMBEDDED_VARIANT)) {
            ((IValueRecordDTO) propertyRecord).setValue(prefix + cloneName + "_context_" + localeCatalog.getLocaleID());
          }
          else {
            ((IValueRecordDTO) propertyRecord).setValue(prefix + cloneName + "_" + localeCatalog.getLocaleID());
          }
        }
      else if (originPropertyRecord.isCalculated()) {
        // add calculation if the record is a calculated attribute
        ((IValueRecordDTO) propertyRecord).addCalculation(((IValueRecordDTO) originPropertyRecord).getCalculation());

        } /*else if (originPropertyRecord.isCoupled()) {
          handleCoupledRecords((PropertyRecordDTO)originPropertyRecord, propertyRecord, originalRecords, property);
          }*/
      
      if (propertyRecord.getIID() != -1L) {
        propertyRecords.add(propertyRecord);
      }
    }
  }
    if (isDefaultImageApplicable) {
      cloneBaseEntityDAO.getBaseEntityDTO().setDefaultImageIID(originBaseEntityDAO.getBaseEntityDTO().getDefaultImageIID());
    }
    else {
      cloneBaseEntityDAO.getBaseEntityDTO().setDefaultImageIID(0L);
    }
    sortProperties(propertyRecords);
    cloneBaseEntityDAO.createPropertyRecords(propertyRecords.toArray(new IPropertyRecordDTO[0]));
  }

  private void sortProperties(List<IPropertyRecordDTO> propertyRecords)
  {
    Collections.sort(propertyRecords, Collections.reverseOrder((a, b) -> {
      IPropertyDTO firstProperty = a.getProperty();
      IPropertyDTO secondProperty = b.getProperty();
      return firstProperty.getSuperType().ordinal() - secondProperty.getSuperType().ordinal();
    }));
  }

  private Map<String, List<IPropertyRecordDTO>> getOriginPropertyRecords(IBaseEntityDAO originBaseEntityDAO,
      IPropertyDTO[] properties) throws RDBMSException
  {
    Map<String, List<IPropertyRecordDTO>> originPropertyRecords = new HashMap<>();
    long baseEntityIID = originBaseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    
    for(IPropertyDTO property : properties) {
      if(property.getSuperType().equals(IPropertyDTO.SuperType.ATTRIBUTE)) {
        RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement query = currentConn.prepareStatement(Q_GET_ALL_VALUE_RECORDS);
          query.setLong(1, property.getIID());
          query.setLong(2, baseEntityIID);
          IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
          List<IPropertyRecordDTO> propertyRecords = new ArrayList<>();
          while(result.next()) {
            ValueRecordDTO valueRecordDto = new ValueRecordDTO(result, property);
            long cxtObjectIID = result.getLong("contextualobjectIID");
            if (cxtObjectIID != 0l) {
              ContextualDataDTO cxtObject = new BaseEntityDAS(currentConn).loadContextualObject(cxtObjectIID);
              valueRecordDto.setContextualData(cxtObject);
            }
            propertyRecords.add(valueRecordDto);
          }
          if(propertyRecords.size() > 0)
              originPropertyRecords.put(property.getCode(),propertyRecords);
        });
      }
      else {
        IPropertyRecordDTO originPropertyRecord = originBaseEntityDAO.getBaseEntityDTO().getPropertyRecord(property.getIID());
          if (originPropertyRecord == null) {
            RDBMSLogger.instance().warn( "Cannot clone property IID: %d as not found in origin", property.getIID());
            continue;
          }
        originPropertyRecords.put(originPropertyRecord.getProperty().getCode(), Collections.singletonList(originPropertyRecord));
      }
    }
    return originPropertyRecords;
  }

  private void clonePropertiesForVariant(IBaseEntityDAO originBaseEntityDAO, IBaseEntityDAO cloneBaseEntityDAO, String prefix, IPropertyDTO... properties) throws RDBMSException, CSFormatException
  {
    Map<String, List<IPropertyRecordDTO>> originalRecords = getOriginPropertyRecords(originBaseEntityDAO, properties);
    
    List<IPropertyRecordDTO> propertyRecords = new ArrayList<>();
    for (IPropertyDTO property : properties) {
      if (property.getPropertyCode().equals(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID)) {
        continue;
      }
      List<IPropertyRecordDTO> originPropertyRecords = originalRecords.get(property.getCode());
      if (originPropertyRecords == null) {
        RDBMSLogger.instance().warn("Cannot clone property IID: %d as not found in origin", property.getIID());
        continue;
      }
      for(IPropertyRecordDTO originPropertyRecord : originPropertyRecords) {
        IPropertyRecordDTO propertyRecord = (PropertyRecordDTO) createPropertyRecordDTO(originPropertyRecord, cloneBaseEntityDAO);
        if (originPropertyRecord.getProperty().getIID() == StandardProperty.nameattribute.getIID()) {
          // handling for name attribute
          String clonedVariantName =((IValueRecordDTO) originPropertyRecord).getValue();
          String existingLanguageCode ="";
          if(clonedVariantName.contains("_") && clonedVariantName.length() > 6) {
            existingLanguageCode = clonedVariantName
                .substring(clonedVariantName.substring(0, clonedVariantName.lastIndexOf("_")).lastIndexOf("_") + 1);
          }
          if ((originBaseEntityDAO.getBaseEntityDTO().getLocaleIds()).contains(existingLanguageCode)) {
            ((IValueRecordDTO) propertyRecord)
            .setValue(prefix + clonedVariantName.replace(existingLanguageCode, localeCatalog.getLocaleID()));
          }
          else {
            ((IValueRecordDTO) propertyRecord).setValue(prefix + clonedVariantName + "_context_" + localeCatalog.getLocaleID());
          }
        }
        else if (originPropertyRecord.isCalculated()) {
          // add calculation if the record is a calculated attribute
          ((IValueRecordDTO) propertyRecord).addCalculation(((IValueRecordDTO) originPropertyRecord).getCalculation());
          
        }
        /* else if (originPropertyRecord.isCoupled()) {
        //handling for coupled records i.e if the record is coupled, inherit its coupling as it is
        ICSECoupling originCoupling = (new CSEParser()).parseCoupling(originPropertyRecord.getCouplingExpression());
        ((PropertyRecordDTO) propertyRecord).addCoupling((CSECoupling) originCoupling, true);
      }*/
        propertyRecords.add(propertyRecord);
      }
    }
    cloneBaseEntityDAO.createPropertyRecords(propertyRecords.toArray(new IPropertyRecordDTO[0]));
  }  
    /**
   * Initialize a new target base entity from a source one for cloning or transferring purpose
   *
   * @param clonedEntityID      the ID of the new target base entity
   * @param originBaseEntityDAO the DAO of the source entity
   * @param isCloned
   * @param endpointCode
   * @return the DAO of the target entity
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public IBaseEntityDAO openTargetBaseEntityDAO(
          String clonedEntityID, IBaseEntityDAO originBaseEntityDAO, boolean isCloned, String endpointCode) throws RDBMSException, CSFormatException {
    IBaseEntityDTO originBaseEntityDTO = originBaseEntityDAO.getBaseEntityDTO();

    IBaseEntityDTO targetBaseEntityDTO = newBaseEntityDTOBuilder(clonedEntityID, originBaseEntityDTO.getBaseType(),
        originBaseEntityDTO.getNatureClassifier()).endpointCode(endpointCode).isClone(isCloned).build();
    if(originBaseEntityDTO.getCatalog().getOrganizationCode() != targetBaseEntityDTO.getCatalog().getOrganizationCode()) {
      targetBaseEntityDTO.setSourceOrganizationCode(originBaseEntityDTO.getSourceOrganizationCode());
    }
    // originBaseEntityIID on  newly cloned entity
    ((BaseEntityDTO) targetBaseEntityDTO).setOriginBaseEntityIID(originBaseEntityDTO.getBaseEntityIID());
    //set other ClassifierIIDs
    targetBaseEntityDTO.setOtherClassifierIIDs(originBaseEntityDAO.getClassifiers().toArray(new IClassifierDTO[0]));
    targetBaseEntityDTO.setEntityExtension(originBaseEntityDTO.getEntityExtension().toString());
    // set contextual information
    ((ContextualDataDTO)targetBaseEntityDTO.getContextualObject()).setFrom( originBaseEntityDTO.getContextualObject());
    
    Set<IBaseEntityDTO> clonedContextualLinkedEntitites =  originBaseEntityDAO.getContextualLinkedEntities();
    List<Long> contextualLinkedEntitites =new ArrayList<>();
    for(IBaseEntityDTO linkedEntities: clonedContextualLinkedEntitites) {
      contextualLinkedEntitites.add(linkedEntities.getBaseEntityIID());
    }
    ((ContextualDataDTO)targetBaseEntityDTO.getContextualObject()).getLinkedBaseEntityIIDs().addAll(contextualLinkedEntitites);
    return this.openBaseEntity(targetBaseEntityDTO);
  }

  private IPropertyRecordDTO createPropertyRecordDTO(IPropertyRecordDTO originPropertyRecord, IBaseEntityDAO targetBaseEntityDAO)
      throws RDBMSException, CSFormatException
  {
    IPropertyDTO property = originPropertyRecord.getProperty();
    switch (property.getSuperType()) {
      case ATTRIBUTE:
        IValueRecordDTO originValueRecord = (IValueRecordDTO) originPropertyRecord;
        IContextualDataDTO contextualObject = originValueRecord.getContextualObject();

        IValueRecordDTOBuilder dtoBuilder = targetBaseEntityDAO.newValueRecordDTOBuilder(property, originValueRecord.getValue())
            .localeID(originValueRecord.getLocaleID())
            .asHTML(originValueRecord.getAsHTML())
            .asNumber(originValueRecord.getAsNumber())
            .unitSymbol(originValueRecord.getUnitSymbol());
        if (!contextualObject.isNull()) {
          IValueRecordDTO valueRecord = dtoBuilder.contextDTO(contextualObject.getContext()).build();
          IContextualDataDTO targetContextualObject = valueRecord.getContextualObject();
          targetContextualObject.setContextStartTime(contextualObject.getContextStartTime());
          targetContextualObject.setContextEndTime(contextualObject.getContextEndTime());
          targetContextualObject.setContextTagValues(contextualObject.getContextTagValues().toArray(new ITagDTO[0]));
          targetContextualObject.setLinkedBaseEntityIIDs(contextualObject.getLinkedBaseEntityIIDs().toArray(new Long[0]));
          return valueRecord;
        }
        return dtoBuilder.build();
      case RELATION_SIDE:
        IRelationsSetDTO originRelationRecord = (IRelationsSetDTO) originPropertyRecord;

        Set<IEntityRelationDTO> relations = originRelationRecord.getRelations();
        IEntityRelationDTO[] relationsSet = relations.toArray(new IEntityRelationDTO[relations.size()]);
        IRelationsSetDTO relation = targetBaseEntityDAO.newEntityRelationsSetDTOBuilder(property,
            originRelationRecord.getSide()).build();
        relation.addRelations(relationsSet);
        return relation;
      case TAGS:
        ITagsRecordDTO tagsRecordDTO = (ITagsRecordDTO) originPropertyRecord;
        ITagDTO[] tagValues = tagsRecordDTO.getTags().toArray(new ITagDTO[tagsRecordDTO.getTags().size()]);
        return targetBaseEntityDAO.newTagsRecordDTOBuilder(property).tags(tagValues).build();
      default:
        throw new RDBMSException(100, "Inconsistent Configuration",
            "Property super type should be one of attribute, tags or relation side");
    }
  }

  @Override public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntities(IBaseEntityIDDTO.BaseType baseType) throws RDBMSException
  {
    String baseTyping = String.format("$%s", baseType.name().toLowerCase());
    String catalog = localeCatalog.getCatalogCode();
    String locale = localeCatalog.getLocaleID();

    String searchExpression = String.format("select $basetype=%s $ctlg=%s $locale=%s", baseTyping, catalog, locale);
    CursorProvider cursorProvider = new CursorProvider(false, searchExpression);
    return new RDBMSCursor<>(cursorProvider);
  }

  @Override public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesByNameLike(BaseType baseType, String nameLike) throws RDBMSException
  {
    String baseTyping = String.format("$%s", baseType.name().toLowerCase());
    String catalog = localeCatalog.getCatalogCode();
    String locale = localeCatalog.getLocaleID();

    String searchExpression = String.format("select $basetype=%s $ctlg=%s $locale=%s where [nameattribute] like '%s' ", baseTyping, catalog,
        locale, "%" + nameLike + "%");
    CursorProvider cursorProvider = new CursorProvider(false, searchExpression);
    return new RDBMSCursor<>(cursorProvider);
  }

  @Override public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesByText(IBaseEntityIDDTO.BaseType baseType, String searchText)
      throws RDBMSException
  {
    String searchExpression = "select $basetype=$article $ctlg= pim|onboarding $locale=en_US where [any] = '%s'";
    CursorProvider cursorProvider = new CursorProvider(false, searchExpression);
    return new RDBMSCursor<>(cursorProvider);
  }

  @Override public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesBySearchExpression(String searchExpression) throws RDBMSException
  {
    return getAllEntitiesBySearchExpression(searchExpression, false);
  }

  @Override public Map<String, Long> getClassCount(Boolean allowChildren, String searchExpression, List<String> classifierIds)
      throws RDBMSException
  {
    Map<String, Long> count = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CSEParser parser = new CSEParser();
      ICSESearch search = parser.parseSearch(searchExpression);
      SearchRenderer searchRenderer = new SearchRenderer(currentConn, search);
      count.putAll(searchRenderer.getClassCount(allowChildren, classifierIds));
    });
    return count;
  }

  @Override public Map<String, Long> getTaxonomyCount(Boolean allowChildren, String searchExpression, List<String> classifierIds)
      throws RDBMSException
  {
    Map<String, Long> count = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CSEParser parser = new CSEParser();
      ICSESearch search = parser.parseSearch(searchExpression);
      SearchRenderer searchRenderer = new SearchRenderer(currentConn, search);
      count.putAll(searchRenderer.getTaxonomyCount(allowChildren, classifierIds));
    });
    return count;
  }

  @Override
  public List<IValueCountDTO> getPropertyCount(Boolean allowChildren, String searchExpression, Collection<IPropertyDTO> propertyCodes)
      throws RDBMSException
  {
    List<IValueCountDTO> count = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CSEParser parser = new CSEParser();
      ICSESearch search = parser.parseSearch(searchExpression);
      SearchRenderer searchRenderer = new SearchRenderer(currentConn, search);
      count.addAll(searchRenderer.getPropertyCount(allowChildren, propertyCodes, getLocaleInheritanceSchema()));
    });
    return count;
  }
  
  @Override
  public List<Long> getAllEntityIIdsBySearchExpression(String searchExpression) throws RDBMSException
  {
    List<Long> entityIIds = new ArrayList<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CSEParser parser = new CSEParser();
      ICSESearch search = parser.parseSearch(searchExpression);
      SearchResolver searchResolver = new SearchResolver(currentConn);
      entityIIds.addAll(searchResolver.getScopeResult((CSEScope) search.getScope()));
      currentConn.commit();
    });
    return entityIIds;
  }

  @Override public IRuleResultDTO getDataQualityCount(Boolean allowChildren, String searchExpression) throws RDBMSException
  {
    IRuleResultDTO[] ruleResultDTO = { new RuleResultDTO(RuleType.dataquality) };
    String localeID = localeCatalog.getLocaleID();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CSEParser parser = new CSEParser();
      ICSESearch search = parser.parseSearch(searchExpression);
      SearchRenderer searchRenderer = new SearchRenderer(currentConn, search);
      ruleResultDTO[0] = searchRenderer.getRuleResult(allowChildren, localeID);
    });
    return ruleResultDTO[0];

  }

  public void checkLocaleInheritance()
  {
    if (localeCatalog.getLocaleInheritanceSchema().isEmpty()) {
      localeCatalog.getLocaleInheritanceSchema().add(getLocaleCatalogDTO().getLocaleID());
    }
  }

  @Override
  public long getEntityIID(String entityID, String catalogCode, String organizationCode, String endpointCode) throws RDBMSException {
    long[] entityIID = new long[1];
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {

      //String endPointCondition = StringUtils.isEmpty(endpointCode) ? " is null" : "endPointCode = ? ";
      //String endPointQuery = "and endpointcode %s " + endPointCondition;

      PreparedStatement statement = currentConn.prepareStatement("select baseentityiid from pxp.baseentity where baseentityid = ? and catalogcode = ? and"
              + " organizationcode = ? and endpointcode " + (endpointCode.isEmpty() ? "is null": "= '"+ endpointCode + "'"));
      statement.setString(1, entityID);
      statement.setString(2, catalogCode);
      statement.setString(3, organizationCode);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        entityIID[0] = resultSet.getLong("baseentityiid");
      }
      else {
        entityIID[0] = -1; // not found
      }
    });
    return entityIID[0];
  }

  public boolean isEntityExisting(String entityID, String catalogCode, String organizationCode, String endpointCode) throws RDBMSException {
    return this.getEntityIID(entityID, catalogCode, organizationCode, endpointCode) != -1;
  }

  @Override public IRDBMSOrderedCursor<IBaseEntityDTO> getAllEntitiesBySearchExpression(String searchExpression, Boolean areChildrenAllowed)
      throws RDBMSException
  {
    CursorProvider cursorProvider = new CursorProvider(areChildrenAllowed, searchExpression);
    return new RDBMSCursor<>(cursorProvider);
  }

  @Override public IWorkflowStatusDAO openWorkflowStatusDAO()
  {
    return new WorkflowStatusDAO(userSessionDTO.getUserIID());
  }

  @Override
  public IUniquenessViolationDTOBuilder newUniquenessViolationBuilder(long sourceIID, long targetIID, long propertyIID, long classifierIID)
      throws RDBMSException
  {
    IUniquenessViolationDTOBuilder newUniquenessViolationDTO = new UniquenessViolationBuilder(sourceIID, targetIID, propertyIID,
        classifierIID);
    return newUniquenessViolationDTO;
  }

  @Override public IUniquenessViolationDAO openUniquenessDAO() throws RDBMSException
  {
    return new UniquenessViolationDAO();
  }

  @Override public List<Long> updateAssetExpiryStatus(long currentTimeStamp) throws RDBMSException
  {
    List<Long> updatedEntities = new ArrayList<Long>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
      IResultSetParser result = entityDAS.updateAssetExpiryStatus(currentTimeStamp);
      while (result.next()) {
        updatedEntities.add(result.getLong("baseentityiid"));

      }
    });
    return updatedEntities;
  }

  public static final String LINKED_INSTANCE_QUERY = "select a.side2entityIID, a.side1entityIID from pxp.relation a join pxp.propertyConfig b " + "on a.propertyIID = b.propertyIID and propertyCode in (%PROPERTY_CODES%) and side2entityIID in (%SIDE_INSTANCE_IDS%) "
      + "join pxp.baseentity be on a.side1entityIID = be.baseentityiid and be.ismerged != true ";

  @Override public Map<Long, Long> getLinkedVariantIds(Collection<IBaseEntityDTO> baseEntityDTOs, Collection<String> linkedRelationshipIds)
      throws RDBMSException
  {
    HashMap<Long, Long> baseEntityVSLinkedVariant = new HashMap<>();
    if(linkedRelationshipIds.isEmpty()){
        return baseEntityVSLinkedVariant;
    }
    Function<IBaseEntityDTO, String> getIID = x -> String.valueOf(x.getBaseEntityIID());

    String side2InstanceIds = Text.join(",", baseEntityDTOs, "'%s'", getIID);
    String propertyCodes = Text.join(",", linkedRelationshipIds, "'%s'");
    String finalQuery = LINKED_INSTANCE_QUERY.replace("%PROPERTY_CODES%", propertyCodes).replace("%SIDE_INSTANCE_IDS%", side2InstanceIds);

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(finalQuery);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        long side2entityIID = resultSet.getLong("side2entityIID");
        long side1entityIID = resultSet.getLong("side1entityIID");
        baseEntityVSLinkedVariant.put(side2entityIID, side1entityIID);
      }
    });
    return baseEntityVSLinkedVariant;
  }

  public List<Long> getOtherSideInstanceIIds(String side, String sourceEntityIId, Long relationPropertyIId) throws RDBMSException
  {
    String entityColumn = side.equals("side1") ? "side1entityIID" : "side2entityIID";
    String targetEntityColumn = side.equals("side1") ? "side2entityIID" : "side1entityIID";
    String query = "select re." + targetEntityColumn + " from pxp.relation re join pxp.baseentity be on re." + targetEntityColumn +" = be.baseentityiid and be.ismerged != true where re." + entityColumn + " = ? and propertyIID = ?";
    List<Long> otherSideIIds = new ArrayList<Long>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      PreparedStatement stmt = currentConn.prepareStatement(query);
      stmt.setLong(1, Long.parseLong(sourceEntityIId));
      stmt.setLong(2, relationPropertyIId);

      IResultSetParser objectResult = currentConn.getResultSetParser(stmt.executeQuery());
      while (objectResult.next()) {
        otherSideIIds.add(objectResult.getLong(targetEntityColumn));
      }
    });
    return otherSideIIds;
  }

  @Override public INotificationDAO openNotificationDAO()
  {
    return new NotificationDAO(userSessionDTO.getUserIID());
  }

  @Override
  public ITaxonomyInheritanceDTOBuilder newTaxonomyInheritanceDTOBuilder(long entityIID, long sourceEntityIID, long relationshipIID)
      throws RDBMSException, CSFormatException
  {
    ITaxonomyInheritanceDTOBuilder taxonomyInheritanceDTO = new TaxonomyInheritanceDTOBuilder(entityIID, sourceEntityIID, relationshipIID);
    return taxonomyInheritanceDTO;
  }

  @Override public ITaxonomyInheritanceDAO openTaxonomyInheritance(ITaxonomyInheritanceDTO entity) throws RDBMSException
  {
    return new TaxonomyInheritanceDAO(entity);
  }

  @Override public ITaxonomyInheritanceDTO getTaxonomyConflict(long entityIID) throws RDBMSException
  {
    // Base Entity DTO to return
    List<ITaxonomyInheritanceDTO> entity = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      TaxonomyInheritanceDAS entitySelectorDas = new TaxonomyInheritanceDAS(currentConn);
      IResultSetParser rs = entitySelectorDas.getTaxonomyInheritanceConflict(entityIID);
      if (rs.next()) {
        entity.add(new TaxonomyInheritanceDTO(rs));
      }
    });

    return entity.isEmpty() ? null : entity.get(0);
  }

  public static final String EXISTENCE_QUERY = "select baseEntityID from pxp.baseEntity where baseEntityID in (%s)";

  /**
   * @param baseEntityIDs: a list of base entities that need to be created
   * @return entities already exist
   * @throws RDBMSException
   */
  public static List<String> getExistingBaseEntityIDs(List<String> baseEntityIDs) throws RDBMSException
  {
    List<String> existingBaseEntities = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);

      String ids = Text.join(",", baseEntityIDs, "'%s'");
      PreparedStatement statement = currentConn.prepareStatement(String.format(EXISTENCE_QUERY, ids));
      statement.execute();
      IResultSetParser result = currentConn.getResultSetParser(statement.getResultSet());
      while (result.next()) {
        String baseEntityID = result.getString("baseEntityID");
        existingBaseEntities.add(baseEntityID);
      }
    });
    return existingBaseEntities;
  }


  @Override
  public IRelationCoupleRecordDAO openRelationCoupleRecordDAO()
  {
    return RelationCoupleRecordDAO.getInstance();
  }

  public static final String GET_CONTENT = "select entityIID from %s where entityIID in (%s) and propertyIID = %s limit 1";

  private void handleCoupledRecords(PropertyRecordDTO originPropertyRecord, PropertyRecordDTO propertyRecord,
      Map<String, List<IPropertyRecordDTO>> originalRecords, IPropertyDTO property) throws CSFormatException, RDBMSException
  {
    //handling for coupled records i.e if the record is coupled, inherit its coupling as it is
    ICSECoupling originCoupling = (new CSEParser()).parseCoupling(originPropertyRecord.getCouplingExpression());
    ICSECouplingSource source = originCoupling.getSource();
    boolean isRelation = source.isRelation();
    if (isRelation && !originalRecords.containsKey(source.toRelation().getCode())) {
      if (property instanceof IConflictingPropertyDTO) {

        IConflictingPropertyDTO conflictingProperty = (IConflictingPropertyDTO) property;
        if (conflictingProperty.getConflicts().isEmpty()) {
          propertyRecord.setIID(-1L);
        }
        CouplingBehavior behavior = conflictingProperty.getConflicts().get(0).getCouplingType().getBehavior();

        List<String> relationships = conflictingProperty.getConflicts()
            .stream()
            .map(x -> x.getConflictSourceCode())
            .collect(Collectors.toList());
        relationships.retainAll(originalRecords.keySet());

        if (relationships.isEmpty()) {
          propertyRecord.setIID(-1L);
        }
        else {
          //first entity available for coupling
          AtomicLong sourceEntity = new AtomicLong();
          for (String relationship : relationships) {

            IRelationsSetDTO relationDTO = (IRelationsSetDTO) originalRecords.get(relationship).get(0);
            Set<IEntityRelationDTO> relations = relationDTO.getRelations();
            List<Long> otherSideEntities = relations.stream().map(x -> x.getOtherSideEntityIID()).collect(Collectors.toList());

            String baseEntityIIDs = Text.join(",", otherSideEntities);
            String tableToQuery = property.getSuperType().equals(SuperType.ATTRIBUTE) ? "pxp.valuerecord" : "pxp.tagsrecord";
            String finalQuery = String.format(GET_CONTENT, tableToQuery, baseEntityIIDs, property.getIID());
            RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              PreparedStatement statement = currentConn.prepareStatement(finalQuery);
              ResultSet resultSet = statement.executeQuery();
              if (resultSet.next()) {
                sourceEntity.set(resultSet.getLong("entityIID"));
              }
            });
            if (!(sourceEntity.intValue() == 0)) {
              propertyRecord.addRelationshipCoupling(relationDTO.getProperty(),
                  relationDTO.getSide() == IPropertyDTO.RelationSide.SIDE_1 ? 2 : 1, property,
                  behavior.equals(CouplingBehavior.DYNAMIC));
              break;
            }
          }

        }
      }
    } 
    else {
      propertyRecord.addCoupling((CSECoupling) originCoupling, true);
    }
  }

  @Override
  public ICouplingDTOBuilder newCouplingDTOBuilder() throws RDBMSException
  {
    ICouplingDTOBuilder couplingDTOBuilder = new CouplingDTOBuilder();     
    return couplingDTOBuilder;
  }

  @Override
  public ICouplingDAO openCouplingDAO() throws RDBMSException
  {
    return new CouplingDAO(userSessionDTO.getUserIID(), localeCatalog.getCatalogCode());
  } 
  
  public void postUsecaseUpdate(long baseEntityIID, EventType event) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      IBaseEntityDTO entityByIID = getBaseEntityDTOByIID(baseEntityIID);
      IBaseEntityDAO baseEntity = openBaseEntity(entityByIID);
      baseEntity.loadAllPropertyRecords();
      EntityEventDAS eventDAS = new EntityEventDAS(currentConn, userSessionDTO, localeCatalog, baseEntity.getBaseEntityDTO());
      eventDAS.pushUpdateEntityEvent();
    });
  }

  public void postUsecaseUpdateWithLoadRecords(IBaseEntityDTO baseEntityDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      IBaseEntityDAO baseEntity = openBaseEntity(baseEntityDTO);
      baseEntity.loadAllPropertyRecords();
      EntityEventDAS eventDAS = new EntityEventDAS(currentConn, userSessionDTO, localeCatalog, baseEntity.getBaseEntityDTO());
      eventDAS.pushUpdateEntityEvent();
    });
  }

  public void postUsecaseUpdateWithEntity(IBaseEntityDTO baseEntityDTO, EventType event) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      EntityEventDAS eventDAS = new EntityEventDAS(currentConn, userSessionDTO, localeCatalog, baseEntityDTO);
      eventDAS.pushUpdateEntityEvent();
    });
  }

  @Override
  public void postUsecaseUpdate(IBaseEntityDTO baseEntityDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      this.fillAllProperties(List.of(baseEntityDTO));
      EntityEventDAS eventDAS = new EntityEventDAS(currentConn, userSessionDTO, localeCatalog, baseEntityDTO);
      eventDAS.pushUpdateEntityEvent();
    });
  }
  
  
  @Override
  public void postDeleteUpdate(IBaseEntityDTO baseEntity) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
     EntityEventDAS eventDAS = new EntityEventDAS(currentConn, userSessionDTO, localeCatalog, baseEntity);
      eventDAS.pushUpdateEntityEvent();
    });
  }

  public List<String> getDuplicateAssetInstances(int size, String physicalCatalogId, String organisationId, String endPointId,
      String sortOrder) throws RDBMSException
  {
    List<String> duplicateBaseEntityIids = new ArrayList<String>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS entityDAS = new BaseEntityDAS(currentConn);
      IResultSetParser result = entityDAS.getAllDuplicateAssets(size, physicalCatalogId, organisationId, endPointId, sortOrder);
      while (result.next()) {
        duplicateBaseEntityIids.add(Long.toString(result.getLong("baseentityiid")));
      }
    });
    return duplicateBaseEntityIids;
  }
  
  public List<Long> getArticlesWithGivenDefaultImage(List<String> assetIIDs) throws RDBMSException
  {
    List<Long> articleIIDs = new ArrayList<>();
    
    String query = "SELECT baseEntityIID FROM pxp.baseEntity WHERE defaultImageIID in ( "
        + Text.join(",", assetIIDs) + " ) and ismerged != true and baseType = "+ BaseType.ARTICLE.ordinal();
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement statement = currentConn.prepareStatement(query);
          ResultSet result = statement.executeQuery();
          while (result.next()) {
            articleIIDs.add(result.getLong("baseEntityIID"));
          }
        });
    return articleIIDs;
  }

  private static final String Q_GET_ENTITIES = "select e.*, v.value as baseEntityName " + "from pxp.BaseEntityTrackingFullContent e join pxp.valueRecord v on v.entityIID = e.baseEntityIID and v.propertyIID = 200 " + "and v.localeID = %s where e.baseEntityIID in (%s) ";

  private static final String Q_LOCALES_FOR_ENTITIES = "select baseEntityIID,array_agg(localeid) as localeids from pxp.baseEntityLocaleIdLink where baseEntityIID in (%s) group by baseEntityIID";

  @Override
  public List<IBaseEntityDTO> getBaseEntitiesByIIDs(List<String> baseEntityIIDs) throws RDBMSException
  {
    if (baseEntityIIDs.isEmpty()) {
      return new ArrayList<>();
    }
    // Base Entity DTO to return
    HashMap<String, IBaseEntityDTO> entitiesVsIID = new HashMap<>();
    Map<String,List<String>> entityIIdLocalesMap = new HashMap<>();
    List<String> baseEntitiesWithTranslation = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String languageTranslationQuery = String.format(Q_LOCALES_FOR_ENTITIES, Text.join(",", baseEntityIIDs));
      PreparedStatement statement = currentConn.prepareStatement(languageTranslationQuery);
      IResultSetParser objectResult = currentConn.getResultSetParser(statement.executeQuery());
      while (objectResult.next()) {
        String[] localeids = objectResult.getStringArray("localeids");
        List<String> locales = Arrays.asList(localeids);
        
        String baseEntityIId = objectResult.getString("baseEntityIID");
        if(locales.contains(localeCatalog.getLocaleID())) {
          baseEntitiesWithTranslation.add(baseEntityIId);
        }
        entityIIdLocalesMap.put(baseEntityIId,locales);
      }
      List<String> baseEntitiesWithoutTranslation = ListUtils.subtract(baseEntityIIDs, baseEntitiesWithTranslation);
      if (!baseEntitiesWithoutTranslation.isEmpty()) {
        fillEntities(entitiesVsIID, baseEntitiesWithoutTranslation, currentConn, "e.baseLocaleID");
      }
      if (!baseEntitiesWithTranslation.isEmpty()) {
        fillEntities(entitiesVsIID, baseEntitiesWithTranslation, currentConn, "'" + localeCatalog.getLocaleID() + "'");
      }
    });
    
    List<IBaseEntityDTO> entities = new ArrayList<>();
    for (String baseEntityIID : baseEntityIIDs) {
      IBaseEntityDTO entity = entitiesVsIID.get(baseEntityIID);
      if (entity != null) {
        entity.setLocaleIds(entityIIdLocalesMap.get(baseEntityIID));
        entities.add(entity);
      }
    }
    return entities;
  }

  private void fillEntities(HashMap<String, IBaseEntityDTO> entities, List<String> baseEntitiesWithTranslation, RDBMSConnection currentConn,
      String localeID) throws RDBMSException, SQLException, CSFormatException
  {
    String format = String.format(Q_GET_ENTITIES, localeID, Text.join(",", baseEntitiesWithTranslation));
    PreparedStatement stmt = currentConn.prepareStatement(format);
    IResultSetParser resultSetParser = currentConn.getResultSetParser(stmt.executeQuery());
    while (resultSetParser.next()) {
      BaseEntityDTO entity = new BaseEntityDTO();
      entity.mapFromBaseEntityTrackingWithName(resultSetParser);
      entities.put(String.valueOf(entity.getBaseEntityIID()), entity);
    }
  }

  @Override
  public ISearchDTOBuilder getSearchDTOBuilder(List<BaseType> typesToFilter, ISearchDTOBuilder.RootFilter root, Boolean isArchivePortal)
  {
    return new SearchDTO.SearchDTOBuilder(localeCatalog.getOrganizationCode(), localeCatalog.getCatalogCode(), localeCatalog.getLocaleID(),
        typesToFilter, root, isArchivePortal);
  }

  @Override
  public ISearchDAO openSearchDAO(ISearchDTO searchDTO)
  {
      return new SearchDAO(searchDTO);
  }
  
  @Override
  public ISearchDAO openGoldenRecordBucketSearchDAO(ISearchDTO searchDTO)
  {
      return new GoldenRecordBucketSearchDAO(searchDTO);
  }
  
  @Override
  public IEntityViolationDAO openEntityViolationDAO() throws RDBMSException
  {
    return new EntityViolationDAO();
  }

  private static final String GET_VIOLATION_WITH_LOCALES =
      "SELECT (CASE WHEN qualityflag = "+ ICSEPropertyQualityFlag.QualityFlag.$red.ordinal() +" THEN 'red'"
                  + " WHEN qualityflag = "+ ICSEPropertyQualityFlag.QualityFlag.$orange.ordinal()+" THEN 'orange'"
                  + " WHEN qualityflag = "+ ICSEPropertyQualityFlag.QualityFlag.$yellow.ordinal()+" THEN 'yellow'"
                  + " END) as color,"
               + " array_agg(DISTINCT CASE WHEN localeid = 'NAL' THEN 'LI' ELSE localeid END) as localeIds"
               + " FROM pxp.baseentityqualityrulelink WHERE baseentityiid = ? GROUP BY color";

  @Override
  public Map<String, List<String>> getViolationVSLocalesMap(Long baseEntityIID) throws RDBMSException
  {
    Map<String, List<String>> violationVSLocales = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement query = currentConn.prepareStatement(GET_VIOLATION_WITH_LOCALES);
      query.setLong(1, baseEntityIID);
      IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
      while(result.next()) {
        violationVSLocales.put(result.getString("color"), Arrays.asList(result.getStringArray("localeIds")));
      }
    });

    return violationVSLocales;
  }

  private static final String GET_ALL_BASE_ENTITES = "SELECT baseentityiid FROM pxp.baseentity where ismerged != true and baseType != "+ BaseType.UNDEFINED.ordinal();

  @Override
  public Set<Long> getAllBaseEntityIIDs() throws RDBMSException
  {
    Set<Long> baseEntityIIDs = new HashSet<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_ALL_BASE_ENTITES);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while(result.next()) {
        baseEntityIIDs.add(result.getLong("baseentityiid"));
      }
    });

    return baseEntityIIDs;
  }

  private final String GET_PARENT_TAXONOMYIDS = "select classifierCode from pxp.classifierconfig where classifieriid in " + "(select unnest(hierarchyiids) from pxp.classifierconfig where classifierCode in (%s))";

  public Set<String> getAllTaxonomyParents(Set<String> taxonomyIds) throws RDBMSException
  {
    if (taxonomyIds.isEmpty()) {
      return new HashSet<>();
    }
    Set<String> allTaxonomyIds = new HashSet<>();
    String query = String.format(GET_PARENT_TAXONOMYIDS, Text.join(",", taxonomyIds, "'%s'"));

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(query);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());

      while (result.next()) {
        String classifierCode = result.getString("classifierCode");
        allTaxonomyIds.add(classifierCode);
      }
    });
    return allTaxonomyIds;
  }

  @Override
  public Map<Long, List<IValueRecordDTO>> getAllValueRecords(Long... baseEntityIIDs) throws RDBMSException
  {
    Map<Long, List<IValueRecordDTO>> iidVSvalueRecords = new HashMap<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
          iidVSvalueRecords.putAll(ValueRecordDAS.getAllValueRecordsForEntities(currentConn, configDAO, Set.of(baseEntityIIDs)));
        });
    return iidVSvalueRecords;
    
    
  }
  
  private static final String            Q_CONTEXTUAL_OBJECT      = "select contextualObjectIID, contextCode, cxtTags,"
      + " lower(cxtTimeRange) as cxtStartTime, upper(cxtTimeRange) as cxtEndTime"
      + " from pxp.contextualObject where contextualObjectIID = ?";
  
  protected static ContextualDataDTO getContextualObjectByIID(
      RDBMSConnection currentConnection, long contextualObjectIID)
      throws SQLException, RDBMSException, CSFormatException
  {
    if (contextualObjectIID <= 0)
      return new ContextualDataDTO();
    PreparedStatement cxtObjectQuery = currentConnection.prepareStatement(Q_CONTEXTUAL_OBJECT);
    cxtObjectQuery.setLong(1, contextualObjectIID);
    IResultSetParser cxtObjectResult = currentConnection.getResultSetParser(cxtObjectQuery.executeQuery());
    if (cxtObjectResult.next()) {
      return new ContextualDataDTO(cxtObjectResult);
    }
    throw new RDBMSException(0, "Program ERROR",
        "contextual object IID not found: " + contextualObjectIID);
  }
  
  @Override
  public Map<Long, List<ITagsRecordDTO>> getAllTagsRecords(Long... baseEntityIIDs) throws RDBMSException
  {
    Map<Long, List<ITagsRecordDTO>> iidVStagsRecords = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      iidVStagsRecords.putAll(TagsRecordDAS.getAllTagRecordsForEntities(currentConn, configDAO, Set.of(baseEntityIIDs)));
        });
    
    return iidVStagsRecords;
  }

  @Override
  public Map<Long, Map<Long,IRelationsSetDTO>> getAllRelationRecords(Long... baseEntityIIDs) throws RDBMSException
  {
    Map<Long, Map<Long,IRelationsSetDTO>> records = new HashMap<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      records.putAll(RelationsSetDAS.loadAllRecords(currentConn, this, List.of(baseEntityIIDs)));
    });

    return records;
  }
  
  @Override
  public List<IBaseEntityDTO> getBaseEntitiesFromArchive(List<String> baseEntityIIDs) throws RDBMSException
  {
    HashMap<String, IBaseEntityDTO> entitiesVsIID = new HashMap<>();
    if (baseEntityIIDs.isEmpty()) {
      return new ArrayList<>();
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(Q_GET_ENTITY_FROM_ARCHIVE, Text.join(",", baseEntityIIDs));
      PreparedStatement statement = currentConn.prepareStatement(query);
      IResultSetParser objectResult = currentConn.getResultSetParser(statement.executeQuery());
      while (objectResult.next()) {
        String jsonContent = TextArchive.unzip(objectResult.getBinaryBlob("objectarchive")).trim();
        BaseEntityDTO entity = new BaseEntityDTO();
        entity.fromPXON(new JSONContentParser(jsonContent));
        entitiesVsIID.put(String.valueOf(entity.getBaseEntityIID()), entity);
      }
    });
    
    List<IBaseEntityDTO> entities = new ArrayList<>();
    for (String baseEntityIID : baseEntityIIDs) {
      if (entitiesVsIID.get(baseEntityIID) != null) {
        entities.add(entitiesVsIID.get(baseEntityIID));
      }
    }
    return entities;
  }
  
  private static final String GET_ALL_RECORDS = "SELECT * FROM %TABLE_NAME% WHERE entityiid IN (%s) ";
  
  @Override
  public Map<Long, List<IRelationsSetDTO>> getAllRelationshipRecords(Long... baseEntityIIDs)
      throws RDBMSException
  {
    Map<Long, List<IRelationsSetDTO>> iidVSRelationRecords = new HashMap<>();
    if(baseEntityIIDs.length == 0){
      return iidVSRelationRecords;
    }

    String query = String.format(GET_ALL_RECORDS.replace("%TABLE_NAME%", "pxp.allrelationside1"),
        Text.join(",", baseEntityIIDs));

    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(query);
          IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

          while (result.next()) {
            IPropertyDTO property = configDAO.getPropertyByIID(result.getLong("propertyiid"));
            IRelationsSetDTO relationRecordDTO = new RelationsSetDTO(RelationSide.SIDE_1, result,
                property);
            Long entityIID = result.getLong("entityiid");
            List<IRelationsSetDTO> relationsRecords = iidVSRelationRecords.get(entityIID);
            if (relationsRecords == null) {
              relationsRecords = new ArrayList<>();
              iidVSRelationRecords.put(entityIID, relationsRecords);
            }
            relationsRecords.add(relationRecordDTO);
          }
        });

    return iidVSRelationRecords;
  }

  private static final String GET_CHILDREN_IIDS = "SELECT baseentityiid FROM pxp.baseentity WHERE topparentiid = ?";

  @Override
  public List<Long> getAllChildrenIIDs(Long baseEntityIID) throws RDBMSException
  {
    List<Long> childrenIIDs = new ArrayList<>();
    RDBMSConnectionManager.instance() .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(GET_CHILDREN_IIDS);
          stmt.setLong(1, baseEntityIID);
          IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
          while (result.next()) {
            childrenIIDs.add(result.getLong("baseentityiid"));
          }
        });
    return childrenIIDs;
  }
  
  private static final String GET_CHILDREN_IIDS_FOR_ENTITIES = "SELECT baseentityiid,topparentiid FROM pxp.baseentity WHERE topparentiid in (%s)";

  @Override
  public Map<Long,List<Long>> getAllChildrenIIDsForEntities(List<Long> baseEntityIIDs) throws RDBMSException
  {
    Map<Long,List<Long>> iidVsChildrenIIDs = new HashMap<>();
    if(baseEntityIIDs.isEmpty()) {
      return iidVsChildrenIIDs;
    }
    RDBMSConnectionManager.instance() .runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(GET_CHILDREN_IIDS_FOR_ENTITIES, Text.join(",", baseEntityIIDs));
          PreparedStatement stmt = currentConn.prepareStatement(query);
          IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
          while (result.next()) {
            long topParentIid = result.getLong("topparentiid");
            long baseentityiid = result.getLong("baseentityiid");
            List<Long> childrenIids = iidVsChildrenIIDs.get(topParentIid);
            if(childrenIids == null) {
              childrenIids = new ArrayList<>();
              iidVsChildrenIIDs.put(topParentIid, childrenIids);
            }
            childrenIids.add(baseentityiid);
          }
        });
    return iidVsChildrenIIDs;
  }

  public static String LOAD_CONTEXTUAL_IIDS = "select be.baseentityiid from pxp.baseentity be " +
      "JOIN pxp.contextualobject co ON be.contextualobjectiid = co.contextualobjectiid " +
      "where be.parentiid = ? and co.contextcode IN (%s) and be.ismerged != true";

  @Override
  public List<Long> loadContextualEntityIIDs(List<String> contextualCodes, Long parentIID) throws RDBMSException
  {
    List<Long> contextualEntityIIDs = new ArrayList<>();

    RDBMSConnectionManager.instance() .runTransaction((RDBMSConnection currentConn) -> {

      String finalQuery = String.format(LOAD_CONTEXTUAL_IIDS, Text.join(",", contextualCodes, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      stmt.setLong(1, parentIID);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while (result.next()) {
        contextualEntityIIDs.add(result.getLong("baseentityiid"));
      }
    });
    return contextualEntityIIDs;
  }

  @Override
  public void deleteFromArchive(Long baseEntityIIDs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      // deleting the object revisions of baseentity
      String deleteRevisionsQuery = String.format(Q_DELETE_ALL_REVISIONS, baseEntityIIDs);
      PreparedStatement statement = currentConn.prepareStatement(deleteRevisionsQuery);
      statement.execute();
      
      deleteEntityFromArchive(baseEntityIIDs);
    });
    
  }
  
  
  @Override
  public void deleteEntityFromArchive(Long baseEntityIIDs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(Q_DELETE_ENTITY_FROM_ARCHIVE, baseEntityIIDs);
      PreparedStatement stmt = currentConn.prepareStatement(query);
      stmt.execute();
    });
    
  }
  
  @Override
  public Map<Long, Map<String,List<String>>> getEntityVSotherClassifierCodes(Long... baseEntityIIDs)
      throws RDBMSException
  {
    Map<Long, Map<String, List<String>>> iidVSclassifierCodes = new HashMap<>();
    List<Long> entitiesWithoutClassifiers = new ArrayList<>(Arrays.asList(baseEntityIIDs));
    
    String query = String.format(Q_GET_ENTITY_VS_OTHER_CLASSIFIER_CODES, Text.join(",", baseEntityIIDs));
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn)-> {
      PreparedStatement stmt = currentConn.prepareStatement(query);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
     
      while(result.next()) {
        
        Long iid = result.getLong("baseentityiid");
        entitiesWithoutClassifiers.remove(iid);
        
        Map<String, List<String>> classifierMapping = iidVSclassifierCodes.get(iid);
        
        if(classifierMapping == null) {
          classifierMapping = new HashMap<>();
          classifierMapping.put("klasses", new ArrayList<>());
          classifierMapping.put("taxonomies", new ArrayList<>());
          
          iidVSclassifierCodes.put(iid, classifierMapping);
        }
        
        if(result.getInt("classifiertype") == ClassifierType.CLASS.ordinal()) {
          classifierMapping.get("klasses").add(result.getString("classifiercode"));
        }
        else {
          classifierMapping.get("taxonomies").add(result.getString("classifiercode"));
        }
      }
      
      // filling empty map for those entities who didn't have any other classifiers.
      for(Long iid : entitiesWithoutClassifiers) {

          Map<String, List<String>> mapping = new HashMap<>();
          mapping.put("klasses", new ArrayList<>());
          mapping.put("taxonomies", new ArrayList<>());
          
          iidVSclassifierCodes.put(iid, mapping);
      }
      
    });
    
    return iidVSclassifierCodes;
    
  }

  @Override
  public List<IClassifierDTO> getOtherClassifiers(Long baseEntityIID)
      throws RDBMSException
  {
    List<IClassifierDTO> classifierDTOS = new ArrayList<>();
    String query = String.format(Q_ENTITY_VS_OTHER_CLASSIFIER, baseEntityIID);

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn)-> {
      PreparedStatement stmt = currentConn.prepareStatement(query);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while(result.next()) {
        Long classifierIID = result.getLong("classifierIID");
        String classifierCode = result.getString("classifierCode");
        ClassifierType classifierType = ClassifierType.valueOf(result.getInt("classifierType"));
        classifierDTOS.add(new ClassifierDTO(classifierIID, classifierCode, classifierType));
        }
    });

    return classifierDTOS;

  }
  
  public static String GET_BASEENTITY_NAMES = "select be.baseentityiid, vr.value from pxp.baseentity be JOIN pxp.valuerecord vr " + 
      "ON be.baseentityiid = vr.entityiid and vr.localeid  = ? " + 
      "and vr.propertyiid = ? and be.baseentityiid in (%s)";
  
  public static String GET_BASEENTITY_BASENAMES= "select be.baseentityiid, vr.value from pxp.baseentity be JOIN pxp.valuerecord vr " + 
      "ON be.baseentityiid = vr.entityiid and be.baselocaleid = vr.localeid  " + 
      "and vr.propertyiid = ? and be.baseentityiid in (%s)";

  @Override
  public Map<Long,String> getBaseEntityNamesByIID(Set<Long> baseEntityIIds) throws RDBMSException
  {
    Map<Long,String> baseEntityNames = new HashMap<>();
    if(baseEntityIIds.isEmpty()) {
      return baseEntityNames;
    }
    IPropertyDTO property = ConfigurationDAO.instance().getPropertyByCode(SystemLevelIds.NAME_ATTRIBUTE);
    RDBMSConnectionManager.instance() .runTransaction((RDBMSConnection currentConn) -> {

      String finalQuery = String.format(GET_BASEENTITY_NAMES, Text.join(",", baseEntityIIds, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      stmt.setString(1, localeCatalog.getLocaleID());
      stmt.setLong(2, property.getIID());
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while (result.next()) {
        long baseEntityIId = result.getLong("baseentityiid");
        baseEntityNames.put(baseEntityIId, result.getString("value"));
        baseEntityIIds.remove(baseEntityIId);
      }
      
      if(!baseEntityIIds.isEmpty()) {
        finalQuery = String.format(GET_BASEENTITY_BASENAMES, Text.join(",", baseEntityIIds, "'%s'"));
        PreparedStatement statement = currentConn.prepareStatement(finalQuery);
        statement.setLong(1, property.getIID());
        IResultSetParser resultSet = currentConn.getResultSetParser(statement.executeQuery());
        while (resultSet.next()) {
          long baseEntityIId = resultSet.getLong("baseentityiid");
          baseEntityNames.put(baseEntityIId, resultSet.getString("value"));
        }
      }
    });
    return baseEntityNames;
  }
  
  @Override
  public IBaseEntityDTO createGoldenRecordArticle(String creationLanguage, IClassifierDTO creationBaseType, BaseType baseType,
      String baseEntityId, String endpoint)
  {
    localeCatalog.setLocaleID(creationLanguage);
    IBaseEntityDTO targetBaseEntityDTO = newBaseEntityDTOBuilder(baseEntityId, baseType, creationBaseType).endpointCode(endpoint).build();
    return targetBaseEntityDTO;
  }
  
  private void removeGoldenArticleClassifier(Set<IClassifierDTO> classifiers)
  {
    classifiers.removeIf(classifier -> classifier.getClassifierCode()
        .equals(SystemLevelIds.GOLDEN_ARTICLE_KLASS));
  }
  
  public  Map<Long,Set<IPropertyRecordDTO>> getPropertyRecordsForEntities(Set<Long> entityIIds, IPropertyDTO... properties)
      throws RDBMSException, CSFormatException
  {
    List<IPropertyDTO> propertyDTOs = new ArrayList<>();
    for (IPropertyDTO property : properties) {
      if ( property.isNull() ) {
        property = newPropertyDTO(0, property.getPropertyCode(), property.getPropertyType());
      }
      propertyDTOs.add(property);
    }
    /*// load tracking properties when required
    for (IPropertyDTO property : properties) {
      if (property.isTrackingProperty())
        entity.addPropertyRecord( entity.getTrackingValueRecord(property));
    }*/
    // check consistency of catalog locale at minimum in locale inheritance schema
    // load records from RecordServices
    Map<String, Map<Long,Set<IPropertyRecordDTO>>> returnMap = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      returnMap.put("result", RecordDASFactory.loadRecordsForEntities(currentConn,
          this, entityIIds, propertyDTOs.toArray(new IPropertyDTO[0]))) ;
    });
    return returnMap.get("result");
  }

  public List<IPropertyDTO> getReferencedPropertiesByCodes(HashSet<String> codes) throws RDBMSException, SQLException
  {
    return ConfigurationDAO.instance().getPropertyDTOs(codes);
  }
  
  @Override
  public Set<Long> getOtherSideIIdsFromRelations(Long baseEntityIID)
      throws RDBMSException
  {
    Set<Long> otherSideIIds = new HashSet<>();
    String query = String.format(Q_GET_OTHER_SIDE_IIDS, baseEntityIID,baseEntityIID);

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn)-> {
      PreparedStatement stmt = currentConn.prepareStatement(query);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while(result.next()) {
        long side1entityiid = result.getLong("side1entityiid");
        long side2entityiid = result.getLong("side2entityiid");
        if(baseEntityIID.equals(side1entityiid)) {
          otherSideIIds.add(side2entityiid);
        }
        else if(baseEntityIID.equals(side2entityiid)) {
          otherSideIIds.add(side1entityiid);
        }
        }
    });

    return otherSideIIds;

  }
  
  @Override
  public Map<Long, IJSONContent> getEntityExtensionByIIDs(Set<Long> objectIIDs) throws RDBMSException
  {
    Map<Long, IJSONContent> returnMap = new HashMap<>();
    if (!objectIIDs.isEmpty()) {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        String query = String.format(Q_GET_ENTITY_EXTENSION_BY_IIDS, Text.join(",", objectIIDs));
        PreparedStatement prepareStatement = currentConn.prepareStatement(query);
        IResultSetParser result = currentConn.getResultSetParser(prepareStatement.executeQuery());
        while (result.next()) {
          Long baseEntityIId = result.getLong("baseentityiid");
          IJSONContent entityExtension = new JSONContent();
          entityExtension.fromString(result.getStringFromJSON("entityextension"));
          returnMap.put(baseEntityIId, entityExtension);
        }
      });
    }
    return returnMap;
  }


  @Override
  public void fillAllProperties(List<IBaseEntityDTO> baseEntityDTOs) throws RDBMSException
  {
    Long[] baseEntityIIDsArray = baseEntityDTOs.stream().map(IBaseEntityIDDTO::getBaseEntityIID).toArray(Long[]::new);

    Map<Long, List<IValueRecordDTO>> valueRecords = getAllValueRecords(baseEntityIIDsArray);
    Map<Long, List<ITagsRecordDTO>> tagRecords = getAllTagsRecords(baseEntityIIDsArray);
    Map<Long, Map<Long,IRelationsSetDTO>> relations = getAllRelationRecords(baseEntityIIDsArray);

    for(IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      List<IValueRecordDTO> values = valueRecords.computeIfAbsent(baseEntityDTO.getBaseEntityIID(), k -> new ArrayList<>());
      if(!values.isEmpty()){
        baseEntityDTO.getPropertyRecords().addAll(values);
      }
      List<ITagsRecordDTO> tags = tagRecords.computeIfAbsent(baseEntityDTO.getBaseEntityIID(), k -> new ArrayList<>());
      if(!tags.isEmpty()) {
        baseEntityDTO.getPropertyRecords().addAll(tags);
      }
      Map<Long,IRelationsSetDTO> relation = relations.computeIfAbsent(baseEntityDTO.getBaseEntityIID(), k -> new HashMap<>());
      if(!relation.isEmpty()) {
        baseEntityDTO.getPropertyRecords().addAll(relation.values());
      }
    }
  }

  public static String FETCH_CHILDREN = "select parentIID, baseEntityIID, baseEntityID, baseType from pxp.baseEntity where parentIID in (%s)";
  @Override
  public Map<Long, List<IBaseEntityDTO>> getAllChildren(List<IBaseEntityDTO> entities) throws RDBMSException
  {
    Map<Long, List<IBaseEntityDTO>> variants = new HashMap<>();

    Function<IBaseEntityDTO, String> k = y -> String.valueOf(y.getBaseEntityIID());
    String finalQuery = String.format(FETCH_CHILDREN, Text.join(",", entities,"%s", k));

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while (result.next()) {
        BaseEntityDTO child = new BaseEntityDTO();
        long parentIID = result.getLong("parentIID");
        child.mapBasicInfo(result, this);
        List<IBaseEntityDTO> baseEntityDTOs = variants.computeIfAbsent(parentIID, y -> new ArrayList<>());
        baseEntityDTOs.add(child);
      }
    });
    return variants;
  }
  public static String FETCH_LINKED_INSTANCES = "SELECT contextualobjectiid, string_agg(baseentityiid::character varying, ',')"
      +" AS cxtlinkedbaseentitiyiids  FROM contextbaseentitylink where contextualobjectiid in (%s) GROUP BY contextualobjectiid";

  public Map<Long, Set<Long>> getLinkedInstancesForContexts(Set<Long> contextualObjectIIDs) throws RDBMSException
  {
    Map<Long, Set<Long>> linkedInstanceIdVsContextIIDs = new HashMap<>();
    if(contextualObjectIIDs.isEmpty()){
      return linkedInstanceIdVsContextIIDs;
    }
    String finalQuery = String.format(FETCH_LINKED_INSTANCES, Text.join(",", contextualObjectIIDs));

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while (result.next()) {
        long contextualObjectIID = result.getLong("contextualobjectiid");
        String linkedIIDs = result.getString("cxtlinkedbaseentitiyiids");
        if(!linkedIIDs.contains(",")){
          Set<Long> linkedInstanceIIDs = linkedInstanceIdVsContextIIDs.computeIfAbsent(Long.parseLong(linkedIIDs), k -> new HashSet<>());
          linkedInstanceIIDs.add(contextualObjectIID);
          continue;
        }
        String[] split = linkedIIDs.split(",");
        for (String linkedInstanceIID : split) {
          Set<Long> linkedInstanceIIDs = linkedInstanceIdVsContextIIDs.computeIfAbsent(Long.parseLong(linkedInstanceIID), k -> new HashSet<>());
          linkedInstanceIIDs.add(contextualObjectIID);
        }
      }
    });
    return linkedInstanceIdVsContextIIDs;
  }

  public static String FETCH_LINKED_INSTANCES_BY_BASETYPE = "SELECT cbel.baseentityiid FROM pxp.contextbaseentitylink cbel "
      + "JOIN pxp.baseentity be ON cbel.baseentityiid = be.baseentityiid AND be.basetype = ? WHERE cbel.contextualobjectiid = ?";

  @Override
  public Set<Long> getLinkedInstancesByBaseType(Long contextualObjectIID, BaseType baseType) throws RDBMSException
  {
    Set<Long> linkedInstances = new HashSet<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(FETCH_LINKED_INSTANCES_BY_BASETYPE);
      stmt.setInt(1, baseType.ordinal());
      stmt.setLong(2, contextualObjectIID);

      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());

      while(result.next())
      {
        linkedInstances.add(result.getLong("baseentityiid"));
      }
    });

    return linkedInstances;
  }



}
