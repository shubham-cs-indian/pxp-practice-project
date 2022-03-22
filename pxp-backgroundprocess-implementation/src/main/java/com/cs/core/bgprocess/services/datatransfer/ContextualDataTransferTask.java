package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IGetConfigDetailsForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeInfoModel;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.model.couplingtype.IPropertiesIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForContextualDataTransferStrategy;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ContextualDataTransferTask extends AbstractBGProcessJob implements IBGProcessJob{

  IContextualDataTransferDTO                        contextualDataTransferDTO = new ContextualDataTransferDTO();
  protected int                                     nbBatches                 = 1;
  protected int                                     batchSize;
  int                                               currentBatchNo            = 0;
  protected Set<IContextualDataTransferGranularDTO> passedBGPCouplingDTOs     = new HashSet<>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));

    contextualDataTransferDTO.fromJSON(jobData.getEntryData().toString());
    
    nbBatches = contextualDataTransferDTO.getBGPCouplingDTOs().size() / batchSize;
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
   
    currentBatchNo = currentBatchNo + 1;
    
    Set<IContextualDataTransferGranularDTO> currentCouplingDTOs = new HashSet<>(contextualDataTransferDTO.getBGPCouplingDTOs());
    currentCouplingDTOs.removeAll(passedBGPCouplingDTOs);
    
    Set<IContextualDataTransferGranularDTO> batchCouplingDTOs = new HashSet<>();
    Iterator<IContextualDataTransferGranularDTO> remEntityIID = currentCouplingDTOs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchCouplingDTOs.add(remEntityIID.next());
    }
    
    Set<Long> successVariantIIDs = new HashSet<Long>();
    
    for(IContextualDataTransferGranularDTO batchCouplingDTO : batchCouplingDTOs) {
      processContextualDataTransfer(batchCouplingDTO, successVariantIIDs);
    }
    
    passedBGPCouplingDTOs.addAll(batchCouplingDTOs);
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);
    
    if(successVariantIIDs.contains(new ArrayList<>(batchCouplingDTOs).get(0).getVariantBaseEntityIID())){
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate((new ArrayList<>(batchCouplingDTOs)).get(0).getVariantBaseEntityIID(), EventType.END_OF_TRANSACTION);
    }
    // Keep continuing as soon as the final number of batches has not been reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchCouplingDTOs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
    
  }
 
  public void processContextualDataTransfer(IContextualDataTransferGranularDTO batchCouplingDTO, Set<Long> successVariantIIDs) throws JsonProcessingException, RDBMSException, Exception {
    
    if(batchCouplingDTO.getVariantBaseEntityIID()!= 0) {
      createCoupledRecords(batchCouplingDTO, successVariantIIDs);
    }
    if(batchCouplingDTO.getDeletedVariantEntityIID()!= 0) {
      deleteCoupleRecordForContext(batchCouplingDTO);
    }
  }
  
  private void deleteCoupleRecordForContext(IContextualDataTransferGranularDTO dataTransferCouplingDTO) throws Exception
  {
    Long deletedContextEntity = dataTransferCouplingDTO.getDeletedVariantEntityIID();
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();

    ICouplingDAO couplingDAO = localeCatlogDAO.openCouplingDAO();
    List<Long> deletedEntities = new ArrayList<Long>();
    couplingDAO.getEntitiesForDelete(deletedContextEntity, deletedEntities);
    deletedEntities.add(deletedContextEntity);
    for (Long deleteId : deletedEntities) {
      couplingDAO.deleteCoupledRecord(deleteId, localeCatlogDAO);
    }
  }
  

  private void createCoupledRecords(IContextualDataTransferGranularDTO dataTransferCouplingDTO, Set<Long> successVariantIIDs) throws Exception, JsonProcessingException, RDBMSException
  {
      
      Long parentEntityIID = dataTransferCouplingDTO.getParentBaseEntityIID();
      Long variantEntityIID = dataTransferCouplingDTO.getVariantBaseEntityIID();
      
      ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
      
      List<String> parentEntityIIDs =  new ArrayList<String>();
      List<String> variantEntityIIDs =  new ArrayList<String>();
      parentEntityIIDs.add(parentEntityIID.toString());
      variantEntityIIDs.add(variantEntityIID.toString());
      
      List<IBaseEntityDTO> parentEntitiesDTOs = localeCatlogDAO.getBaseEntitiesByIIDs(parentEntityIIDs);
      List<IBaseEntityDTO> variantEntitiesDTOs = localeCatlogDAO.getBaseEntitiesByIIDs(variantEntityIIDs);
      
      if(parentEntitiesDTOs.isEmpty() || variantEntitiesDTOs.isEmpty()) {
        return;
      }
      
      IBaseEntityDAO parentEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(parentEntitiesDTOs.get(0));
      IBaseEntityDAO variantEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(variantEntitiesDTOs.get(0));
      
      ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
      String currentDataLangauge = rdbmsComponentUtils.getDataLanguage();
      long languageIID = configurationDAO.getLanguageConfig(currentDataLangauge).getLanguageIID();
      
      IGetConfigDetailsForContextualDataTransferModel configDetails = getConfigDetails(parentEntityDAO);
      
      Map<String, IPropertiesIdCodeCouplingTypeModel> contextualDataTransfer =  configDetails.getContextualDataTransfer();
      
      IPropertiesIdCodeCouplingTypeModel contextualTransfer = contextualDataTransfer.get(dataTransferCouplingDTO.getContextID());
      
      IConfigDetailsForBulkPropagationResponseModel parentBaseEntityConfigDetail = DataTransferUtil.getConfigDetailForBaseEntity(parentEntityDAO);
      IConfigDetailsForBulkPropagationResponseModel variantBaseEntityConfigDetail = DataTransferUtil.getConfigDetailForBaseEntity(variantEntityDAO);
      
      ICouplingDTO couplingDTO = DataTransferUtil.prepareCouplingDTO(parentEntityIID, variantEntityIID);
      couplingDTO.setCouplingSourceIID(configurationDAO.getClassifierByCode(dataTransferCouplingDTO.getContextID()).getClassifierIID());
      
      // Independent Properties PropertyRecord Instance To Create At Source and Target.
      Map<Long, RecordStatus> recordStatusInfo = new HashMap<>();
      List<IIdCodeCouplingTypeModel> applicableIndependentProperties = createPropertyRecordInstance(parentEntityDAO, 
          variantEntityDAO, contextualTransfer.getIndependentAttributes(), contextualTransfer.getTags(), 
          configurationDAO, 0l, parentBaseEntityConfigDetail, variantBaseEntityConfigDetail, couplingDTO,recordStatusInfo);
      
      // Dependent properties PropertyRecord Instance To Create At Source and Target. 
      List<IIdCodeCouplingTypeModel> applicableDependentProperties = createPropertyRecordInstance(parentEntityDAO, 
          variantEntityDAO, contextualTransfer.getDependentAttributes(), new ArrayList<>(), 
          configurationDAO, languageIID, parentBaseEntityConfigDetail, variantBaseEntityConfigDetail, couplingDTO, recordStatusInfo);
      
      ICouplingDAO couplingDAO = localeCatlogDAO.openCouplingDAO();
      //To create Coupling Records Instance for Independent Properties. 
      createCoupledRecordsForContextual(configurationDAO, applicableIndependentProperties, couplingDAO, couplingDTO, 0l, recordStatusInfo);
      //To create Coupling Records for Dependent Properties. 
      createCoupledRecordsForContextual(configurationDAO, applicableDependentProperties, couplingDAO, couplingDTO, languageIID, recordStatusInfo);
    
    RuleHandler ruleHandler = new RuleHandler();
    IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(variantBaseEntityConfigDetail.getReferencedElements(),
        variantBaseEntityConfigDetail.getReferencedAttributes(), variantBaseEntityConfigDetail.getReferencedTags());
    ruleHandler.evaluateDQMustShould(variantEntityIID, dataRules, variantEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO());
    
    successVariantIIDs.add(variantEntityIID);
  }

  private void createCoupledRecordsForContextual(ConfigurationDAO configurationDAO,
      List<IIdCodeCouplingTypeModel> dataTransferPropertiesForCoupling, ICouplingDAO couplingDAO, 
      ICouplingDTO couplingDTO, Long languageIID, Map<Long, RecordStatus> recordStatusInfo) throws Exception
  {
    couplingDTO.setLocaleIID(languageIID);
    List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist = couplingDAO.checkWheatherConflictingValuesAlreadyExist(couplingDTO);
    
    if(checkWheatherConflictingValuesAlreadyExist.size() == 0) {
      
      for(IIdCodeCouplingTypeModel dataTransferProperty : dataTransferPropertiesForCoupling) {
        
        if (CommonConstants.TIGHTLY_COUPLED.equalsIgnoreCase(dataTransferProperty.getCouplingType())) {
          couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
          couplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
        }
        else {
          couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
          couplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
        }
        long propertyIID = configurationDAO.getPropertyByCode(dataTransferProperty.getId()).getPropertyIID();
        RecordStatus recordStatus = recordStatusInfo.get(propertyIID);
        RecordStatus coupledRecord = recordStatus == null ? RecordStatus.UNDEFINED : recordStatus;
        couplingDTO.setRecordStatus(coupledRecord);
        couplingDTO.setPropertyIID(propertyIID);
        couplingDAO.createCoupledRecordForContextual(couplingDTO,rdbmsComponentUtils.getLocaleCatlogDAO());
      }
    }
  }

  private List<IIdCodeCouplingTypeModel> createPropertyRecordInstance(IBaseEntityDAO sourceBaseEntityDAO, IBaseEntityDAO targetBaseEntityDAO,
      List<IIdCodeCouplingTypeModel> attributesDataTransferProperties,
      List<IIdCodeCouplingTypeModel> tagsDataTransferProperties, ConfigurationDAO configurationDAO, 
      Long languageIID, IConfigDetailsForBulkPropagationResponseModel parentBaseEntityConfigDetail, 
      IConfigDetailsForBulkPropagationResponseModel variantBaseEntityConfigDetail, ICouplingDTO couplingDTO, Map<Long, RecordStatus> recordStatusInfo) throws RDBMSException, Exception
  {
    List<IPropertyRecordDTO> dataTransferProperties = new ArrayList<>();
    List<IIdCodeCouplingTypeModel> applicableProperties = new ArrayList<>();
    
    Map<String, IReferencedSectionElementModel> sourceReferencedElements = parentBaseEntityConfigDetail.getReferencedElements();
    Map<String, IReferencedSectionElementModel> targetReferencedElements = variantBaseEntityConfigDetail.getReferencedElements();
    
    for(IIdCodeCouplingTypeModel attributeDataTransfer : attributesDataTransferProperties) {
      IPropertyDTO propertyDTO = configurationDAO.getPropertyByCode(attributeDataTransfer.getId());
      
      IReferencedSectionAttributeModel sourceAttributeElement = 
          (IReferencedSectionAttributeModel) sourceReferencedElements.get(propertyDTO.getPropertyCode());
        
      IReferencedSectionAttributeModel targetAttributeElement = 
          (IReferencedSectionAttributeModel) targetReferencedElements.get(propertyDTO.getPropertyCode());
        
        
      if(sourceReferencedElements.containsKey(propertyDTO.getPropertyCode()) && 
          targetReferencedElements.containsKey(propertyDTO.getPropertyCode())) {
        
        if(sourceAttributeElement.getAttributeVariantContext() != null || 
            targetAttributeElement.getAttributeVariantContext() != null) {
          continue;
        }
        
        if(sourceAttributeElement.getIsSkipped() || 
            targetAttributeElement.getIsSkipped()) {
          continue;
        }
        
        couplingDTO.setLocaleIID(languageIID);
        couplingDTO.setPropertyIID(propertyDTO.getPropertyIID());
        
        List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO()
            .checkWheatherConflictingValuesAlreadyExist(couplingDTO);
        
        if(checkWheatherConflictingValuesAlreadyExist.size() == 0) {
          CouplingType couplingType = getCouplingContextType(attributeDataTransfer);
          dataTransferProperties.add(sourceBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, 
              "Value to be discarded").couplingType(couplingType).build());
          applicableProperties.add(attributeDataTransfer);
        }
      }
    }
    
    for(IIdCodeCouplingTypeModel tagsDataTransfer : tagsDataTransferProperties) {
      IPropertyDTO propertyDTO = configurationDAO.getPropertyByCode(tagsDataTransfer.getId());

      if(sourceReferencedElements.containsKey(propertyDTO.getPropertyCode()) && 
          targetReferencedElements.containsKey(propertyDTO.getPropertyCode())) {
        
        if(sourceReferencedElements.get(propertyDTO.getPropertyCode()).getIsSkipped() || 
            targetReferencedElements.get(propertyDTO.getPropertyCode()).getIsSkipped()) {
          continue;
        }
        
        couplingDTO.setLocaleIID(languageIID);
        couplingDTO.setPropertyIID(propertyDTO.getPropertyIID());
        
        List<ICouplingDTO> checkWheatherConflictingValuesAlreadyExist = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO()
            .checkWheatherConflictingValuesAlreadyExist(couplingDTO);
        
        if(checkWheatherConflictingValuesAlreadyExist.size() == 0) {
          CouplingType couplingType = getCouplingContextType(tagsDataTransfer);
          dataTransferProperties.add(sourceBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).couplingType(couplingType).build());
          applicableProperties.add(tagsDataTransfer);
        }
      }
    }
    
    DataTransferUtil.createPropertyRecordsForCoupling(targetBaseEntityDAO, dataTransferProperties, 
        sourceBaseEntityDAO, languageIID, parentBaseEntityConfigDetail, variantBaseEntityConfigDetail,
        new ArrayList<>(), couplingDTO, recordStatusInfo);
    
    return applicableProperties;
  }

  private CouplingType getCouplingContextType(IIdCodeCouplingTypeModel attributeDataTransfer)
  {
    if(CommonConstants.TIGHTLY_COUPLED.equalsIgnoreCase(attributeDataTransfer.getCouplingType())) {
      return CouplingType.TIGHT_CONTEXTUAL;
    }else {
      return CouplingType.DYN_CONTEXTUAL;
    }
  }
  
  public IGetConfigDetailsForContextualDataTransferModel getConfigDetails(IBaseEntityDAO baseEntityDAO) throws JsonProcessingException, Exception {
    
    IKlassInstanceTypeInfoModel klassInstanceTypeModel = new KlassInstanceTypeInfoModel();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
    klassInstanceTypeModel.setKlassIds(types);
    List<String> taxonomyIds = new ArrayList<>();
    
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    for(IClassifierDTO classifier : classifiers) {
      if(classifier.getClassifierType().equals(ClassifierType.CLASS)){
        types.add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
    klassInstanceTypeModel.setTaxonomyIds(taxonomyIds);
    
    List<String> languageCodes = new ArrayList<>(); 
    languageCodes.add(baseEntityDTO.getBaseLocaleID());
    klassInstanceTypeModel.setLanguageCodes(languageCodes);
      
    IGetConfigDetailsForContextualDataTransferStrategy configDetailForContextualDataTransfer = BGProcessApplication.getApplicationContext().
        getBean(IGetConfigDetailsForContextualDataTransferStrategy.class);
    
    IGetConfigDetailsForContextualDataTransferModel configDetails = configDetailForContextualDataTransfer.execute(klassInstanceTypeModel);
    
    return configDetails;
  }
  
}
