package com.cs.core.bgprocess.services.dataintegration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.ITransferPlanDTO;
import com.cs.core.bgprocess.services.datarules.DataRuleUtil;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.strategy.usecase.klass.GetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAS;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflict;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.DataTransferBGPModelBuilder;
import com.cs.di.runtime.authorization.utils.PartnerAuthorizationUtils;
import com.cs.di.runtime.business.process.utils.TriggerBusinessProcessWFUtils;
import com.cs.di.runtime.entity.dao.IPartnerAuthorizationDAO;
import com.cs.di.runtime.entity.dao.PartnerAuthorizationDAO;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.Usecase;

public class BaseEntityTransferDAS {
  
  public static final String                              Q_GET_EVENT_IID  = "Select trackIID from pxp.eventqueuewithdata where objectIID= ? and eventType = ? order by trackiid desc";
  private static String IS_ALL_RELATIONSHIPS_SELECTED         = "isAllRelationshipsSelected";
  private static String RELATIONSHIP_MAPPINGS                 = "relationshipMappings";
  private final UserSessionDTO                            userSessionDTO;
  ILocaleCatalogDAO                                       localeCatalogDAO = null;
  RDBMSComponentUtils   rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
  final static DataTransferBGPModelBuilder dataTransferModelBuilder = BGProcessApplication.getApplicationContext().getBean(
      DataTransferBGPModelBuilder.class);
  
  static IGetConfigDetailsWithoutPermissionsStrategy getConfigDetails = BGProcessApplication.getApplicationContext().getBean(
      GetConfigDetailsWithoutPermissionsStrategy.class);

  static ConfigUtil configUtil = BGProcessApplication.getApplicationContext().getBean(ConfigUtil.class);
  KlassInstanceUtils  klassInstanceUtils = BGProcessApplication.getApplicationContext().getBean(KlassInstanceUtils.class);
  static GoldenRecordUtils goldenRecordUtils = BGProcessApplication.getApplicationContext().getBean(GoldenRecordUtils.class);


   /**
   * Create the DAO from user session and locale catalog information
   *
   * @param pUserSessionDTO
   * @param pLocalCatalog
   */
    
  public BaseEntityTransferDAS(IUserSessionDTO pUserSessionDTO)
  {
    userSessionDTO = (UserSessionDTO) pUserSessionDTO;
  }  

  /**
   * Create a transferred base entity into another catalog
   * 
   * @param localeCatalogDAO
   * @param sourceBaseEntityIID
   * @param transferPlanDTO
   * @param properties
   * @param isRelationship flag is true if side2 entity is passed, is false in all other cases 
   * @return
   * @throws Exception
   */
  public IBaseEntityDTO transferEntityByIID(ILocaleCatalogDAO localeCatalogDAO,long sourceBaseEntityIID, ITransferPlanDTO transferPlanDTO, IPropertyDTO[] properties,  boolean isRelationship)
      throws Exception
  {
    this.localeCatalogDAO = localeCatalogDAO;
    IBaseEntityDAO sourceEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(sourceBaseEntityIID));
    IBaseEntityDTO sourceEntityDTO = sourceEntityDAO.loadPropertyRecords(properties);
    Set<IPropertyRecordDTO> createdUpdatedPropertyRecords = new HashSet<IPropertyRecordDTO>();
    IGetConfigDetailsForCustomTabModel sourceConfigDetails = null;    
      try {
        IMulticlassificationRequestModel multiclassificationRequestModel = configUtil
            .getConfigRequestModelForImport(new ArrayList<String>(), sourceEntityDAO);
        sourceConfigDetails = getConfigDetails.execute(multiclassificationRequestModel);
      }
      catch (Exception e) {
        RDBMSLogger.instance().throwException(new CSFormatException(e.getMessage()));
      }
    String targetCatalogCode = transferPlanDTO.getTargetCatalogCode();
    String targetOrganizationCode = transferPlanDTO.getTargetOrganizationCode();
    String targetEndpointCode = transferPlanDTO.getTargetEndPointCode();
    Map<String, Object> authorizationMapping = getPartnerAuthMapping(transferPlanDTO);
    if (authorizationMapping != null && !EmbeddedType.CONTEXTUAL_CLASS.equals(sourceEntityDTO.getEmbeddedType())) {
      try {
        if (!isRelationship) {      
          // Nature class check for auth Mapping handled ;
          if (!PartnerAuthorizationUtils.isAuthorizedNatureClass(sourceEntityDTO, authorizationMapping)) {
            throw new Exception("Found Unauthrized Nature Class");
          }
        }
        else {
          if (!PartnerAuthorizationUtils.isAuthorizedNatureClass(sourceEntityDTO,
              authorizationMapping)) {
            RDBMSLogger.instance().info("Found Unauthorized Nature Class");
            return null;
          }
        }
      }
      catch (Exception exception) {
     // in case Found Unauthrized Nature Class no transfer needed
        RDBMSLogger.instance().throwException(new CSFormatException(exception.getMessage()));
      }
    }
    
    String targetOrgCode = targetOrganizationCode.equals(IStandardConfig.STANDARD_ORGANIZATION_CODE)
        ? IStandardConfig.STANDARD_ORGANIZATION_RCODE
        : targetOrganizationCode;
    // Check if target entity exists
    String baseLocaleID = sourceEntityDTO.getBaseLocaleID();
    LocaleCatalogDAO targetCatalogDAO = new LocaleCatalogDAO(userSessionDTO,
        new LocaleCatalogDTO(baseLocaleID, targetCatalogCode, targetOrgCode));
    
