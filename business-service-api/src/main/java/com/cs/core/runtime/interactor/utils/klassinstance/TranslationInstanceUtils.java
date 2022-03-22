package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.*;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.*;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.configuration.CreationLanguageInstanceDeleteException;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TranslationInstanceUtils {
 
  @Autowired
  protected ConfigUtil                                  configUtil;
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  RelationshipInstanceUtil                              relationshipInstanceUtil;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected TransactionThreadData                       transactionThread;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  private static final String SERVICE_FOR_RDT = "RELATIONSHIP_DATA_TRANSFER";
  private static final String SERVICE_FOR_CDT = "CONTEXTUAL_DATA_TRANSFER_TASK";
  private static final String SERVICE_FOR_LANG_INHERITANCE = "LANGUAGE_INHERITANCE_TASK";
  private static final String SERVICE_FOR_CLASSIFICATION_DT = "CLASSIFICATION_DATA_TRANSFER";
  
  
  /*********************** CREATE TRANSLATION HANDLING **********************/

  public IMulticlassificationRequestModel handleTranslationCreation(IKlassInstanceSaveModel klassInstancesModel, Long counter) throws Exception {
    
    long starTime = System.currentTimeMillis();
    
    List<String> languageInheritance = configUtil.getLanguageInheritance();
    String languageCode = languageInheritance.get(0);
    long baseEntityIID = Long.parseLong(klassInstancesModel.getId());
    IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, languageInheritance);
    List<String> languageCodes = klassInstancesModel.getLanguageCodes();
    RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|executeInternal|getEntityByIID| %d ms",
        System.currentTimeMillis() - starTime);
 
    IMulticlassificationRequestModel multiclassificationRequestModel = this.configUtil.getConfigRequestModelForSaveInstance(klassInstancesModel.getTemplateId(), klassInstancesModel.getTabId(), klassInstancesModel.getTypeId(),
        languageCodes, baseEntityDAO);
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(baseEntityIID);
    String classifierCode = baseEntityDTO.getNatureClassifier().getClassifierCode();
    IGetConfigDetailsModel configDetails = getConfigDetailsForSave(multiclassificationRequestModel);
    String newInstanceName = klassInstancesModel.getName();
    String existingLanguageCode ="";
    if (newInstanceName == null) {
      String baseEntityName = baseEntityDTO.getBaseEntityName();
      if(baseEntityName.contains("_") && baseEntityName.length() > 6) {
       existingLanguageCode = baseEntityName.substring(baseEntityName.substring(0, baseEntityName.lastIndexOf("_")).lastIndexOf("_")+1);
      }
      if ((baseEntityDTO.getLocaleIds()).contains(existingLanguageCode)) {
        newInstanceName = baseEntityName.replace(existingLanguageCode, languageCode);
      }
      else if (baseEntityDTO.getEmbeddedType().name() == "UNDEFINED") {
        newInstanceName = baseEntityName + "_" + languageCode;
      }
      else {
        newInstanceName = baseEntityName + "_context_" + languageCode;
      }
      klassInstancesModel.setName(newInstanceName);
    }
    
    IPropertyRecordDTO[] propertyRecords = createPropertyRecordInstance(baseEntityDAO, (IGetConfigDetailsForCustomTabModel) configDetails, klassInstancesModel.getName());
    
    baseEntityDAO.createPropertyRecords(propertyRecords);
    if(!baseEntityDTO.getLocaleIds().contains(baseEntityDAO.getLocaleCatalog().getLocaleCatalogDTO().getLocaleID())) {
      baseEntityDAO.createLanguageTranslation();
    }
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(), false,
        configDetails.getReferencedElements(), configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    multiclassificationRequestModel = this.configUtil.getConfigRequestModelForTab(klassInstancesModel, baseEntityDAO);
    multiclassificationRequestModel.getLanguageCodes().add(transactionThread.getTransactionData().getDataLanguage());
    
    List<Long> dependentPropertyIIDs = new ArrayList<>();
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    for (IAttribute referencedAttribute : referencedAttributes.values()) {
      if (referencedAttribute.getIsTranslatable()) {
        dependentPropertyIIDs.add(referencedAttribute.getPropertyIID());
      }
    }
    
    ILanguageInheritanceDTO languageInheritanceDTO = new LanguageInheritanceDTO();
    languageInheritanceDTO.setDependentPropertyIIDs(dependentPropertyIIDs);
    languageInheritanceDTO.setBaseEntityIID(baseEntityIID);
    languageInheritanceDTO.getLocaleIIDs().add(transactionThread.getTransactionData().getDataLanguage());
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    languageInheritanceDTO.setLocaleID(localeCatlogDAO.getLocaleCatalogDTO().getLocaleID());
    languageInheritanceDTO.setCatalogCode(localeCatlogDAO.getLocaleCatalogDTO().getCatalogCode());
    languageInheritanceDTO.setOrganizationCode(localeCatlogDAO.getLocaleCatalogDTO().getOrganizationCode());
    languageInheritanceDTO.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_LANG_INHERITANCE, "", userPriority,
        new JSONContent(languageInheritanceDTO.toJSON()));
    
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();

    initiateClassificationDataTransfer(localeCatlogDAO, baseEntityIID);

    prepareDataForRelationshipDataTransferTask(baseEntityIID, couplingDAO);
    
    prepareDataForContextualDataTransferTask(baseEntityIID, couplingDAO);
    
    return multiclassificationRequestModel;
  }
  
  protected IGetConfigDetailsForCustomTabModel getConfigDetailsForSave(IMulticlassificationRequestModel multiclassificationRequestModel)
      throws Exception
  {
    return getConfigDetailsWithoutPermissionsStrategy.execute(multiclassificationRequestModel);
  }
  
  private void prepareDataForContextualDataTransferTask(long baseEntityIID, ICouplingDAO couplingDAO)
      throws RDBMSException, Exception, CSFormatException
  {
    List<ICouplingDTO> otherSideEntitesForContextual = couplingDAO.getOtherSideEntitesForContextual(baseEntityIID);
    List<IContextualDataTransferGranularDTO> bgpCouplingDTOs = new ArrayList<>();
    for(ICouplingDTO contextualInstance : otherSideEntitesForContextual) {
      
      List<String> getDataLanguages = couplingDAO.getlanguageCodesByBaseEntityIIDs(contextualInstance.getSourceEntityIID(), 
          contextualInstance.getTargetEntityIID());
      if(getDataLanguages.contains(rdbmsComponentUtils.getDataLanguage())) {
        IContextualDataTransferGranularDTO dataTransferDTO = new ContextualDataTransferGranularDTO();
        dataTransferDTO.setParentBaseEntityIID(contextualInstance.getSourceEntityIID());
        dataTransferDTO.setVariantBaseEntityIID(contextualInstance.getTargetEntityIID());
        String contextID = ConfigurationDAO.instance().getClassifierByIID(contextualInstance.getCouplingSourceIID()).getClassifierCode();
        dataTransferDTO.setContextID(contextID);
        bgpCouplingDTOs.add(dataTransferDTO);
      }
    }
    
    if(!bgpCouplingDTOs.isEmpty()) {
      IContextualDataTransferDTO dataTransfer = new ContextualDataTransferDTO();
      IBGProcessDTO.BGPPriority userPriority3 = IBGProcessDTO.BGPPriority.HIGH;
      dataTransfer.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
      dataTransfer.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
      dataTransfer.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
      dataTransfer.setUserId(transactionThread.getTransactionData().getUserId());

      dataTransfer.setBGPCouplingDTOs(bgpCouplingDTOs);
      
      BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CDT, "", userPriority3,
          new JSONContent(dataTransfer.toJSON()));
    }
  }

  private void initiateClassificationDataTransfer(ILocaleCatalogDAO localeCatlogDAO, Long baseEntityIID) throws Exception
  {
    IClassificationDataTransferDTO classificationDataTransfer = new ClassificationDataTransferDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    classificationDataTransfer.setLocaleID(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setCatalogCode(localeCatalogDTO.getCatalogCode());
    classificationDataTransfer.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    classificationDataTransfer.setUserId(transactionThread.getTransactionData().getUserId());
    classificationDataTransfer.setBaseEntityIID(baseEntityIID);
    classificationDataTransfer.getAddedTranslations().add(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setTranslationChanged(true);

    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CLASSIFICATION_DT, "", BGPPriority.HIGH,
        new JSONContent(classificationDataTransfer.toJSON()));
  }

  
  private void prepareDataForRelationshipDataTransferTask(long baseEntityIID, ICouplingDAO couplingDAO) throws RDBMSException, Exception, CSFormatException
  {
    
    Map<String, IBGPCouplingDTO> otherSideEntitiesFromRelationship = couplingDAO.getOtherSideEntitesFromRelationship(baseEntityIID);
    
    if(otherSideEntitiesFromRelationship.isEmpty()) {
      return;
    }
    
    List<IBGPCouplingDTO> bgpCouplingDTOs = new ArrayList<>();
    for(String propertyIID : otherSideEntitiesFromRelationship.keySet()) {
      bgpCouplingDTOs.add(otherSideEntitiesFromRelationship.get(propertyIID));
    }
    
    IBGProcessDTO.BGPPriority userPriority1 = IBGProcessDTO.BGPPriority.HIGH;
    IRelationshipDataTransferDTO entryData = new RelationshipDataTransferDTO();
    entryData.setBGPCouplingDTOs(bgpCouplingDTOs);
    entryData.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    entryData.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    entryData.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    entryData.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_RDT, "", userPriority1, new JSONContent(entryData.toJSON()));
  }
  
  protected IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails, String contentName) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.DEFAULT,
        rdbmsComponentUtils.getLocaleCatlogDAO());
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = this.createAttributePropertyRecordInstance(propertyRecordBuilder, configDetails,
        contentName);
    
    return propertyRecords.toArray(new IPropertyRecordDTO[propertyRecords.size()]);
  }
  
  protected List<IPropertyRecordDTO> createAttributePropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsForCustomTabModel configDetails, String contentName) throws Exception
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    configDetails.getReferencedAttributes().values().forEach(referencedAttribute -> {
      try {
        IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
        if (dto != null) {
          attributePropertyRecords.add(dto);
        }
      }
      catch (Exception e) {
        new RuntimeException(e);
      }
    });
    addMandatoryAttribute(attributePropertyRecords, propertyRecordBuilder, configDetails, contentName);
    return attributePropertyRecords;
  }
  
  protected void addMandatoryAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO, PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsForCustomTabModel configDetails, String contentName) throws Exception
  {
    addNameAttribute(listOfPropertyRecordsDTO, propertyRecordBuilder, contentName);
  }
  
  protected void addNameAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO, PropertyRecordBuilder propertyRecordBuilder,
      String name) throws Exception
  {
    IPropertyRecordDTO nameAttribute = propertyRecordBuilder.createStandardNameAttribute(name);
    listOfPropertyRecordsDTO.add(nameAttribute);
  }
  
  
  /*********************** DELETE TRANSLATION HANDLING **********************/
  
  public void handleTranslationDelete(String localeIdToDelete,
      List<String> successIds, IExceptionModel exception,
      IDeleteTranslationResponseModel responseModel, IBaseEntityDTO baseEntityDTO)
      throws CreationLanguageInstanceDeleteException, Exception, RDBMSException
  {
    long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    String baseLocaleID = baseEntityDTO.getBaseLocaleID();
    
    if (localeIdToDelete.equals(baseLocaleID)) {
      throw new CreationLanguageInstanceDeleteException();
    }
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    try {
      
      deleteCouplingRecordsForDataTransfer(localeIdToDelete, baseEntityIID, couplingDAO);
      deleteNotificationFromChildLanguage(localeIdToDelete, baseEntityIID, couplingDAO);
      baseEntityDAO.deleteLanguageTranslation(baseEntityIID, localeIdToDelete);
      successIds.add(localeIdToDelete);
      responseModel.setSuccess(successIds);
    }
    
    catch (Exception e) {
      ExceptionUtil.addFailureDetailsToFailureObject(exception, e, String.valueOf(baseEntityIID),
          "Language Translation cannot be deleted for " + baseEntityDTO.getBaseEntityName());
      responseModel.setFailure(exception);
    }
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    localeCatalogDAO.getLocaleCatalogDTO().setLocaleID(localeIdToDelete);
    localeCatalogDAO.getLocaleCatalogDTO().setLocaleInheritanceSchema(List.of(localeIdToDelete));
    localeCatalogDAO.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_TRANSLATION_DELETE);
  }
  
  private void deleteCouplingRecordsForDataTransfer(String localeIdToDelete, long baseEntityIID, ICouplingDAO couplingDAO)
      throws RDBMSException, Exception
  {
    long languageIID = ConfigurationDAO.instance().getLanguageConfig(localeIdToDelete).getLanguageIID();
    List<ICouplingDTO> targetConflictingValues = couplingDAO.getTargetConflictingValues(baseEntityIID, 
        CouplingType.LANG_INHERITANCE, RecordStatus.COUPLED, languageIID);
    
    for(ICouplingDTO targetConflictingValue : targetConflictingValues) {
      
      createPropertyRecord(targetConflictingValue.getSourceEntityIID(), targetConflictingValue.getTargetEntityIID(), 
          targetConflictingValue.getPropertyIID(), targetConflictingValue.getLocaleIID());
    }
    
    couplingDAO.deleteCouplingRecordsForTranslationDelete(baseEntityIID, languageIID);
  }
  
  private void deleteNotificationFromChildLanguage(String localeIdToDelete, long baseEntityIID, ICouplingDAO couplingDAO)
      throws RDBMSException, Exception, CSFormatException
  {
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    Long localeIID = configurationDAO.getLanguageConfig(localeIdToDelete).getLanguageIID();
    List<ICouplingDTO> allTargetCouplingDTOs = couplingDAO.getAllTargetLocaleIIDs(baseEntityIID, localeIID, RecordStatus.COUPLED);
    
    IBaseEntityDAO sourceBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, localeIdToDelete);
    for (ICouplingDTO targetCouplingDTO : allTargetCouplingDTOs) {
      
      IPropertyDTO propertyDTO = configurationDAO.getPropertyByIID(targetCouplingDTO.getPropertyIID());
      IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
      IValueRecordDTO sourceValueRecord = (IValueRecordDTO) sourceBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
      String targetLocaleCode = configurationDAO.getLanguageConfigByLanguageIID(targetCouplingDTO.getLocaleIID()).getLanguageCode();
      
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, targetLocaleCode);
      IValueRecordDTO targetValueRecord = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, sourceValueRecord.getValue())
          .localeID(targetLocaleCode).build();
      targetBaseEntityDAO.createPropertyRecords(targetValueRecord);
      couplingDAO.deleteCoupledRecordByLocaleIIDAndPropertyIID(targetCouplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
    }
    
    ICouplingDTO couplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
    couplingDTO.setSourceEntityIID(baseEntityIID);
    couplingDTO.setTargetEntityIID(baseEntityIID);
    couplingDTO.setLocaleIID(localeIID);
    couplingDTO.setCouplingSourceIID(localeIID);
    couplingDTO.setCouplingSourceType(CouplingType.LANG_INHERITANCE);
    
    couplingDAO.deleteCoupledRecordByLocaleIID(couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
    couplingDAO.deleteConflictingValuesByLocaleIIds(couplingDTO);
  }
  
  public void createPropertyRecord(long sourceEntityIID, long targetEntityIID, long propertyIID, long localeIID) throws Exception
  {
    String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode();
    IBaseEntityDAO sourceEntityDAO = getBaseEntityDAO(sourceEntityIID, languageCode);
    LocaleCatalogDAO localeCatlogDAO = (LocaleCatalogDAO) rdbmsComponentUtils.getLocaleCatlogDAO();
    IPropertyDTO propertyDTO = localeCatlogDAO.getPropertyByIID(propertyIID);
    IBaseEntityDTO sourceBaseEntityDTO = sourceEntityDAO.loadPropertyRecords(propertyDTO);
    IPropertyRecordDTO propertyRecord = sourceBaseEntityDTO.getPropertyRecord(propertyIID);
    
    IBaseEntityDAO targetBaseEntityDAO = getBaseEntityDAO(targetEntityIID, languageCode);
    if (propertyRecord instanceof IValueRecordDTO) {
      IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
      
      if(languageCode.equals(null)) {
        IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue()).build();
        targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
      }else {
        IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue())
            .localeID(languageCode)
            .build();
        targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
      }
    }
    else {
      ITagsRecordDTO tagRecord = (ITagsRecordDTO) propertyRecord;
      ITagsRecordDTO newTagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
      Set<ITagDTO> newTagsDTO = tagRecord.getTags();
      newTagRecordDTO.setTags(newTagsDTO.toArray(new ITagDTO[newTagsDTO.size()]));
      targetBaseEntityDAO.createPropertyRecords(newTagRecordDTO);
    }
  }
  
  public IBaseEntityDAO getBaseEntityDAO(long entityIID, String localeCode) throws Exception
  {
    
    if (localeCode.equals(null)) {
      return rdbmsComponentUtils.getBaseEntityDAO(entityIID);
    }
    return rdbmsComponentUtils.getBaseEntityDAO(entityIID, localeCode);
  }
}
