package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.ListOfContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.IListOfContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
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

public class ContextualDataTransferOnConfigChangeTask extends AbstractBGProcessJob implements IBGProcessJob {
  
  IListOfContextualDataTransferOnConfigChangeDTO                contextualDataTransferDTOs = new ListOfContextualDataTransferOnConfigChangeDTO();
  protected int                                                 nbBatches                  = 1;
  protected int                                                 batchSize;
  int                                                           currentBatchNo             = 0;
  protected Set<IContextualDataTransferOnConfigChangeDTO> passedBGPCouplingDTOs      = new HashSet<>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    
    contextualDataTransferDTOs.fromJSON(jobData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    currentBatchNo = currentBatchNo + 1;
    
    Set<IContextualDataTransferOnConfigChangeDTO> currentCouplingDTOs = new HashSet<>();
    currentCouplingDTOs.addAll(contextualDataTransferDTOs.getContextualDataTransferOnConfigChangeDTOs());
    currentCouplingDTOs.removeAll(passedBGPCouplingDTOs);
    
    Set<IContextualDataTransferOnConfigChangeDTO> batchCouplingDTOs = new HashSet<>();
    Iterator<IContextualDataTransferOnConfigChangeDTO> remEntityIID = currentCouplingDTOs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchCouplingDTOs.add(remEntityIID.next());
    }
    
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    
    for (IContextualDataTransferOnConfigChangeDTO contextualDataTransferDTO : batchCouplingDTOs) {
      
      List<IModifiedPropertiesCouplingDTO> addedProperties = contextualDataTransferDTO.getAddedProperties();
      List<IModifiedPropertiesCouplingDTO> modifiedProperties = contextualDataTransferDTO.getModifiedProperties();
      List<Long> deleteProperties = contextualDataTransferDTO.getDeletedPropertyIIDs();
      
      if (!addedProperties.isEmpty()) {
        addDataTransferProperties(configurationDAO, contextualDataTransferDTO, addedProperties);
      }
      
      if(!modifiedProperties.isEmpty()) {
        modifiedDataTransferProperties(contextualDataTransferDTO, modifiedProperties);
      }
      
      if(!deleteProperties.isEmpty()) {
        
        deleteDataTransferProperties(contextualDataTransferDTO, deleteProperties);
      }
    }
    