    long targetEntityIID = localeCatalogDAO.getEntityIID(sourceEntityDTO.getBaseEntityID(), targetCatalogCode, targetOrgCode, targetEndpointCode);
    boolean targetEntityExists = (targetEntityIID != -1);
    IBaseEntityDAO targetEntityDAO = targetEntityExists ? targetCatalogDAO.openBaseEntity(targetCatalogDAO.getEntityByIID(targetEntityIID))
        : targetCatalogDAO.openTargetBaseEntityDAO(sourceEntityDTO.getBaseEntityID(), sourceEntityDAO, false, targetEndpointCode);
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      BaseEntityDAS baseEntityDAS = new BaseEntityDAS(currentConn);
      if (targetEntityDAO.getBaseEntityDTO().getOriginBaseEntityIID() == 0L) {
        baseEntityDAS.updateOriginBaseEntityIId(sourceEntityDTO.getBaseEntityIID(), targetEntityDAO.getBaseEntityDTO().getBaseEntityIID());
      }     
});
   
    
    Set<IPropertyDTO> contextualProperties = new HashSet<>();
    Set<IPropertyDTO> propertiesToDeteleFromTranslatinFetch = new HashSet<>();
    
    for (IPropertyRecordDTO propertyRecord : sourceEntityDTO.getPropertyRecords()) {
      if (propertyRecord.getProperty().getSuperType() == SuperType.ATTRIBUTE) {
        if (!((IValueRecordDTO) propertyRecord).getContextualObject().isNull()) {
          contextualProperties.add(propertyRecord.getProperty());
        }
        if (((IValueRecordDTO) propertyRecord).getLocaleID().isEmpty()) {
          propertiesToDeteleFromTranslatinFetch.add(propertyRecord.getProperty());
        }
      }
      else {
        propertiesToDeteleFromTranslatinFetch.add(propertyRecord.getProperty());
      }
    }
    Set<IRelationsSetDTO>  createdRelationshipRecords = new HashSet<>();
    // create or update properties for tranfer.
    boolean shouldCreateRevision = handlePropertiesForTransfer(sourceEntityDTO, targetCatalogDAO,
        targetEntityExists, targetEntityDAO, contextualProperties, true, properties, transferPlanDTO,
        authorizationMapping, createdUpdatedPropertyRecords, sourceConfigDetails, createdRelationshipRecords);
    
    List<String> originLocaleIds = sourceEntityDTO.getLocaleIds();
    // transferred BaseEntity is already created with originBaseLocaleId. so
    // avoiding it for creating translations.
    originLocaleIds.remove(baseLocaleID);
    List<String> translationsLocaleIds = new ArrayList<>();
    translationsLocaleIds.addAll(originLocaleIds);
    IBaseEntityDTO baseEntityDTO = targetEntityDAO.getBaseEntityDTO();
    if (targetEntityExists) {
      List<String> targetLocaleIds = baseEntityDTO.getLocaleIds();
      targetLocaleIds.remove(baseEntityDTO.getBaseLocaleID());
      for (String localeId : targetLocaleIds) {
        if (!originLocaleIds.contains(localeId)) {
          targetEntityDAO.deleteLanguageTranslation(targetEntityIID, localeId);
        }
        else {
          translationsLocaleIds.remove(localeId);
        }
      }
    }
    List<Long> dependentPropertyIIDs = dataTransferModelBuilder.fetchDependentPropertyIIDs(sourceConfigDetails); 
    // create language translations
    if (!translationsLocaleIds.isEmpty()) {
      targetEntityDAO.createLanguageTranslation(translationsLocaleIds);
      if(!dependentPropertyIIDs.isEmpty())
      {
        dataTransferModelBuilder.initiateBulkLanguageInheritanceDataTransfer(targetEntityDAO.getLocaleCatalog(), baseEntityDTO.getBaseEntityIID(), dependentPropertyIIDs,translationsLocaleIds); 
      }
    }   
    List<IPropertyDTO> propertiesToFetchForTranslation = new ArrayList<IPropertyDTO>();
    propertiesToFetchForTranslation.addAll(Arrays.asList(properties));
    propertiesToFetchForTranslation.removeAll(propertiesToDeteleFromTranslatinFetch);
    
    // update or create all language dependent attributes with its locale
    for (String localeId : originLocaleIds) {
      LocaleCatalogDAO sourceCatalogDAO = new LocaleCatalogDAO(userSessionDTO, new LocaleCatalogDTO(localeId,
          sourceEntityDTO.getCatalog().getCatalogCode(), sourceEntityDTO.getCatalog().getOrganizationCode()));
      IBaseEntityDAO localeSourceEntityDAO = sourceCatalogDAO.openBaseEntity(sourceEntityDTO);
      LocaleCatalogDAO targetLocaleCatalogDAO = new LocaleCatalogDAO(userSessionDTO,
          new LocaleCatalogDTO(localeId, targetCatalogCode, targetOrganizationCode));
      IBaseEntityDAO localeTargetEntityDAO = targetLocaleCatalogDAO.openBaseEntity(baseEntityDTO);
      
      IBaseEntityDTO localeSourceEntityDTO = localeSourceEntityDAO
          .loadPropertyRecords(propertiesToFetchForTranslation.toArray(new IPropertyDTO[0]));
      for (IPropertyRecordDTO propertyRecord : localeSourceEntityDTO.getPropertyRecords()) {
        if (propertyRecord.getProperty().getSuperType() == SuperType.ATTRIBUTE) {
          if (!((IValueRecordDTO) propertyRecord).getContextualObject().isNull()) {
            contextualProperties.add(propertyRecord.getProperty());
          }
        }
      }
      boolean targetEntityExistsInLocaleID = false;
      if (!translationsLocaleIds.contains(localeId)) {
        targetEntityExistsInLocaleID = true;
      }
      boolean shouldCreateLocalRevision = handlePropertiesForTransfer(localeSourceEntityDTO, targetLocaleCatalogDAO, targetEntityExistsInLocaleID, localeTargetEntityDAO,
          contextualProperties, false, propertiesToFetchForTranslation.toArray(new IPropertyDTO[0]), transferPlanDTO, authorizationMapping,
          createdUpdatedPropertyRecords, sourceConfigDetails, createdRelationshipRecords);
      shouldCreateRevision = shouldCreateRevision ? shouldCreateRevision : shouldCreateLocalRevision;
    }
    List<IClassifierDTO> addRemoveClassifier =  new ArrayList<IClassifierDTO>();
    List<IClassifierDTO> sourceClassifiers = sourceEntityDAO.getClassifiers();
    if (authorizationMapping != null) {
      PartnerAuthorizationUtils.filterAuthorizedClassifier(sourceClassifiers, authorizationMapping);
    }
    Set<IClassifierDTO> classifiersToAdd = new HashSet<>();
    Set<IClassifierDTO> classifiersToRemove = new HashSet<>();
    if (!targetEntityExists && !sourceClassifiers.isEmpty()) {
      targetEntityDAO.addClassifiers(sourceClassifiers.toArray(new IClassifierDTO[0]));
      addRemoveClassifier.addAll(sourceClassifiers);
    }
    else {
      List<IClassifierDTO> targetClassifiers = targetEntityDAO.getClassifiers();
      sourceClassifiers.forEach(classifier -> {
        if (!targetClassifiers.contains(classifier)) {
          classifiersToAdd.add(classifier);
        }
      });
      if (!classifiersToAdd.isEmpty()) {
        targetEntityDAO.addClassifiers(classifiersToAdd.toArray(new IClassifierDTO[0]));
        addRemoveClassifier.addAll(sourceClassifiers);
        shouldCreateRevision = true;
      }
      
      boolean booleanHandleForBlankValueAllowed = PartnerAuthorizationUtils.getIsTaxonomyBlank(sourceClassifiers)
          ? authorizationMapping != null ? !(Boolean) authorizationMapping.get("isBlankValueAcceptedForTaxonomies") : false
          : false;
      targetClassifiers.forEach(classifier -> {
        if (!sourceClassifiers.contains(classifier)
            && PartnerAuthorizationUtils.isClassifierAuthorized(classifier, authorizationMapping, booleanHandleForBlankValueAllowed)) {
          classifiersToRemove.add(classifier);
        }
      });
      if (!classifiersToRemove.isEmpty()) {
        //Remove properties related to classifier from base entity
        removeClassifierProperties(sourceConfigDetails, targetCatalogDAO, targetEntityDAO);
        
        targetEntityDAO.removeClassifiers(classifiersToRemove.toArray(new IClassifierDTO[0]));
        addRemoveClassifier.addAll(classifiersToRemove);
        shouldCreateRevision = true;
      }
    }
    handleEmbeddedVariantForTransfer(sourceBaseEntityIID, transferPlanDTO, targetEntityExists, targetEntityDAO, authorizationMapping);
    
    // Prepare and initiate data rule handling 
    Map<String, Object> requestModel = DataRuleUtil.prepareConfigBulkPropagtionRequestModel(baseEntityDTO, IStandardConfig.StandardUser.admin.toString());
    IConfigDetailsForBulkPropagationResponseModel configDetails = DataRuleUtil.getConfigDetailsForBulkPropagtion(requestModel, baseLocaleID);
    DataRuleUtil.propagateRule(targetEntityDAO, configDetails);
    
    //Initiate background processes for bulk propagation
    initiateBulkPropagation(targetEntityDAO, classifiersToAdd, classifiersToRemove,
        sourceConfigDetails, createdRelationshipRecords, dependentPropertyIIDs,sourceEntityDAO);
    
    // Handling for auto create variant.
    if (!targetEntityExists) {
      IKlassInstanceInformationModel klassInstanceInformationModel = dataTransferModelBuilder
          .fillklassInstanceInformationModel(targetEntityDAO.getBaseEntityDTO());
      IReferencedKlassDetailStrategyModel referencedKlass = sourceConfigDetails
          .getReferencedKlasses()
          .get(klassInstanceInformationModel.getTypes()
              .get(0));
      String newInstanceName = referencedKlass.getLabel() + dataTransferModelBuilder.getCounter();
      
      List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableModel = sourceConfigDetails
          .getTechnicalImageVariantContextWithAutoCreateEnable();
      VariantInstanceUtils.createEmbeddedVariantsWithAutoCreateEnabledTransfer(newInstanceName,
          targetEntityDAO.getBaseEntityDTO(), klassInstanceInformationModel,
          contextsWithAutoCreateEnableModel, dataTransferModelBuilder.getTransactionThread(),
          rdbmsComponentUtils.getUserName(), targetEntityDAO);
    }
    /* RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
    initiateDataCoupling(targetEntityDAO, classifiersToAdd, classifiersToRemove, sourceConfigDetails, createdRelationshipRecords);            
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      EventQueueDAS eventQueueDAS = new EventQueueDAS(currentConn, userSessionDTO);
      PreparedStatement statement = currentConn.prepareStatement(Q_GET_EVENT_IID);
      statement.setLong(1, baseEntityDTO.getBaseEntityIID());
      statement.setInt(2, targetEntityExists ? EventType.OBJECT_UPDATE.ordinal() : EventType.OBJECT_CREATION.ordinal());
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next()) {
        long trackIID = executeQuery.getLong("trackIID");
       // eventQueueDAS.postEventRedirection(EventType.OBJECT_TRANSFER, trackIID, null);
         }
      else {
        throw new RDBMSException(500, "Inconsistency",
            String.format("No entry will be found for transfered entity into evnt queue for 
            " + "baseEntityId : %s and baseEntityIID : %d",
                baseEntityDTO.getBaseEntityID(), baseEntityDTO.getBaseEntityIID()));
      }
    });*/ 
    targetCatalogDAO.loadLocaleIds(baseEntityDTO);
    Set<IPropertyDTO> loadProperties = (Set<IPropertyDTO>) targetCatalogDAO.getAllEntityProperties(baseEntityDTO.getBaseEntityIID());
    targetEntityDAO.loadPropertyRecords(loadProperties.toArray(new IPropertyDTO[0]));
    if (transferPlanDTO.getIsRevisionableTransfer() || shouldCreateRevision) {
      rdbmsComponentUtils.createNewRevision(baseEntityDTO, sourceConfigDetails.getNumberOfVersionsToMaintain());
    }
    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(targetEntityDAO.getBaseEntityDTO());
    BusinessProcessActionType action = targetEntityExists ? BusinessProcessActionType.AFTER_SAVE
        : BusinessProcessActionType.AFTER_CREATE;
    new TriggerBusinessProcessWFUtils().triggerBusinessProcessWF(baseEntityDTO, action, userSessionDTO,
        createdUpdatedPropertyRecords, addRemoveClassifier, sourceClassifiers, Usecase.TRANSFER);
 
    return baseEntityDTO;
  }

  @SuppressWarnings("unchecked")
  private boolean handlePropertiesForTransfer(IBaseEntityDTO sourceEntityDTO, LocaleCatalogDAO targetCatalogDAO, boolean targetEntityExists,
      IBaseEntityDAO targetEntityDAO, Set<IPropertyDTO> contextualProperties, Boolean shouldDeleteProperties, IPropertyDTO[] properties,
      ITransferPlanDTO transferPlanDTO, Map<String, Object> authorizationMapping, Set<IPropertyRecordDTO> createdUpdatedPropertyRecords,
      IGetConfigDetailsForCustomTabModel sourceConfigDetails, Set<IRelationsSetDTO> createdRelationshipRecords) throws RDBMSException, CSFormatException
  {
    IBaseEntityDTO targetBaseEntityDTO = targetEntityDAO.getBaseEntityDTO();
    Set<IPropertyDTO> targetProperties = new HashSet<>();
    Set<IPropertyDTO> targetContextualProperties = new HashSet<>();
    if (targetEntityExists) {
      targetProperties.addAll(localeCatalogDAO.getAllEntityProperties(targetEntityDAO.getBaseEntityDTO().getBaseEntityIID()));
      targetBaseEntityDTO = targetEntityDAO.loadPropertyRecords(targetProperties.toArray(new IPropertyDTO[0]));
      // PXPFDEV-20161 : Thumbnail Image is displayed in destination tile view when article is transferred again without relationship.
      if (sourceEntityDTO.getDefaultImageIID() == 0) {
        targetBaseEntityDTO.setDefaultImageIID(0);
      }
      targetBaseEntityDTO.getPropertyRecords().forEach(propertyRecord -> {
        if (propertyRecord.getProperty().getSuperType() == SuperType.ATTRIBUTE) {
          if (!((IValueRecordDTO) propertyRecord).getContextualObject().isNull()) {
            targetContextualProperties.add(propertyRecord.getProperty());
          }
        }
      });
    }
    
    List<IPropertyRecordDTO> propertyRecordsToCreate = new ArrayList<>();
    List<IPropertyRecordDTO> propertyRecordsToUpdate = new ArrayList<>();
    List<IPropertyRecordDTO> propertyRecordsToDelete = new ArrayList<>();
    List<String> versionableAttributes = sourceConfigDetails.getVersionableAttributes();
    List<String> versionableTags = sourceConfigDetails.getVersionableTags();
    Map<String, IAttribute> referencedAttributes = sourceConfigDetails.getReferencedAttributes();
    ICouplingDAO couplingDAO = localeCatalogDAO.openCouplingDAO();
    String dataLanguage = localeCatalogDAO.getLocaleCatalogDTO().getLocaleID();
    ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(dataLanguage);
    for (IPropertyDTO property : properties) {
     
      if (contextualProperties.contains(property)) {
        continue;
      }
      IPropertyRecordDTO originPropertyRecord = sourceEntityDTO.getPropertyRecord(property.getIID());
      if (originPropertyRecord == null) {
        RDBMSLogger.instance().warn("Cannot transfer property record for propertyIID : %d as its not done exist in origin",
            property.getIID());
        continue;
      }
      IPropertyRecordDTO targetPropertyRecord = null;
      if (targetEntityExists) {
        targetPropertyRecord = targetBaseEntityDTO.getPropertyRecord(property.getIID());
      }
      if (targetPropertyRecord == null) {
        //Do not create the relationship record if it is not authorized
        if (authorizationMapping != null && originPropertyRecord.getProperty().getSuperType().equals(SuperType.RELATION_SIDE)
            && (!(Boolean) authorizationMapping.get(IS_ALL_RELATIONSHIPS_SELECTED)
                && !((List<String>) authorizationMapping.get(RELATIONSHIP_MAPPINGS))
                    .contains(originPropertyRecord.getProperty().getCode()))) {
          continue;
        }
        IPropertyRecordDTO propertyRecord = handleCreatePropertyForTransfer(sourceEntityDTO, targetCatalogDAO, targetEntityDAO,
            originPropertyRecord, transferPlanDTO);
        if (!(propertyRecord instanceof IRelationsSetDTO
            && ((IRelationsSetDTO) propertyRecord).getReferencedBaseEntityIIDs().length == 0)) {
          propertyRecordsToCreate.add(propertyRecord);
        }
        if(propertyRecord instanceof IRelationsSetDTO){
          createdRelationshipRecords.add((IRelationsSetDTO) propertyRecord);
        }
      }
      else {
        if (TriggerBusinessProcessWFUtils.isPropertyUpdatedForWFConfig(originPropertyRecord, targetPropertyRecord)) {
          createdUpdatedPropertyRecords.add(targetPropertyRecord);
        }
        boolean isValueChange = handleUpdatePropertyForTransfer(sourceEntityDTO, originPropertyRecord,
            targetPropertyRecord, targetCatalogDAO, targetEntityDAO, transferPlanDTO);
        if (isValueChange) {
          Long localId = 0l;
          if (targetPropertyRecord.getProperty().getSuperType().equals(SuperType.ATTRIBUTE)
              && referencedAttributes.get(property.getCode()).getIsTranslatable()) {
            localId = languageConfig.getLanguageIID();
          }
          if (!targetPropertyRecord.isCoupled()) {
            couplingDAO.updateTargetConflictingValues(targetPropertyRecord.getProperty().getPropertyIID(),
                targetEntityDAO.getBaseEntityDTO().getBaseEntityIID(), localId);
          }
          propertyRecordsToUpdate.add(targetPropertyRecord);
        }
      }
    }
    
    if (targetEntityExists && shouldDeleteProperties) {
      targetProperties.removeAll(Arrays.asList(properties));
      handleDeletePropertierForTransfer(targetBaseEntityDTO, targetProperties, propertyRecordsToDelete);
    }
    
    handleAttributeVariantForTransfer(sourceEntityDTO, targetEntityDAO, contextualProperties,
        propertyRecordsToCreate, propertyRecordsToUpdate, propertyRecordsToDelete, targetEntityExists,
        targetContextualProperties);
    
    // useCase 0,1,2 stands for create , update and Delete action respectively
    if (!propertyRecordsToCreate.isEmpty()) {
      if (authorizationMapping != null) {
        PartnerAuthorizationUtils.filterAuthorizedProperties(propertyRecordsToCreate, authorizationMapping, 0);
      }
      targetEntityDAO.createPropertyRecords(propertyRecordsToCreate.toArray(new IPropertyRecordDTO[0]));
    }
    
    if (!propertyRecordsToUpdate.isEmpty()) {
      if (authorizationMapping != null) {
        PartnerAuthorizationUtils.filterAuthorizedProperties(propertyRecordsToUpdate, authorizationMapping, 1);
      }
      targetEntityDAO.updatePropertyRecords(propertyRecordsToUpdate.toArray(new IPropertyRecordDTO[0]));
    }
    
    if (!propertyRecordsToDelete.isEmpty()) {
      if (authorizationMapping != null) {
        PartnerAuthorizationUtils.filterAuthorizedProperties(propertyRecordsToDelete, authorizationMapping, 2);
      }
      targetEntityDAO.deletePropertyRecords(propertyRecordsToDelete.toArray(new IPropertyRecordDTO[0]));
    }

    return shouldCreateRevision(propertyRecordsToCreate, propertyRecordsToUpdate, propertyRecordsToDelete,
        versionableAttributes, versionableTags);
  }

  private boolean shouldCreateRevision(List<IPropertyRecordDTO> propertyRecordsToCreate,
      List<IPropertyRecordDTO> propertyRecordsToUpdate,
      List<IPropertyRecordDTO> propertyRecordsToDelete, List<String> versionableAttributes,
      List<String> versionableTags)
  {
    List<IPropertyRecordDTO> propertyToCheck = new ArrayList<IPropertyRecordDTO>(propertyRecordsToCreate);
    propertyToCheck.addAll(propertyRecordsToUpdate);
    propertyToCheck.addAll(propertyRecordsToDelete);
    for (IPropertyRecordDTO record : propertyToCheck) {
      String propertyCode = record.getProperty().getPropertyCode();
      if (versionableAttributes.contains(propertyCode) || versionableTags.contains(propertyCode) || record instanceof IRelationsSetDTO) {
        return true;
      }
    }
    return false;
  }


  private IPropertyRecordDTO handleCreatePropertyForTransfer(IBaseEntityDTO sourceEntityDTO, LocaleCatalogDAO targetCatalogDAO,
      IBaseEntityDAO targetEntityDAO, IPropertyRecordDTO originPropertyRecord, ITransferPlanDTO transferPlanDTO)
      throws RDBMSException, CSFormatException
  {
    IPropertyRecordDTO propertyRecord = createPropertyRecordDTO(originPropertyRecord, targetEntityDAO);
    
    if (originPropertyRecord.getProperty().getIID() == StandardProperty.nameattribute.getIID()) {
      // handling for name attribute which doesn't follow the rule of transfer
      // coupling
      ((IValueRecordDTO) propertyRecord).setValue(((IValueRecordDTO) originPropertyRecord).getValue());
    }
    else if (originPropertyRecord.isCalculated()) {
      // add calculation if the record is a calculated attribute
      ((IValueRecordDTO) propertyRecord).addCalculation(((IValueRecordDTO) originPropertyRecord).getCalculation());
    }
    else if (originPropertyRecord.getProperty().getSuperType() == SuperType.RELATION_SIDE) {
      Long[] referencedBaseEntityIIDs = ((IRelationsSetDTO) originPropertyRecord).getReferencedBaseEntityIIDs();
      Set<IEntityRelationDTO> relations = ((IRelationsSetDTO) originPropertyRecord).getRelations();
      ((IRelationsSetDTO) propertyRecord).removeRelations(referencedBaseEntityIIDs);
      // For relations => check if the target entity is present in target
      // catalog and change it if yes
      for (IEntityRelationDTO relation : relations) {
        
       //patch for bug PXPFDEV-21732
        if(relation.getOtherSideContextualObject().getContextStartTime() == 0 && relation.getOtherSideContextualObject().getContextEndTime()== -1) {
          relation.getOtherSideContextualObject().setContextEndTime(0);
        }
        if(relation.getContextualObject().getContextStartTime() == 0 && relation.getContextualObject().getContextEndTime()== -1) {
          relation.getContextualObject().setContextEndTime(0);
        }
        //End
        long defaultImageIID = sourceEntityDTO.getDefaultImageIID();
        long otherSideEntityIID = relation.getOtherSideEntityIID();
        String refrencedEntityID = localeCatalogDAO.getEntityIdByIID(otherSideEntityIID);
        long targetRefrencedEntityIID = targetCatalogDAO.getEntityIIDByID(refrencedEntityID, transferPlanDTO.getTargetEndPointCode());
        
        if (targetRefrencedEntityIID != 0) {
          relation.setOtherSideEntityIID(targetRefrencedEntityIID);
          ((IRelationsSetDTO) propertyRecord).addRelations(relation);
          if(defaultImageIID == otherSideEntityIID) {
            targetEntityDAO.getBaseEntityDTO().setDefaultImageIID(relation.getOtherSideEntityIID());
          }
        }
        else if (isStandardArticleAssetRelationship(propertyRecord)) {
          Boolean isTransferComplete = transferOtherSideEntity(targetCatalogDAO, targetEntityDAO, (IRelationsSetDTO) propertyRecord, relation,
              transferPlanDTO);
          // default image IId
          if (isTransferComplete && defaultImageIID == otherSideEntityIID) {
            targetEntityDAO.getBaseEntityDTO().setDefaultImageIID(relation.getOtherSideEntityIID());
          }
        }
      }
      
      if (((IRelationsSetDTO) propertyRecord).getReferencedBaseEntityIIDs().length == 0) {
        return propertyRecord;
      }
    }
    return propertyRecord;
  }
  
  /**
   * Checked it is standard article asset relationship or not
   * 
   * @param propertyRecord
   * @return
   */
  
  private boolean isStandardArticleAssetRelationship(IPropertyRecordDTO propertyRecord)
  {
    return propertyRecord.getProperty().getPropertyCode()
        .equals(IStandardConfig.StandardProperty.standardArticleAssetRelationship.toString());
  }
  
  private void handleDeletePropertierForTransfer(IBaseEntityDTO targetEntityDTO, Set<IPropertyDTO> targetProperties,
      List<IPropertyRecordDTO> propertyRecordsToDelete) throws RDBMSException
  {
    if (!targetProperties.isEmpty()) {
      for (IPropertyDTO deletedProperty : targetProperties) {
        // Delete missing relationships
        if (deletedProperty.getSuperType() == SuperType.RELATION_SIDE || deletedProperty.getSuperType() == SuperType.TAGS) {
          propertyRecordsToDelete.add(targetEntityDTO.getPropertyRecord(deletedProperty.getIID()));
        }
      }
    }
  }
  
  private boolean handleUpdatePropertyForTransfer(IBaseEntityDTO sourceEntityDTO, IPropertyRecordDTO originPropertyRecord,
      IPropertyRecordDTO targetPropertyRecord, LocaleCatalogDAO targetCatalogDAO, IBaseEntityDAO targetEntityDAO,
      ITransferPlanDTO transferPlanDTO) throws RDBMSException, CSFormatException
  {
    boolean isValueChange = copyValue(originPropertyRecord, targetPropertyRecord);
    
    IPropertyDTO property = targetPropertyRecord.getProperty();
    if (property.getSuperType() == SuperType.RELATION_SIDE) {
      Set<IEntityRelationDTO> relations = ((IRelationsSetDTO) originPropertyRecord).getRelations();
      ((IRelationsSetDTO) targetPropertyRecord).removeRelations(((IRelationsSetDTO) targetPropertyRecord).getReferencedBaseEntityIIDs());
      ;
      // For relations => check if the target entity is present in target
      // catalog and change it if yes
      for (IEntityRelationDTO relation : relations) {
        long defaultImageIID = sourceEntityDTO.getDefaultImageIID();
        long otherSideEntityIID = relation.getOtherSideEntityIID();

        String refrencedEntityID = localeCatalogDAO.getEntityIdByIID(otherSideEntityIID);
        long targetRefrencedEntityIID = targetCatalogDAO.getEntityIIDByID(refrencedEntityID, transferPlanDTO.getTargetEndPointCode());
        
        if (targetRefrencedEntityIID != 0) {
          relation.setOtherSideEntityIID(targetRefrencedEntityIID);
          ((IRelationsSetDTO) targetPropertyRecord).addRelations(relation);
          if(defaultImageIID == otherSideEntityIID) {
            targetEntityDAO.getBaseEntityDTO().setDefaultImageIID(relation.getOtherSideEntityIID());
          }
        }
        else if (isStandardArticleAssetRelationship(targetPropertyRecord)) {
          Boolean isTransferComplete = transferOtherSideEntity(targetCatalogDAO, targetEntityDAO, (IRelationsSetDTO) targetPropertyRecord, relation,
              transferPlanDTO);
          if (defaultImageIID == otherSideEntityIID && isTransferComplete) {
            targetEntityDAO.getBaseEntityDTO().setDefaultImageIID(relation.getOtherSideEntityIID());
          }
        }
      }
      if (((IRelationsSetDTO) targetPropertyRecord).getReferencedBaseEntityIIDs().length == 0) {
        return false;
      }
    }
    return isValueChange;
  }
  
  /**
   *  Transfer other side entity for only standard article asset relationship
   * with all properties other than relationship  
   * @param targetCatalogDAO
   * @param targetEntityDAO
   * @param propertyRecord
   * @param relation
   * @param transferPlanDTO
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private Boolean transferOtherSideEntity(LocaleCatalogDAO targetCatalogDAO, IBaseEntityDAO targetEntityDAO, IRelationsSetDTO propertyRecord,
      IEntityRelationDTO relation, ITransferPlanDTO transferPlanDTO) throws RDBMSException, CSFormatException
  {
    Collection<IPropertyDTO> allEntityProperties = localeCatalogDAO.getAllEntityProperties(relation.getOtherSideEntityIID());
    Set<IPropertyDTO> propertiesWithoutRelationship = allEntityProperties.stream()
        .filter(p -> !p.getPropertyType().equals(PropertyType.RELATIONSHIP)).collect(Collectors.toSet());

    try {
      IBaseEntityDTO relationTransferEntity = transferEntityByIID(localeCatalogDAO,relation.getOtherSideEntityIID(), transferPlanDTO,
          propertiesWithoutRelationship.toArray(new IPropertyDTO[0]), true);
      if (relationTransferEntity != null) {
      relation.setOtherSideEntityIID(relationTransferEntity.getBaseEntityIID());
      propertyRecord.addRelations(relation);
      targetCatalogDAO.postUsecaseUpdate(relationTransferEntity.getBaseEntityIID(), EventType.ELASTIC_UPDATE);
      return true;
      }
      return false;
    }
    catch(Exception e){
      RDBMSLogger.instance().exception(new CSFormatException(e.getMessage()));
      return false;
    }
  }
  
  private void handleEmbeddedVariantForTransfer(long sourceBaseEntityIID, ITransferPlanDTO transferPlanDTO, boolean targetEntityExists,
      IBaseEntityDAO targetEntityDAO, Map<String, Object> authorizationMapping) throws Exception
  {
    // Manage children
    List<Long> childrenIIDs = new ArrayList<>();
    Set<Long> targetChildrenOriginIIDs = new HashSet<>();
    Map<Long, Long> childIIDsWithOriginIID = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      BaseEntityDAS baseEntityDAS = new BaseEntityDAS(currentConn);
      long[] iids = baseEntityDAS.getAllChildrens(sourceBaseEntityIID);
      childrenIIDs.addAll(Arrays.asList(ArrayUtils.toObject(iids)));
    });
    
    boolean isChildIIDWithOriginIIdsFetch = true;
    for (long childrenIID : childrenIIDs) {
      IPropertyDTO[] properties = localeCatalogDAO.getAllEntityProperties(childrenIID).toArray(new IPropertyDTO[0]);
      IBaseEntityDTO sourceEntityDTO = localeCatalogDAO.getEntityByIID(childrenIID);
      if (!PartnerAuthorizationUtils.isVariantAuthorized(sourceEntityDTO, authorizationMapping)) {
        continue;
      }
      
      IBaseEntityDTO childEntityDTO = this.transferEntityByIID(localeCatalogDAO, childrenIID,
          transferPlanDTO, properties, false);
      if (isChildIIDWithOriginIIdsFetch) {
        isChildIIDWithOriginIIdsFetch = false;
        addChildIIdsWithOriginIId(targetEntityExists, targetEntityDAO, targetChildrenOriginIIDs, childIIDsWithOriginIID);
      }
      
      if (!targetEntityExists || !targetChildrenOriginIIDs.contains(childrenIID)) {
        IBaseEntityDTO originChildDTO = localeCatalogDAO.getEntityByIID(childrenIID);
        targetEntityDAO.addChildren(originChildDTO.getEmbeddedType(), childEntityDTO);
      }
    }

    // Handling of removal of existing variants on re-transfer with no variants.
    if (childrenIIDs.isEmpty()) {
      addChildIIdsWithOriginIId(targetEntityExists, targetEntityDAO, targetChildrenOriginIIDs, childIIDsWithOriginIID);
    }

    for (long childrenIID : childIIDsWithOriginIID.keySet()) {
      Long long1 = childIIDsWithOriginIID.get(childrenIID);
      if (long1 == 0 || !childrenIIDs.contains(long1)) {
        IBaseEntityDTO removedTargetChildDTO = localeCatalogDAO.getEntityByIID(childrenIID);
        
        // Authorization check before removing variant from target
        if (!PartnerAuthorizationUtils.isVariantAuthorized(removedTargetChildDTO, authorizationMapping))
          continue;
        if (removedTargetChildDTO.getContextualObject() != null)
          targetEntityDAO.removeChildren(removedTargetChildDTO.getEmbeddedType(), removedTargetChildDTO);
      }
    }
  }

  private void addChildIIdsWithOriginIId(boolean targetEntityExists, IBaseEntityDAO targetEntityDAO, Set<Long> targetChildrenOriginIIDs,
      Map<Long, Long> childIIDsWithOriginIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS baseEntityDAS = new BaseEntityDAS(currentConn);
      if (targetEntityExists) {
        // Manage already added children into target entity
        childIIDsWithOriginIID
            .putAll(baseEntityDAS.getAllChildrensOriginWithBaseEntityIID(targetEntityDAO.getBaseEntityDTO().getBaseEntityIID()));
        targetChildrenOriginIIDs.addAll(childIIDsWithOriginIID.values());
      }
    });
  }

  private void handleAttributeVariantForTransfer(IBaseEntityDTO sourceEntityDTO, IBaseEntityDAO targetEntityDAO,
      Set<IPropertyDTO> contextualProperties, List<IPropertyRecordDTO> propertyRecordsToCreate,
      List<IPropertyRecordDTO> propertyRecordsToUpdate, List<IPropertyRecordDTO> propertyRecordsToDelete, Boolean targetEntityExist,
      Set<IPropertyDTO> targetContextualProperties) throws RDBMSException, CSFormatException
  {
    // create or update attribute variants
    for (IPropertyDTO property : contextualProperties) {
      List<IPropertyRecordDTO> sourcePropertyContexts = sourceEntityDTO.getPropertyRecords().stream()
          .filter(valueRecord -> valueRecord.getProperty().getPropertyIID() == property.getPropertyIID()).collect(Collectors.toList());
      if (!targetEntityExist)
        for (IPropertyRecordDTO propertyContext : sourcePropertyContexts) {
          propertyRecordsToCreate.add(createPropertyRecordDTO(propertyContext, targetEntityDAO));
        }
      
      else {
        List<IValueRecordDTO> targetPropertyContexts = targetEntityDAO.getBaseEntityDTO().getPropertyRecords().stream()
            .filter(valueRecord -> valueRecord.getProperty().getPropertyIID() == property.getPropertyIID())
            .map(valueRecord -> (IValueRecordDTO) valueRecord).collect(Collectors.toList());
        
        for (IPropertyRecordDTO propertyContext : sourcePropertyContexts) {
          IValueRecordDTO valueRecord = (IValueRecordDTO) propertyContext;
          Optional<IValueRecordDTO> result = targetPropertyContexts.stream()
              .filter(targetPropertyContext -> targetPropertyContext.getValue().equals(valueRecord.getValue())
                  && ((targetPropertyContext.getLocaleID() == null && valueRecord.getLocaleID() == null)
                      || targetPropertyContext.getLocaleID().equals(valueRecord.getLocaleID())))
              .findFirst();
          if (result.isPresent()) {
            targetPropertyContexts.remove(result.get());
            if(copyValue(propertyContext, result.get())) {
              propertyRecordsToUpdate.add(result.get());
            }
          }
          else {
            propertyRecordsToCreate.add(createPropertyRecordDTO(propertyContext, targetEntityDAO));
          }
        }
        propertyRecordsToDelete.addAll(targetPropertyContexts);
      }
    }
    if (targetEntityExist) {
      targetContextualProperties.removeAll(contextualProperties);
      if (!targetContextualProperties.isEmpty()) {
        for (IPropertyDTO property : targetContextualProperties) {
          List<IValueRecordDTO> targetPropertyContexts = targetEntityDAO.getBaseEntityDTO().getPropertyRecords().stream()
              .filter(valueRecord -> valueRecord.getProperty().getPropertyIID() == property.getPropertyIID())
              .map(valueRecord -> (IValueRecordDTO) valueRecord).collect(Collectors.toList());
          propertyRecordsToDelete.addAll(targetPropertyContexts);
        }
      }
    }
  }
  
  private boolean copyValue(IPropertyRecordDTO originPropertyRecord, IPropertyRecordDTO propertyRecord) throws RDBMSException
  {
    IPropertyDTO property = originPropertyRecord.getProperty();
    switch (property.getSuperType()) {
      case ATTRIBUTE:
       IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
       IValueRecordDTO sourceValueRecord = (IValueRecordDTO) originPropertyRecord;
       if(isAttributeValuceChanged(valueRecord, sourceValueRecord)) {
        valueRecord.setValue(sourceValueRecord.getValue());
        valueRecord.setAsHTML(sourceValueRecord.getAsHTML());
        valueRecord.setAsNumber(sourceValueRecord.getAsNumber());
        valueRecord.setUnitSymbol(sourceValueRecord.getUnitSymbol());
        
        IContextualDataDTO sourceContextualObject = sourceValueRecord.getContextualObject();
        if (!sourceContextualObject.isNull()) {
          IContextualDataDTO contextualObject = valueRecord.getContextualObject();
          contextualObject.setContextStartTime(sourceContextualObject.getContextStartTime());
          contextualObject.setContextEndTime(sourceContextualObject.getContextEndTime());
          contextualObject.setContextTagValues(sourceContextualObject.getContextTagValues().toArray(new ITagDTO[0]));
        }
        return true;
       }
        break;
      case TAGS:
        ITagsRecordDTO tagPropertyRecord = (ITagsRecordDTO) propertyRecord;
        ITagsRecordDTO originTagPropertyRecord = (ITagsRecordDTO) originPropertyRecord;
        if(isTagValueChanged(originTagPropertyRecord.getTags(), tagPropertyRecord.getTags())) {
          tagPropertyRecord.setTags(originTagPropertyRecord.getTags().toArray(new ITagDTO[0]));
          return true;
        }
        break;
      
      case RELATION_SIDE:
        propertyRecord.setChanged(true);
        IRelationsSetDTO relationshipPropertyRecord= (IRelationsSetDTO)propertyRecord;
        IRelationsSetDTO originRelationshipPropertyRecord= (IRelationsSetDTO)originPropertyRecord;
        Set<IEntityRelationDTO> targetRelations = relationshipPropertyRecord.getRelations();
        List<String> targetSide2Ids = new ArrayList<>();
        targetRelations.forEach(targetRelationRecord -> {
          targetSide2Ids.add(targetRelationRecord.getOtherSideEntityID());
        });
        
        Set<IEntityRelationDTO> sourceRelations = originRelationshipPropertyRecord.getRelations();
        List<String> sourceSide2Ids = new ArrayList<>();
        sourceRelations.forEach(sourceRelationRecord -> {
          sourceSide2Ids.add(sourceRelationRecord.getOtherSideEntityID());
        });
        
        if(targetSide2Ids.retainAll(sourceSide2Ids) ||  sourceSide2Ids.retainAll(targetSide2Ids)) {
          return true;
        }
       break;
       
      default:
        throw new RDBMSException(100, "Inconsistent Configuration", "Property super type should be one of attribute or tags");
    }
    return false;
  }

  private boolean isAttributeValuceChanged(IValueRecordDTO valueRecord,
      IValueRecordDTO sourceValueRecord)
  {
    boolean isValueChange = false;
    if(!sourceValueRecord.getValue().equals(valueRecord.getValue()) ||
       !sourceValueRecord.getAsHTML().equals(valueRecord.getAsHTML()) || isContextChanged(valueRecord, sourceValueRecord)) {
      isValueChange = true;
    }
    return isValueChange;
  }

  private boolean isContextChanged(IValueRecordDTO valueRecord, IValueRecordDTO sourceValueRecord)
  {
    IContextualDataDTO sourceContextualObject = sourceValueRecord.getContextualObject();
    if (!sourceContextualObject.isNull()) {
      IContextualDataDTO contextualObject = valueRecord.getContextualObject();

      Set<ITagDTO> sourceContextTagValues = sourceContextualObject.getContextTagValues();
      Set<ITagDTO> contextTagValues = contextualObject.getContextTagValues();

      if(!(sourceContextualObject.getContextStartTime() == contextualObject.getContextStartTime()) ||
          !(sourceContextualObject.getContextEndTime() == contextualObject.getContextEndTime()) ||
          isTagValueChanged(sourceContextTagValues, contextTagValues)) {
        return true;
      }
    }
    return false;
  }

private boolean isTagValueChanged(Set<ITagDTO> sourceTagValues, Set<ITagDTO> tagValues)
{
  if(sourceTagValues.size() != tagValues.size())
    return true;
  for (ITagDTO sourceTagValue : sourceTagValues) {
    boolean tagValueFound = false;
    for (ITagDTO tagValue : tagValues) {
      if (sourceTagValue.getTagValueCode().equals(tagValue.getTagValueCode())) {
        if (sourceTagValue.getRange() != tagValue.getRange()) {
          return true;
        }
        tagValueFound = true;
      }
    }
    if(!tagValueFound)
      return true;
  }
  return false;
}



  /**
   * Initialize a new target base entity from a source one for cloning or
   * transferring purpose
   *
   * @param clonedEntityID the ID of the new target base entity
   * @param originBaseEntityDAO the DAO of the source entity
   * @param isCloned
   * @param endpointCode
   * @return the DAO of the target entity
   * @throws RDBMSException
   * @throws CSFormatException
   */
  
  private IBaseEntityDAO openTargetBaseEntityDAO(String clonedEntityID, IBaseEntityDAO originBaseEntityDAO, boolean isCloned,
      String endpointCode) throws RDBMSException, CSFormatException
  {
    IBaseEntityDTO originBaseEntityDTO = originBaseEntityDAO.getBaseEntityDTO();
    
    IBaseEntityDTO targetBaseEntityDTO = localeCatalogDAO.newBaseEntityDTOBuilder(clonedEntityID, originBaseEntityDTO.getBaseType(),
        originBaseEntityDTO.getNatureClassifier()).endpointCode(endpointCode).isClone(isCloned).build();
    // originBaseEntityIID on newly cloned entity
    ((BaseEntityDTO) targetBaseEntityDTO).setOriginBaseEntityIID(originBaseEntityDTO.getBaseEntityIID());
    // set other ClassifierIIDs
    targetBaseEntityDTO.setOtherClassifierIIDs(originBaseEntityDAO.getClassifiers().toArray(new IClassifierDTO[0]));
    targetBaseEntityDTO.setEntityExtension(originBaseEntityDTO.getEntityExtension().toString());
    targetBaseEntityDTO.setDefaultImageIID(originBaseEntityDTO.getDefaultImageIID());
    // set contextual information
    ((ContextualDataDTO) targetBaseEntityDTO.getContextualObject()).setFrom(originBaseEntityDTO.getContextualObject());
    
    Set<IBaseEntityDTO> clonedContextualLinkedEntitites = originBaseEntityDAO.getContextualLinkedEntities();
    List<Long> contextualLinkedEntitites = new ArrayList<>();
    for (IBaseEntityDTO linkedEntities : clonedContextualLinkedEntitites) {
      contextualLinkedEntitites.add(linkedEntities.getBaseEntityIID());
    }
    ((ContextualDataDTO) targetBaseEntityDTO.getContextualObject()).getLinkedBaseEntityIIDs().addAll(contextualLinkedEntitites);
    return localeCatalogDAO.openBaseEntity(targetBaseEntityDTO);
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
            .localeID(originValueRecord.getLocaleID()).asHTML(originValueRecord.getAsHTML()).asNumber(originValueRecord.getAsNumber())
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
        IRelationsSetDTO relation = targetBaseEntityDAO.newEntityRelationsSetDTOBuilder(property, originRelationRecord.getSide()).build();
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
  /**
   * 
   * @param transferPlanDTO
   * @return
   */
  private Map<String, Object> getPartnerAuthMapping(ITransferPlanDTO transferPlanDTO)
  {
    Map<String, Object> authorizationMapping = null;
    if (!transferPlanDTO.getAuthorizationMappingId().equals("null")) {
      IPartnerAuthorizationDAO partnerAuthorizationDAO = new PartnerAuthorizationDAO();
      authorizationMapping = partnerAuthorizationDAO.getPartnerAuthMapping(transferPlanDTO.getAuthorizationMappingId(),
          transferPlanDTO.getLocaleID(),true);
    }
    return (authorizationMapping != null) ? (Map<String, Object>) authorizationMapping.get(PartnerAuthorizationUtils.ENTITY) : null;
  }
 
  /**
   * @param targetEntityDAO
   * @param classifiersToAdd
   * @param classifiersToRemove
   * @param sourceConfigDetails
   * @param relationRecordsToCreate 
   * @throws Exception
   */
  private void initiateBulkPropagation(IBaseEntityDAO targetEntityDAO, Set<IClassifierDTO> classifiersToAdd,
      Set<IClassifierDTO> classifiersToRemove, IGetConfigDetailsForCustomTabModel sourceConfigDetails,
      Set<IRelationsSetDTO> relationRecordsToCreate, List<Long> dependentPropertyIIDs, IBaseEntityDAO sourceEntityDAO) throws Exception
  {
    IGetKlassInstanceCustomTabModel ConflictAndCoupleTypeDetails = new GetKlassInstanceForCustomTabModel();
    ILocaleCatalogDAO targetLocaleCatalogDAO = targetEntityDAO.getLocaleCatalog();
    IBaseEntityDTO targetBaseEntityDTO = targetEntityDAO.getBaseEntityDTO();
    long baseEntityIID = targetBaseEntityDTO.getBaseEntityIID();
    
    // initiate runtime cleanup for type-switch
    if (!classifiersToRemove.isEmpty()) {
      dataTransferModelBuilder.setDataForRuntimeCleanup(targetEntityDAO,
          targetEntityDAO.getClassifiers().stream().collect(Collectors.toSet()), classifiersToRemove);
    }
    //Initiate classification data transfer
    if(!classifiersToAdd.isEmpty() || !classifiersToRemove.isEmpty()){
      IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();
      List<String> addedTaxonomyIds = classifiersToAdd.stream().map(IClassifierDTO::getCode)
          .collect(Collectors.toList());
      List<String> removedTaxonomyIds = classifiersToRemove.stream().map(IClassifierDTO::getCode)
          .collect(Collectors.toList());
      typeSwitchModel.setAddedTaxonomyIds(addedTaxonomyIds);
      typeSwitchModel.setDeletedTaxonomyIds(removedTaxonomyIds);
      dataTransferModelBuilder.initiateTaxonomyInheritanceTransferTypeSwitch(typeSwitchModel,
          sourceConfigDetails, targetEntityDAO.getBaseEntityDTO()
              .getBaseEntityIID(),
          targetEntityDAO);
     // Prepare Model & Initiate Relationship Inheritance Switch Type Event using
      dataTransferModelBuilder.prepareDataForRelationshipInheritanceSwitchType(targetEntityDAO,
          classifiersToAdd.stream().collect(Collectors.toList()), classifiersToRemove.stream().collect(Collectors.toList()),
          IApplicationTriggerModel.ApplicationActionType.TRANSFER);
      // Prepare Model & Initiate ClassificationDataTransfer using
      dataTransferModelBuilder.initiateClassificationDataTransfer(targetLocaleCatalogDAO, baseEntityIID, classifiersToAdd,
          classifiersToRemove);
      dataTransferModelBuilder.initiateLanguageInheritanceDataTransfer(targetLocaleCatalogDAO, baseEntityIID, dependentPropertyIIDs, true); 
    }
    //Initiate contextual data transfer
    dataTransferModelBuilder.contextualDataCoulpingAfterTransfer(baseEntityIID, targetLocaleCatalogDAO);
    
    //Initiate contextual data transfer
    List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
    List<IContentRelationshipInstanceModel> modifiedNatureRelationships = new ArrayList<>();
    // Prepare Conflict values using source and Transfer it to Target entity 
    transferRelationshipConflict(targetLocaleCatalogDAO, sourceEntityDAO, sourceConfigDetails, ConflictAndCoupleTypeDetails,
        targetEntityDAO);
    fillCreatedRelationships(sourceConfigDetails, relationRecordsToCreate,
        modifiedRelationships, modifiedNatureRelationships,targetEntityDAO);
    if (!modifiedRelationships.isEmpty() || !modifiedNatureRelationships.isEmpty()) {
      dataTransferModelBuilder.relationshipDataCoulpingAfterTransfer(targetEntityDAO,
          modifiedRelationships, modifiedNatureRelationships);
      dataTransferModelBuilder.prepareDataForTaxonomyInheritanceTransfer(modifiedNatureRelationships,baseEntityIID,
          sourceConfigDetails, targetEntityDAO);
    }
  }

  /**
   * @param configDetails
   * @param relations
   * @param modifiedRelationships
   * @param modifiedNatureRelationships
   * @throws Exception 
   */
  private void fillCreatedRelationships(IGetConfigDetailsForCustomTabModel configDetails, Set<IRelationsSetDTO> relations,
      List<IContentRelationshipInstanceModel> modifiedRelationships, List<IContentRelationshipInstanceModel> modifiedNatureRelationships,IBaseEntityDAO targetEntityDAO) throws Exception
  { 
    for (IRelationsSetDTO relationSet : relations) {
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel = new ArrayList<>();
      IContentRelationshipInstanceModel model = new ContentRelationshipInstanceModel();
      String relationshipId = relationSet.getProperty().getPropertyCode();
      model.setRelationshipId(relationshipId);
      IReferencedRelationshipModel referencedRelationship = configDetails.getReferencedRelationships().get(relationshipId);
      if (referencedRelationship == null) {
        referencedRelationship = configDetails.getReferencedNatureRelationships().get(relationshipId);
      }
      IRelationshipSide side = relationSet.getSide().equals(IPropertyDTO.RelationSide.SIDE_1) ?
          referencedRelationship.getSide1() :
          referencedRelationship.getSide2();
      model.setSideId(side.getElementId());
      for(IEntityRelationDTO relationship: relationSet.getRelations()){
        IRelationshipVersion relation = new RelationshipVersion();
        if ("SIDE_2".equals(relationSet.getSide().name()) && configDetails.getReferencedRelationships()
                .get(relationshipId).getIsNature()) {
          // If side 1 article is already present in destination then side 2 article is transferred then trigger the 
          // relationship data transfer and taxonomy inheritance from side1 article.
          triggerBulkPropagationFromSide1(configDetails, targetEntityDAO, relationshipId,
              referencedRelationship, relationship, relation);
        }
        else {
         relation.setId(String.valueOf(relationship.getOtherSideEntityIID()));
         relation.setContextId(String.valueOf(relationship.getContextualObject().getContext().getContextCode()));
         model.getAddedElements().add(relation);
        }
      }

      if (referencedRelationship.getIsNature()) {
        modifiedNatureRelationships.add(model);
        dataTransferModelBuilder.fillDataForRelationshipInheritanceForAddedAndModifiedElements(relationshipDataTransferModel, relationSet,
            !(model.getAddedElements()==null||model.getAddedElements().isEmpty())?model.getAddedElements():null, true);
        dataTransferModelBuilder.prepareDataForRelationshipInheritance(relationshipDataTransferModel, relationSet.getEntityIID(), false,
            IApplicationTriggerModel.ApplicationActionType.TRANSFER, targetEntityDAO);
        dataTransferModelBuilder.clearConflictIfExistOnTargetEntity(relationSet);
      }
      else {
        modifiedRelationships.add(model);
        dataTransferModelBuilder.fillDataForRelationshipInheritanceForAddedAndModifiedElements(relationshipDataTransferModel, relationSet,
            !(model.getAddedElements()==null||model.getAddedElements().isEmpty())?model.getAddedElements():null, false);
        dataTransferModelBuilder.prepareDataForRelationshipInheritance(relationshipDataTransferModel, relationSet.getEntityIID(), false,
            IApplicationTriggerModel.ApplicationActionType.TRANSFER, targetEntityDAO);
        dataTransferModelBuilder.clearConflictIfExistOnTargetEntity(relationSet);
      }
    }    
  }

  /**
   * @param sourceConfigDetails
   * @param targetCatalogDAO
   * @param targetEntityDAO
   * @throws RDBMSException
   * @throws CSFormatException
   */
  private void removeClassifierProperties(IGetConfigDetailsForCustomTabModel sourceConfigDetails,
      LocaleCatalogDAO targetCatalogDAO, IBaseEntityDAO targetEntityDAO) throws RDBMSException, CSFormatException
  {
    //Prepare config attribute and tag ids list
    Map<String, IAttribute> referencedAttributes = sourceConfigDetails.getReferencedAttributes();
    Map<String, ITag> referencedTags = sourceConfigDetails.getReferencedTags();
    Set<String> attributeAndTagIds = new HashSet<>();
    if (!MapUtils.isEmpty(referencedAttributes)) {
      attributeAndTagIds.addAll(referencedAttributes.keySet());
    }
    if (!MapUtils.isEmpty(referencedTags)) {
      attributeAndTagIds.addAll(referencedTags.keySet());
    }
    
    //Load all property records
    Set<IPropertyDTO> loadProperties = (Set<IPropertyDTO>) targetCatalogDAO
        .getAllEntityProperties(targetEntityDAO.getBaseEntityDTO().getBaseEntityIID());
    targetEntityDAO.loadPropertyRecords(loadProperties.toArray(new IPropertyDTO[0]));
    Set<IPropertyRecordDTO> propertyRecords = targetEntityDAO.getBaseEntityDTO().getPropertyRecords();
    
    List<IPropertyRecordDTO> propertyRecordsToDelete = new ArrayList<>();
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      if (!attributeAndTagIds.contains(propertyRecord.getProperty().getPropertyCode())) {
        propertyRecordsToDelete.add(propertyRecord);
      }
    }
    targetEntityDAO.deletePropertyRecords(propertyRecordsToDelete.toArray(new IPropertyRecordDTO[0]));
  }
  
  /**
   * If source has Conflicted Relationship
   * Transfer it to Target entity
   * @param localeCatalogDAO
   * @param sourceEntityDAO
   * @param sourceConfigDetails
   * @param ConflictAndCoupleTypeDetails
   * @param targetEntityDAO
   * @throws Exception
   */
  private void transferRelationshipConflict(ILocaleCatalogDAO localeCatalogDAO,
      IBaseEntityDAO sourceEntityDAO, IGetConfigDetailsForCustomTabModel sourceConfigDetails,
      IGetKlassInstanceCustomTabModel ConflictAndCoupleTypeDetails, IBaseEntityDAO targetEntityDAO) throws Exception
  {
    String catalogcode = targetEntityDAO.getBaseEntityDTO().getCatalog().getCatalogCode();
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(sourceEntityDAO, sourceConfigDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    ConflictAndCoupleTypeDetails.setKlassInstance((IContentInstance) klassInstance);
    klassInstanceUtils.getConflictAndCoupleType(sourceEntityDAO, ConflictAndCoupleTypeDetails, sourceConfigDetails);
    List<IRelationshipConflict> relationshipConflicts = ConflictAndCoupleTypeDetails.getKlassInstance().getRelationshipConflictingValues();
    relationshipConflicts.forEach(p -> {
      List<String> propagableRelationshipSideIds = new ArrayList<String>();
      propagableRelationshipSideIds.add(p.getPropagableRelationshipSideId());
      p.getConflicts().forEach(q -> {
        List<Long> targetEntity = new ArrayList<Long>();
        try {
          //Using SourceContentId as Origin fetch target record from Destination catalog in targetEntity
          getTargetEntityFromOriginBaseEntity(Long.valueOf(q.getSourceContentId()), catalogcode,targetEntity);
          if (!(targetEntity == null || targetEntity.isEmpty())) {
            Long propagableRelationshipIID = ConfigurationDAO.instance().getPropertyByCode(p.getPropagableRelationshipId())
                .getPropertyIID();
            Long natureRelationshipIID = ConfigurationDAO.instance().getPropertyByCode(q.getRelationshipId()).getPropertyIID();
            dataTransferModelBuilder.prepareConflictAndUpdateInCoupleTable(q.getCouplingType(), propagableRelationshipSideIds,
                propagableRelationshipIID, natureRelationshipIID, targetEntity.get(0),
                targetEntityDAO.getBaseEntityDTO().getBaseEntityIID(), localeCatalogDAO);
          }
        }
        catch (NumberFormatException | RDBMSException e) {
          RDBMSLogger.instance().warn("Cannot add/update relationcouplerecord for targetentityid : %d ",
              targetEntityDAO.getBaseEntityDTO().getBaseEntityIID());
        }
      });
    });
  }
  
  /**
   * Get Target entity in catalog 
   * using Origin base entity 
   * @param originBaseEntity
   * @param catalogcode
   * @param targetEntityIids
   * @return
   * @throws RDBMSException
   */
  private void getTargetEntityFromOriginBaseEntity(long originBaseEntity, String catalogcode, List<Long> targetEntityIids)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      BaseEntityDAS baseEntityDAS = new BaseEntityDAS(currentConn);
      Long target = baseEntityDAS.getTargetEntityFromOriginBaseEntity(originBaseEntity, catalogcode);
      if (target != 0L) {
        targetEntityIids.add(target);
      }
    });
  }
  
  /**
   * Trigger relationship data transfer and taxonomy inheritance from side 1 
   * @param configDetails
   * @param targetEntityDAO
   * @param relationshipId
   * @param referencedRelationship
   * @param relationship
   * @param relation
   * @throws RDBMSException
   * @throws CSFormatException
   * @throws Exception
   */
  private void triggerBulkPropagationFromSide1(IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO targetEntityDAO,
      String relationshipId, IReferencedRelationshipModel referencedRelationship,
      IEntityRelationDTO relationship, IRelationshipVersion relation)
      throws RDBMSException, CSFormatException, Exception
  {
    Long side1BaseEntityIID = relationship.getSideBaseEntityIID();
    ILocaleCatalogDAO targetLocalCatlog = targetEntityDAO.getLocaleCatalog();
    IBaseEntityDTO side1BaseEntityDTO = targetLocalCatlog.getEntityByIID(side1BaseEntityIID);
    IBaseEntityDAO side1BaseEtityDAO = targetLocalCatlog.openBaseEntity(side1BaseEntityDTO);
    Set<IPropertyDTO> properties = new HashSet<>();
    properties.addAll(targetLocalCatlog.getAllEntityProperties(side1BaseEntityDTO.getBaseEntityIID()));
    IPropertyDTO[] PropertyDTOArray = properties.stream().toArray(IPropertyDTO[]::new);
    side1BaseEtityDAO.loadPropertyRecords(PropertyDTOArray);
    for (IPropertyRecordDTO propertyRecord : side1BaseEntityDTO.getPropertyRecords()) {
      if (propertyRecord instanceof IRelationsSetDTO) {
        IRelationsSetDTO relationSetDTO = (IRelationsSetDTO) propertyRecord;
        IContentRelationshipInstanceModel relationshipContent = new ContentRelationshipInstanceModel();
        relationshipContent.setRelationshipId(relationshipId);
        relationshipContent.setSideId(referencedRelationship.getSide1().getElementId());
        for (IEntityRelationDTO sideEntityRelationDTO : relationSetDTO.getRelations()) {
          if (sideEntityRelationDTO.getOtherSideEntityIID() == targetEntityDAO.getBaseEntityDTO().getBaseEntityIID()) {
            relation.setId(String.valueOf(sideEntityRelationDTO.getOtherSideEntityIID()));
            relation.setContextId(String.valueOf(sideEntityRelationDTO.getContextualObject()
                .getContext().getContextCode()));
            relationshipContent.getAddedElements().add(relation);
          }
        }
        List<IContentRelationshipInstanceModel> modifiedNatureRelationship = new ArrayList<>(Arrays.asList(relationshipContent));
        dataTransferModelBuilder.relationshipDataCoulpingAfterTransfer(side1BaseEtityDAO,
            new ArrayList<>(), modifiedNatureRelationship);
        dataTransferModelBuilder.prepareDataForTaxonomyInheritanceTransfer(modifiedNatureRelationship, side1BaseEntityIID == null
            ? targetEntityDAO.getBaseEntityDTO().getBaseEntityIID(): side1BaseEntityIID,configDetails, side1BaseEtityDAO);
      }
    }
  }  
}