    passedBGPCouplingDTOs.addAll(batchCouplingDTOs);
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);
    
    if (currentBatchNo < nbBatches && !batchCouplingDTOs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }

  private void deleteDataTransferProperties(IContextualDataTransferOnConfigChangeDTO contextualDataTransferDTO, List<Long> deleteProperties)
      throws Exception, RDBMSException
  {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    ICouplingDAO couplingDAO = localeCatlogDAO.openCouplingDAO();
    Long contextualIID = contextualDataTransferDTO.getContextualIID();
    
    for(Long deletedProperty : deleteProperties) {
      List<ICouplingDTO> sourceAndTargetEntities = couplingDAO.getConflictingValuesByCouplingSourceIIDAndPropertyIID(contextualIID, deletedProperty);
      
      for(ICouplingDTO sourceAndTargetDTO : sourceAndTargetEntities) {
        if(sourceAndTargetDTO.getRecordStatus() == RecordStatus.COUPLED) {
          
          createPropertyRecord(sourceAndTargetDTO.getSourceEntityIID(), sourceAndTargetDTO.getTargetEntityIID(), 
              sourceAndTargetDTO.getPropertyIID(), sourceAndTargetDTO.getLocaleIID());
          
          couplingDAO.deleteCoupledRecord(sourceAndTargetDTO.getSourceEntityIID(), sourceAndTargetDTO.getTargetEntityIID(),
              sourceAndTargetDTO.getPropertyIID(), sourceAndTargetDTO.getLocaleIID(), localeCatlogDAO);
        }
        couplingDAO.deleteCouplingRecordFromConflictingValues(sourceAndTargetDTO);
      }
    }
  }

  private void modifiedDataTransferProperties(IContextualDataTransferOnConfigChangeDTO contextualDataTransferDTO,
      List<IModifiedPropertiesCouplingDTO> modifiedProperties) throws Exception, RDBMSException
  {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    ICouplingDAO couplingDAO = localeCatlogDAO.openCouplingDAO();
    
    Long contextualIID = contextualDataTransferDTO.getContextualIID();
    
    ICouplingDTO couplingDTOToRaiseEvents = null;
    
    Set<Long> consolidatedTargetEntityIIds = new HashSet<>();
    for(IModifiedPropertiesCouplingDTO modifiedProperty : modifiedProperties) {
      
      List<ICouplingDTO> targetCouplingDTOs = couplingDAO.
          getConflictingValuesByCouplingSourceIIDAndPropertyIID(contextualIID, modifiedProperty.getPropertyIID());
      
      if(!targetCouplingDTOs.isEmpty()) {
        couplingDTOToRaiseEvents = targetCouplingDTOs.get(0);
      }
      CouplingBehavior couplingBehavior = CouplingBehavior.valueOf(modifiedProperty.getCouplingType()) == 
          CouplingBehavior.DYNAMIC ? CouplingBehavior.TIGHTLY : CouplingBehavior.DYNAMIC;
      
      for(ICouplingDTO targetCouplingDTO : targetCouplingDTOs) {
        
        if(couplingBehavior == CouplingBehavior.DYNAMIC) {
          RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
          
          switch(recordStatus) {
            
            case COUPLED:
              configModificationForDynamicCoupledRecord(localeCatlogDAO, couplingDAO, modifiedProperty.getPropertyIID(), 
                  contextualIID, targetCouplingDTO);
              break;
              
            case FORKED:
              configModificationForDynamicForkedRecord(couplingDAO, targetCouplingDTO);
              break;
              
            case NOTIFIED:
              configModificationForDynamicNotifiedRecord(couplingDAO, targetCouplingDTO);
              break;
              
            default:
              break;
          }
        }else {
          RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
          
          switch(recordStatus) {
            
            case COUPLED:
              configModificationForTightlyCoupledRecord(couplingDAO, targetCouplingDTO);
              break;
              
            case FORKED:
              configModificationForTightlyForkedRecord(couplingDAO, targetCouplingDTO);
              break;
              
            case NOTIFIED:
              configModificationForTightlyNotifiedRecord(couplingDAO, targetCouplingDTO);
              break;
            default:
              break;
          }
        }
        consolidatedTargetEntityIIds.add(targetCouplingDTO.getTargetEntityIID());
        consolidatedTargetEntityIIds.add(targetCouplingDTO.getSourceEntityIID());
      }
    }
    
    for(Long targetEntityIID : consolidatedTargetEntityIIds) {
      IBaseEntityDAO targetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetEntityIID);
      IConfigDetailsForBulkPropagationResponseModel configDetails = DataTransferUtil.getConfigDetailForBaseEntity(targetEntityDAO);
      
      RuleHandler ruleHandler = new RuleHandler();
      IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(configDetails.getReferencedElements(),
          configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
      ruleHandler.evaluateDQMustShould(targetEntityIID, dataRules, targetEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO());
      
    }
    
    if(couplingDTOToRaiseEvents != null) {
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(couplingDTOToRaiseEvents.getSourceEntityIID(), 
          IEventDTO.EventType.END_OF_TRANSACTION);
    }
  }
  
  
  private void configModificationForTightlyNotifiedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws CSFormatException, Exception
  {
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
    
    for (ICouplingDTO sourceValues : sourceConflictingValues) {
      couplingDAO.updateRecordStatusForConflictingValues(sourceValues, RecordStatus.FORKED);
    }
    
    targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
    targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
    //couplingDAO.resolvedConflicts(targetCouplingDTO);
    
    resolveConflictsAndRaiseEventsForWorkFlow(rdbmsComponentUtils.getLocaleCatlogDAO(), couplingDAO, 
        targetCouplingDTO.getPropertyIID(), targetCouplingDTO);
    
    deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
  }
  
  
  private void configModificationForTightlyForkedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws Exception
  {
    
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    
    if (sourceConflictingValues.size() > 0) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
      
    }
    else {
      
      List<ICouplingDTO> sourceTightlyConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO,
          CouplingBehavior.TIGHTLY);
      
      for (ICouplingDTO sourceValues : sourceTightlyConflictingValues) {
        
        couplingDAO.updateRecordStatusForConflictingValues(sourceValues, RecordStatus.FORKED);
        couplingDAO.deleteCoupledRecord(sourceValues.getSourceEntityIID(), sourceValues.getTargetEntityIID(),
            sourceValues.getPropertyIID(), sourceValues.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
      }
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
      //couplingDAO.resolvedConflicts(targetCouplingDTO);
      
      resolveConflictsAndRaiseEventsForWorkFlow(rdbmsComponentUtils.getLocaleCatlogDAO(), couplingDAO, 
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO);
      
      deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
    }
  }
  
  private void configModificationForTightlyCoupledRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO)
      throws RDBMSException, Exception
  {
    targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
    targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
    couplingDAO.updateCouplingTypeForCoupledRecord(targetCouplingDTO, RecordStatus.COUPLED);
  }
  
  private void configModificationForDynamicNotifiedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws RDBMSException
  {
    
    targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
    targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
    
  }
  
  private void configModificationForDynamicForkedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO) throws RDBMSException
  {
    targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
    targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
  }
  
  private void configModificationForDynamicCoupledRecord(ILocaleCatalogDAO localeCatlogDAO, ICouplingDAO couplingDAO, long propertyIID,
      long relationshipIID, ICouplingDTO targetCouplingDTO) throws RDBMSException, Exception
  {
    
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    
    if (sourceConflictingValues.size() == 1) {
      
      couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatlogDAO);
      
      resolveConflictsAndRaiseEventsForWorkFlow(localeCatlogDAO, couplingDAO, propertyIID, sourceConflictingValues.get(0));
      //couplingDAO.resolvedConflicts(sourceConflictingValues.get(0));
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
      
    }
    else if (sourceConflictingValues.size() > 1) {
      
      couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatlogDAO);
      
      createPropertyRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID());
      
      for (ICouplingDTO sourceCouplingDTO : sourceConflictingValues) {
        couplingDAO.updateRecordStatusForConflictingValues(sourceCouplingDTO, RecordStatus.NOTIFIED);
      }
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
      couplingDAO.updateRecordStatusForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
      
    }
    else if (sourceConflictingValues.size() == 0) {
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
      
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
      couplingDAO.updateCouplingTypeForCoupledRecord(targetCouplingDTO, RecordStatus.COUPLED);
    }
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
  
  private void deletePropertyRecordFromTargetBaseEntity(ICouplingDTO targetCouplingDTO) throws Exception, RDBMSException, CSFormatException
  {
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    
    if(targetCouplingDTO.getLocaleIID() == 0l) {
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetCouplingDTO.getTargetEntityIID());
      IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(ConfigurationDAO.instance().getPropertyByIID(targetCouplingDTO.getPropertyIID()));
      couplingDAO.deletePropertyRecord(targetBaseEntityDTO.getPropertyRecord(targetCouplingDTO.getPropertyIID()));
    }else {
      String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(targetCouplingDTO.getLocaleIID()).getLanguageCode();
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(targetCouplingDTO.getTargetEntityIID(), languageCode);
      IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(ConfigurationDAO.instance().getPropertyByIID(targetCouplingDTO.getPropertyIID()));
      couplingDAO.deletePropertyRecord(targetBaseEntityDTO.getPropertyRecord(targetCouplingDTO.getPropertyIID()));
    }
  }
  
  public IBaseEntityDAO getBaseEntityDAO(long entityIID, String localeCode) throws Exception
  {
    
    if (localeCode.equals(null)) {
      return rdbmsComponentUtils.getBaseEntityDAO(entityIID);
    }
    return rdbmsComponentUtils.getBaseEntityDAO(entityIID, localeCode);
  }


  private void addDataTransferProperties(ConfigurationDAO configurationDAO,
      IContextualDataTransferOnConfigChangeDTO contextualDataTransferDTO, List<IModifiedPropertiesCouplingDTO> addedProperties)
      throws RDBMSException, Exception
  {
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    List<ICouplingDTO> sourceAndTargetEntities = couplingDAO
        .getSourceAndTargetEntitiesByCouplingSourceIID(contextualDataTransferDTO.getContextualIID());
    
    Map<String, IModifiedPropertiesCouplingDTO> propertyCodes = new HashMap<>();
    for(IModifiedPropertiesCouplingDTO modifiedPropertyCouplingDTO : addedProperties) {
      String propertyCode = configurationDAO.getPropertyByIID(modifiedPropertyCouplingDTO.getPropertyIID()).getPropertyCode();
      propertyCodes.put(propertyCode, modifiedPropertyCouplingDTO);
    }
    
    for (ICouplingDTO sourceAndTarget : sourceAndTargetEntities) {
      
      long parentEntityIID = sourceAndTarget.getSourceEntityIID();
      long variantEntityIID = sourceAndTarget.getTargetEntityIID();
      
      IBaseEntityDAO parentEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(parentEntityIID);
      IBaseEntityDAO variantEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(variantEntityIID);
      
      IGetConfigDetailsForContextualDataTransferModel configDetails = getConfigDetails(parentEntityDAO);
      Map<String, IPropertiesIdCodeCouplingTypeModel> contextualDataTransfer = configDetails.getContextualDataTransfer();
      
      IPropertiesIdCodeCouplingTypeModel dataTransferPropertiesInContextual = contextualDataTransfer.
          get(configurationDAO.getClassifierByIID(contextualDataTransferDTO.getContextualIID()).getClassifierCode());
      
      List<IModifiedPropertiesCouplingDTO> dependentProperties = new ArrayList<>();
      getModifiedPropertiesCouplingDTOs(dataTransferPropertiesInContextual.getDependentAttributes(), dependentProperties, propertyCodes);
      
      List<IModifiedPropertiesCouplingDTO> independentProperties = new ArrayList<>();
      getModifiedPropertiesCouplingDTOs(dataTransferPropertiesInContextual.getIndependentAttributes(), independentProperties, propertyCodes);
      
      getModifiedPropertiesCouplingDTOs(dataTransferPropertiesInContextual.getTags(), independentProperties, propertyCodes);
      
      IConfigDetailsForBulkPropagationResponseModel parentBaseEntityConfigDetail = DataTransferUtil.getConfigDetailForBaseEntity(parentEntityDAO);
      IConfigDetailsForBulkPropagationResponseModel variantBaseEntityConfigDetail = DataTransferUtil.getConfigDetailForBaseEntity(variantEntityDAO);
      
      // Creating CouplingRecords for Independent Properties
      createCouplingRecordsForContextual(configurationDAO, contextualDataTransferDTO, independentProperties, couplingDAO,
         parentEntityDAO, variantEntityDAO, 0l, parentBaseEntityConfigDetail, variantBaseEntityConfigDetail);
      
      // Creating Coupling Records for Dependent properties
      List<String> localeCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(parentEntityIID, variantEntityIID);
      
      for(String localeCode : localeCodes) {
        long languageIID = configurationDAO.getLanguageConfig(localeCode).getLanguageIID();
        createCouplingRecordsForContextual(configurationDAO, contextualDataTransferDTO, dependentProperties, couplingDAO,
            parentEntityDAO, variantEntityDAO, languageIID, parentBaseEntityConfigDetail, variantBaseEntityConfigDetail);
      }

      RuleHandler ruleHandler = new RuleHandler();
      IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(
          variantBaseEntityConfigDetail.getReferencedElements(), variantBaseEntityConfigDetail.getReferencedAttributes(),
          variantBaseEntityConfigDetail.getReferencedTags());
      ruleHandler.evaluateDQMustShould(variantEntityIID, dataRules, variantEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO());
    }
    
    if(!sourceAndTargetEntities.isEmpty()) {
      rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(sourceAndTargetEntities.get(0).getSourceEntityIID(), 
          IEventDTO.EventType.END_OF_TRANSACTION);
    }
  }

  private void createCouplingRecordsForContextual(ConfigurationDAO configurationDAO,
      IContextualDataTransferOnConfigChangeDTO contextualDataTransferDTO, List<IModifiedPropertiesCouplingDTO> addedProperties,
      ICouplingDAO couplingDAO, IBaseEntityDAO parentEntityDAO,
      IBaseEntityDAO variantEntityDAO, Long languageIID, 
      IConfigDetailsForBulkPropagationResponseModel parentBaseEntityConfigDetail, 
      IConfigDetailsForBulkPropagationResponseModel variantBaseEntityConfigDetail) throws RDBMSException, Exception
  {
    List<IPropertyRecordDTO> dataTransferProperties = new ArrayList<>();
    List<IModifiedPropertiesCouplingDTO> applicablePropertyRecords = new ArrayList<>();
    
    
    ICouplingDTO couplingDTO = DataTransferUtil.prepareCouplingDTO(parentEntityDAO.getBaseEntityDTO().getBaseEntityIID(), variantEntityDAO.getBaseEntityDTO().getBaseEntityIID());
    couplingDTO
        .setCouplingSourceIID(configurationDAO.getClassifierByIID(contextualDataTransferDTO.getContextualIID()).getClassifierIID());
    Map<Long, RecordStatus> recordStatusInfo = new HashMap<>();
    createPropertyRecordInstance(parentEntityDAO, variantEntityDAO, dataTransferProperties, 
        addedProperties, configurationDAO, languageIID, applicablePropertyRecords, 
        parentBaseEntityConfigDetail, variantBaseEntityConfigDetail, couplingDTO, recordStatusInfo);
    
    for (IModifiedPropertiesCouplingDTO dataTransferProperty : applicablePropertyRecords) {
      
      if (CouplingBehavior.TIGHTLY.ordinal() == dataTransferProperty.getCouplingType()) {
        
        couplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
        couplingDTO.setCouplingSourceType(CouplingType.TIGHT_CONTEXTUAL);
      }
      else {
        couplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
        couplingDTO.setCouplingSourceType(CouplingType.DYN_CONTEXTUAL);
      }
      RecordStatus recordStatus = recordStatusInfo.get(dataTransferProperty.getPropertyIID());
      RecordStatus coupledRecord = recordStatus == null ? RecordStatus.UNDEFINED : recordStatus;
      couplingDTO.setRecordStatus(coupledRecord);
      couplingDTO.setLocaleIID(languageIID);
      couplingDTO.setPropertyIID(dataTransferProperty.getPropertyIID());
      couplingDAO.createCoupledRecordForContextual(couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
      
    }
  }
  
  public void getModifiedPropertiesCouplingDTOs(List<IIdCodeCouplingTypeModel> properties, 
      List<IModifiedPropertiesCouplingDTO> dependentProperties, Map<String, IModifiedPropertiesCouplingDTO> propertyCodes){
    
    for(IIdCodeCouplingTypeModel property : properties) {
      if(propertyCodes.containsKey(property.getId())) {
        dependentProperties.add(propertyCodes.get(property.getId()));
      }
    }
    
  }
  public IGetConfigDetailsForContextualDataTransferModel getConfigDetails(IBaseEntityDAO baseEntityDAO)
      throws JsonProcessingException, Exception
  {
    
    IKlassInstanceTypeInfoModel klassInstanceTypeModel = new KlassInstanceTypeInfoModel();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
    klassInstanceTypeModel.setKlassIds(types);
    List<String> taxonomyIds = new ArrayList<>();
    
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
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
    
    IGetConfigDetailsForContextualDataTransferStrategy configDetailForContextualDataTransfer = BGProcessApplication.getApplicationContext()
        .getBean(IGetConfigDetailsForContextualDataTransferStrategy.class);
    
    IGetConfigDetailsForContextualDataTransferModel configDetails = configDetailForContextualDataTransfer.execute(klassInstanceTypeModel);
    
    return configDetails;
  }
  
  private void createPropertyRecordInstance(IBaseEntityDAO sourceBaseEntityDAO, IBaseEntityDAO targetBaseEntityDAO,
      List<IPropertyRecordDTO> dataTransferProperties, List<IModifiedPropertiesCouplingDTO> addedProperties,
      ConfigurationDAO configurationDAO, Long languageIID, 
      List<IModifiedPropertiesCouplingDTO> applicablePropertyRecords, 
      IConfigDetailsForBulkPropagationResponseModel parentBaseEntityConfigDetail, 
      IConfigDetailsForBulkPropagationResponseModel variantBaseEntityConfigDetail, ICouplingDTO couplingDTO,  Map<Long, RecordStatus> recordStatusInfo) throws RDBMSException, Exception
  {
    
    Map<String, IReferencedSectionElementModel> sourceReferencedElements = parentBaseEntityConfigDetail.getReferencedElements();
    Map<String, IReferencedSectionElementModel> targetReferencedElements = variantBaseEntityConfigDetail.getReferencedElements();
    
    for (IModifiedPropertiesCouplingDTO attributeDataTransfer : addedProperties) {
      
      IPropertyDTO propertyDTO = configurationDAO.getPropertyByIID(attributeDataTransfer.getPropertyIID());
      
      if (propertyDTO.getSuperType().equals(SuperType.ATTRIBUTE)) {
        
        IReferencedSectionAttributeModel sourceAttributeElement = 
            (IReferencedSectionAttributeModel) sourceReferencedElements.get(propertyDTO.getPropertyCode());
          
        IReferencedSectionAttributeModel targetAttributeElement = 
            (IReferencedSectionAttributeModel) targetReferencedElements.get(propertyDTO.getPropertyCode());
          
        
        if(sourceReferencedElements.containsKey(propertyDTO.getPropertyCode()) && 
            targetReferencedElements.containsKey(propertyDTO.getPropertyCode())) {
          
          if(sourceAttributeElement.getAttributeVariantContext() != null || targetAttributeElement.getAttributeVariantContext() != null) {
            continue;
          }
          
          if(sourceAttributeElement.getIsSkipped() || 
              targetAttributeElement.getIsSkipped()) {
            continue;
          }
          
          dataTransferProperties.add(sourceBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "Value to be discarded").build());
          applicablePropertyRecords.add(attributeDataTransfer);
        }
        
      }
      else {
        
        if(sourceReferencedElements.containsKey(propertyDTO.getPropertyCode()) && 
            targetReferencedElements.containsKey(propertyDTO.getPropertyCode())) {
          
          if(sourceReferencedElements.get(propertyDTO.getPropertyCode()).getIsSkipped() || 
              targetReferencedElements.get(propertyDTO.getPropertyCode()).getIsSkipped()) {
            continue;
          }
          
          dataTransferProperties.add(sourceBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build());
          applicablePropertyRecords.add(attributeDataTransfer);
        }
        
      }
    }
    
    DataTransferUtil.createPropertyRecordsForCoupling(targetBaseEntityDAO, dataTransferProperties, 
        sourceBaseEntityDAO, languageIID, parentBaseEntityConfigDetail, variantBaseEntityConfigDetail, 
        new ArrayList<>(), couplingDTO, recordStatusInfo);
  }
  
  private void resolveConflictsAndRaiseEventsForWorkFlow(ILocaleCatalogDAO localeCatlogDAO, ICouplingDAO couplingDAO, long propertyIID,
      ICouplingDTO couplingDTO) throws RDBMSException, Exception, CSFormatException
  {
    String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(couplingDTO.getLocaleIID()).getLanguageCode();
    
    IBaseEntityDAO targetBaseEntityDAO = languageCode != null ? getBaseEntityDAO(couplingDTO.getTargetEntityIID(), languageCode) : 
      rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getTargetEntityIID());
    
    couplingDAO.resolvedConflicts(couplingDTO);
    
    IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(propertyIID);
    IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(propertyDTO);
    IPropertyRecordDTO propertyRecord = targetBaseEntityDTO.getPropertyRecord(propertyIID);
    
    couplingDAO.raiseEventsForCoupling(propertyRecord, couplingDTO, localeCatlogDAO, targetBaseEntityDTO);
    
  }
  
}
