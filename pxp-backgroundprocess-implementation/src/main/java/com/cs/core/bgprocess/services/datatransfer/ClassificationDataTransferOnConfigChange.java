package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.json.simple.JSONObject;

import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.CouplingDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.EntityEventDAS;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTOBuilder;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClassificationDataTransferOnConfigChange {

  /**
   * this method is use to population data when default value changed or
   * couplingtype changed for klass/taxonomy
   * 
   * @param rdbmsComponentUtils
   * @param modifiedCoupledPropertyDTOs
   * @throws CSFormatException
   * @throws CSInitializationException
   * @throws PluginException
   * @throws Exception
   */
  public static void executeForClassificationCoupling(RDBMSComponentUtils rdbmsComponentUtils,
      Set<IModifiedCoupedPropertyDTO> modifiedCoupledPropertyDTOs) throws Exception
  {
    if (modifiedCoupledPropertyDTOs == null || modifiedCoupledPropertyDTOs.isEmpty())
      return;
    
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    ICouplingDAO couplingDAO = localeCatlogDAO.openCouplingDAO();
    
    Map<String, Set<IPropertyDTO>> modifiedCoupledPropertyWithClassifierCode = new HashMap<>();
    Map<String, IModifiedCoupedPropertyDTO> modifiedCoupledPropertyMap = new HashMap<>();
    
    modifiedCoupledPropertyDTOs.forEach(modifiedCoupledPropertyDTO -> {
      Set<String> classifierCodes = modifiedCoupledPropertyDTO.getClassifierCodes();
      classifierCodes.forEach(classifierCode -> {
        Set<IPropertyDTO> set = modifiedCoupledPropertyWithClassifierCode.get(classifierCode);
        if (set == null)
          modifiedCoupledPropertyWithClassifierCode.put(classifierCode, new HashSet<>());
        modifiedCoupledPropertyWithClassifierCode.get(classifierCode).add(modifiedCoupledPropertyDTO.getProperty());
      });
      
      modifiedCoupledPropertyMap.put(modifiedCoupledPropertyDTO.getProperty().getCode(), modifiedCoupledPropertyDTO);
    });

    ILocaleCatalogDAO defaultLocaleCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO(rdbmsComponentUtils.getLocaleCatlogDAO().getUserSessionDTO().getTransactionID());
    IConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    Long sourceEntityIID = 0l;
    for (String classifierCode : modifiedCoupledPropertyWithClassifierCode.keySet()) {
      IBaseEntityDTO entityByID = defaultLocaleCatalogDAO.getEntityByID(classifierCode);
      IBaseEntityDAO sourceBaseEntityDAO = defaultLocaleCatalogDAO.openBaseEntity(entityByID);

      Set<IPropertyDTO> propertyDTOs = modifiedCoupledPropertyWithClassifierCode.get(classifierCode);
      IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO
          .loadPropertyRecords(propertyDTOs.toArray(new IPropertyDTO[propertyDTOs.size()]));
      IClassifierDTO classifierDTO = configurationDAO.getClassifierByCode(classifierCode);
      Set<IPropertyDTO> addedCouplingTypeProperties = new HashSet<>();
      List<IPropertyRecordDTO> updatedPropertyRecords = new ArrayList<>();
      
      for (IPropertyDTO propertyDTO : propertyDTOs) {
        IPropertyRecordDTO propertyRecord = sourceBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
        IModifiedCoupedPropertyDTO modifiedCoupedProperty = modifiedCoupledPropertyMap.get(propertyDTO.getCode());
        CouplingBehavior couplingBehavior = modifiedCoupedProperty.getCouplingBehavior();
        // property record null means newly added coupling type and coupling
        // value
        if (propertyRecord != null) {
          long couplingSourceIID = sourceBaseEntityDTO.getNatureClassifier().getClassifierIID();
          List<ICouplingDTO> targetCouplingDTOs = couplingDAO.getConflictingValuesByCouplingSourceIIDAndPropertyIID(couplingSourceIID,
              propertyDTO.getPropertyIID());

          if(!targetCouplingDTOs.isEmpty()){
            ICouplingDTO couplingDTO = targetCouplingDTOs.get(0);
            sourceEntityIID = couplingDTO.getTargetEntityIID();
          }

          Boolean couplingTypeChanged = modifiedCoupedProperty.isCouplingTypeChanged();
          Boolean valueChanged = modifiedCoupedProperty.isValueChanged();
          for (ICouplingDTO targetCouplingDTO : targetCouplingDTOs) {
            
            IBaseEntityDTO targetBaseEntity = localeCatlogDAO.getBaseEntityDTOByIID(targetCouplingDTO.getTargetEntityIID());

            ILocaleCatalogDTO localeCatalogDTO = RDBMSUtils.newUserSessionDAO().newLocaleCatalogDTO(localeCatlogDAO.getLocaleCatalogDTO().getLocaleID(),
                targetBaseEntity.getCatalog().getCatalogCode(), targetBaseEntity.getCatalog().getOrganizationCode());
           localeCatlogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog(localeCatlogDAO.getUserSessionDTO(), localeCatalogDTO);
           
            if (couplingTypeChanged && !valueChanged) {
              handleForCouplingTypeChanged(rdbmsComponentUtils, localeCatlogDAO, defaultLocaleCatalogDAO, couplingDAO, sourceBaseEntityDTO,
                  propertyDTO, couplingSourceIID, targetCouplingDTO, couplingBehavior);
            }
            else if (valueChanged && !couplingTypeChanged) {
              handleForDefaultValueChanged(rdbmsComponentUtils, localeCatlogDAO, defaultLocaleCatalogDAO, couplingDAO, sourceBaseEntityDTO,
                  propertyDTO, couplingSourceIID, targetCouplingDTO);
              
            }
            else if (valueChanged && couplingTypeChanged) {
              handleForCouplingTypeAndDefaultValueChanged(rdbmsComponentUtils, localeCatlogDAO, defaultLocaleCatalogDAO, couplingDAO,
                  sourceBaseEntityDTO, propertyDTO, couplingSourceIID, targetCouplingDTO, couplingBehavior);
            }
          }  
          if (couplingBehavior != CouplingBehavior.NONE) {
            if (valueChanged) {
              if (propertyDTO.getSuperType() == SuperType.ATTRIBUTE) {
                IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
                valueRecordDTO.setValue(modifiedCoupedProperty.getValue());
                valueRecordDTO.setAsHTML(modifiedCoupedProperty.getValueAsHtml());
                valueRecordDTO.setAsNumber(modifiedCoupedProperty.getValueAsNumber());
                valueRecordDTO.setUnitSymbol(modifiedCoupedProperty.getUnitSymbol());
                updatedPropertyRecords.add(valueRecordDTO);
                //sourceBaseEntityDAO.updatePropertyRecords(valueRecordDTO);
              }
              else if (propertyDTO.getSuperType() == SuperType.TAGS) {
                ITagsRecordDTO tagsRecordDTO = (ITagsRecordDTO) propertyRecord;
                tagsRecordDTO
                    .setTags(modifiedCoupedProperty.getTagValues().toArray(new ITagDTO[modifiedCoupedProperty.getTagValues().size()]));
                updatedPropertyRecords.add(tagsRecordDTO);
                //sourceBaseEntityDAO.updatePropertyRecords(tagsRecordDTO);
              }
            }
          }
          else {
            sourceBaseEntityDAO.deletePropertyRecords(propertyRecord);
          }
        }
        else {
          if (couplingBehavior == CouplingBehavior.TIGHTLY || couplingBehavior == CouplingBehavior.DYNAMIC) {
            addedCouplingTypeProperties.add(propertyDTO);
          }
        }
      }
      
      if (updatedPropertyRecords.size() > 0) {
        sourceBaseEntityDAO.updatePropertyRecords(updatedPropertyRecords.toArray(new IPropertyRecordDTO[updatedPropertyRecords.size()]));
      }
      
      if (addedCouplingTypeProperties.size() > 0) {
        Set<Long> targets = handleDataTransferForAddedCouplingType(rdbmsComponentUtils, localeCatlogDAO, couplingDAO,
            modifiedCoupledPropertyMap, defaultLocaleCatalogDAO, classifierCode, sourceBaseEntityDAO, sourceBaseEntityDTO, classifierDTO,
            addedCouplingTypeProperties);
        if(!targets.isEmpty()){
          sourceEntityIID = new ArrayList<>(targets).get(0);
        }
      }
      Set<Long> targetEntityIIDs = couplingDAO.getAllEntityIIDsForCLassifierIID(classifierDTO.getClassifierIID(),
          classifierDTO.getClassifierType());
      // remove default instance IID from targetEntityIIDs list.
      targetEntityIIDs.remove(sourceBaseEntityDTO.getBaseEntityIID());
      List<IBaseEntityDTO> targetBaseEntityDTOs = localeCatlogDAO.getBaseEntityDTOsByIIDs(targetEntityIIDs);
      for(IBaseEntityDTO targetBaseEntity : targetBaseEntityDTOs) {
        ILocaleCatalogDTO localeCatalogDTO = RDBMSUtils.newUserSessionDAO().newLocaleCatalogDTO(localeCatlogDAO.getLocaleCatalogDTO().getLocaleID(),
            targetBaseEntity.getCatalog().getCatalogCode(), targetBaseEntity.getCatalog().getOrganizationCode());
       localeCatlogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog(localeCatlogDAO.getUserSessionDTO(), localeCatalogDTO);
       
        IBaseEntityDAO targetEntityDAO = localeCatlogDAO.openBaseEntity(targetBaseEntity);
        
        IDataRulesHelperModel configDetails = getConfigDetails(targetEntityDAO, rdbmsComponentUtils);
        
        RuleHandler ruleHandler = new RuleHandler();
        IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(configDetails.getReferencedElements(),
            configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
        ruleHandler.evaluateDQMustShould(targetEntityDAO.getBaseEntityDTO().getBaseEntityIID(), dataRules, targetEntityDAO, localeCatlogDAO);
      }
    }
    
    if(sourceEntityIID != 0l){
      localeCatlogDAO.postUsecaseUpdate(sourceEntityIID, EventType.END_OF_TRANSACTION);
    }
  }

  private static IDataRulesHelperModel getConfigDetails(IBaseEntityDAO baseEntityDAO, RDBMSComponentUtils rdbmsComponentUtils)
      throws CSFormatException, CSInitializationException, Exception, JsonProcessingException
  {
    
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
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

    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("classifiers", ListUtils.sum(types, taxonomyIds));
    requestMap.put("removedClassifiers", new ArrayList<String>());
    
    String localeID = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID();
    JSONObject detailsFromODB = CSConfigServer.instance().request(requestMap, "GetProductIdentifiersForClassifiers", localeID); 
    IDataRulesHelperModel dataRulesHelperModel = ObjectMapperUtil.readValue(ObjectMapperUtil.writeValueAsString(detailsFromODB),
        DataRulesHelperModel.class);
    return dataRulesHelperModel;
  }
  
  private static Set<Long> handleDataTransferForAddedCouplingType(RDBMSComponentUtils rdbmsComponentUtils, ILocaleCatalogDAO localeCatalogDAO,
      ICouplingDAO couplingDAO, Map<String, IModifiedCoupedPropertyDTO> modifiedCoupledPropertyMap,
      ILocaleCatalogDAO defalutLocaleCatlogDAO, String classifierCode, IBaseEntityDAO sourceBaseEntityDAO,
      IBaseEntityDTO sourceBaseEntityDTO, IClassifierDTO classifierDTO, Set<IPropertyDTO> propertiesToCreateAtSourceSide)
      throws RDBMSException, CSFormatException, Exception
  {
    List<IPropertyRecordDTO> propertyRecordDTOs = new ArrayList<>();
    // create instances of newly added properties to default instance of source
    for (IPropertyDTO propertyDTO : propertiesToCreateAtSourceSide) {
      IModifiedCoupedPropertyDTO modifiedCoupedPropertyDTO = modifiedCoupledPropertyMap.get(propertyDTO.getCode());
      IPropertyRecordDTO propertyRecordDTO = null;
      if (propertyDTO.getSuperType().equals(SuperType.ATTRIBUTE)) {
        IValueRecordDTOBuilder valueRecordBuilder = sourceBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, modifiedCoupedPropertyDTO.getValue())
            .asHTML(modifiedCoupedPropertyDTO.getValueAsHtml()).asNumber(modifiedCoupedPropertyDTO.getValueAsNumber())
            .unitSymbol(modifiedCoupedPropertyDTO.getUnitSymbol());
        if(modifiedCoupedPropertyDTO.isDependent()) {
          valueRecordBuilder.localeID(localeCatalogDAO.getLocaleCatalogDTO().getLocaleID());
        }
        propertyRecordDTO = valueRecordBuilder.build();
        
      }
      else if (propertyDTO.getSuperType().equals(SuperType.TAGS)) {
        propertyRecordDTO = sourceBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO)
            .tags(modifiedCoupedPropertyDTO.getTagValues().toArray(new ITagDTO[modifiedCoupedPropertyDTO.getTagValues().size()])).build();
      }
      propertyRecordDTOs.add(propertyRecordDTO);
    }
    sourceBaseEntityDAO.createPropertyRecords(propertyRecordDTOs.toArray(new IPropertyRecordDTO[propertyRecordDTOs.size()]));
    
    Set<Long> targetEntityIIDs = couplingDAO.getAllEntityIIDsForCLassifierIID(classifierDTO.getClassifierIID(),
        classifierDTO.getClassifierType());
    // remove default instance IID from targetEntityIIDs list.
    targetEntityIIDs.remove(sourceBaseEntityDTO.getBaseEntityIID());
    List<IBaseEntityDTO> targetBaseEntityDTOs = localeCatalogDAO.getBaseEntityDTOsByIIDs(targetEntityIIDs);
    for(IBaseEntityDTO targetBaseEntity : targetBaseEntityDTOs) {
      ILocaleCatalogDTO localeCatalogDTO = RDBMSUtils.newUserSessionDAO().newLocaleCatalogDTO(localeCatalogDAO.getLocaleCatalogDTO().getLocaleID(),
          targetBaseEntity.getCatalog().getCatalogCode(), targetBaseEntity.getCatalog().getOrganizationCode());
     localeCatalogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog(localeCatalogDAO.getUserSessionDTO(), localeCatalogDTO);
      
     IBaseEntityDAO targetBaseEntityDAO = localeCatalogDAO.openBaseEntity(targetBaseEntity);
      Long targetEntityIID = targetBaseEntity.getBaseEntityIID();
      for (IPropertyDTO propertyDTO : propertiesToCreateAtSourceSide) {
        IModifiedCoupedPropertyDTO modifiedCoupedPropertyDTO = modifiedCoupledPropertyMap.get(propertyDTO.getCode());
        
        if (modifiedCoupedPropertyDTO.getCouplingBehavior() == CouplingBehavior.DYNAMIC) {
          newlyAddedDynamicCoupledRecord(rdbmsComponentUtils, localeCatalogDAO, defalutLocaleCatlogDAO, couplingDAO, sourceBaseEntityDTO, classifierDTO,
              targetEntityIID, propertyDTO, 0l);
        }
        else {
          if (!modifiedCoupedPropertyDTO.isDependent()) {
            if (modifiedCoupedPropertyDTO.getCouplingBehavior() == CouplingBehavior.TIGHTLY) {
              newlyAddedTightlyCoupledRecord(rdbmsComponentUtils, localeCatalogDAO, defalutLocaleCatlogDAO, couplingDAO, sourceBaseEntityDTO, classifierDTO,
                  targetEntityIID, propertyDTO, targetBaseEntityDAO, 0l);
            }
          }
            else {
              List<String> localeCodes = targetBaseEntityDAO.getBaseEntityDTO().getLocaleIds();
              for (String locale : localeCodes) {
                targetBaseEntityDAO = getBaseEntityDAO(rdbmsComponentUtils, targetEntityIID, locale);
                ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfig(locale);
                newlyAddedTightlyCoupledRecord(rdbmsComponentUtils, localeCatalogDAO, defalutLocaleCatlogDAO, couplingDAO, sourceBaseEntityDTO, classifierDTO,
                    targetEntityIID, propertyDTO, targetBaseEntityDAO, languageConfigDTO.getLanguageIID());
                
              }
            }
          }
      }
    }
    return targetEntityIIDs;
  }
  
  private static void newlyAddedDynamicCoupledRecord(RDBMSComponentUtils rdbmsComponentUtils, ILocaleCatalogDAO localeCatalogDAO, ILocaleCatalogDAO defaultCatalogDAO,
      ICouplingDAO couplingDAO, IBaseEntityDTO sourceBaseEntityDTO, IClassifierDTO classifierDTO, Long targetEntityIID,
      IPropertyDTO propertyDTO, Long localeIID) throws Exception
  {
    ICouplingDTO targetCouplingDTO = new CouplingDTO();
    targetCouplingDTO.setSourceEntityIID(sourceBaseEntityDTO.getBaseEntityIID());
    targetCouplingDTO.setTargetEntityIID(targetEntityIID);
    targetCouplingDTO.setPropertyIID(propertyDTO.getPropertyIID());
    targetCouplingDTO.setCouplingSourceIID(classifierDTO.getClassifierIID());
    targetCouplingDTO.setLocaleIID(localeIID);
    
    List<ICouplingDTO> sourceDynamicConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    
    if (sourceDynamicConflictingValues.size() == 0) {
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setRecordStatus(RecordStatus.COUPLED);
      DataTransferUtil.deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
      couplingDAO.createCoupledRecord(targetCouplingDTO, localeCatalogDAO);
      couplingDAO.createConflictingValueRecord(targetCouplingDTO);

    }
    else {
      Boolean coupledRecordFound = false;
      for (ICouplingDTO couplingDTO : sourceDynamicConflictingValues) {
        if (couplingDTO.getRecordStatus() != RecordStatus.COUPLED) {
          continue;
        }
        IBaseEntityDAO coupledSourceBaseEntityDAO = null;
        if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION)
            || couplingDTO.getCouplingSourceType().equals(CouplingType.DYN_CLASSIFICATION)) {
          coupledSourceBaseEntityDAO = defaultCatalogDAO.openBaseEntity(defaultCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
          
        }
        else {
          coupledSourceBaseEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
        }   
        IBaseEntityDTO sourceBaseEntityDTOForCoupledRecord = coupledSourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
        createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTOForCoupledRecord, couplingDTO.getSourceEntityIID(),
            couplingDTO.getTargetEntityIID(), propertyDTO, couplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(couplingDTO, RecordStatus.NOTIFIED);
        coupledRecordFound = true;
        break;
        
      }
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
      
      if (coupledRecordFound) {
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
      }
      else {
        couplingDAO.createCoupledRecord(targetCouplingDTO, localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
        DataTransferUtil.deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
      }
      
    }
  }
  
  private static void newlyAddedTightlyCoupledRecord(RDBMSComponentUtils rdbmsComponentUtils, ILocaleCatalogDAO localeCatalogDAO, ILocaleCatalogDAO defaultCatalogDAO,
      ICouplingDAO couplingDAO, IBaseEntityDTO sourceBaseEntityDTO, IClassifierDTO classifierDTO, Long targetEntityIID,
      IPropertyDTO propertyDTO, IBaseEntityDAO targetBaseEntityDAO, Long localeIID) throws RDBMSException, Exception, CSFormatException
  {
    ICouplingDTO targetCouplingDTO = new CouplingDTO();
    targetCouplingDTO.setSourceEntityIID(sourceBaseEntityDTO.getBaseEntityIID());
    targetCouplingDTO.setTargetEntityIID(targetEntityIID);
    targetCouplingDTO.setPropertyIID(propertyDTO.getPropertyIID());
    targetCouplingDTO.setCouplingSourceIID(classifierDTO.getClassifierIID());
    targetCouplingDTO.setLocaleIID(localeIID);
    
    List<ICouplingDTO> sourceDynamicConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    // if already this property is dynamically coupled with any other source
    // then
    if (sourceDynamicConflictingValues.size() > 0) {
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setRecordStatus(RecordStatus.FORKED);
      couplingDAO.createConflictingValueRecord(targetCouplingDTO);
    }
    
    List<ICouplingDTO> sourceTightlyConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
    
    if (sourceTightlyConflictingValues.size() == 0) {
      IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.loadPropertyRecords(propertyDTO);
      IPropertyRecordDTO propertyRecord = targetBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
      
      if (propertyRecord == null) {
        createEmptyPropertyRecord(propertyDTO, targetBaseEntityDAO, localeIID);
      }
      
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setRecordStatus(RecordStatus.NOTIFIED);
      couplingDAO.createConflictingValueRecord(targetCouplingDTO);
      
    }
    else {
      for (ICouplingDTO couplingDTO : sourceDynamicConflictingValues) {
        if (couplingDTO.getRecordStatus() != RecordStatus.COUPLED) {
          continue;
        }
        IBaseEntityDAO coupledSourceBaseEntityDAO = null;
        if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION)
            || couplingDTO.getCouplingSourceType().equals(CouplingType.DYN_CLASSIFICATION)) {
          coupledSourceBaseEntityDAO = defaultCatalogDAO.openBaseEntity(defaultCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
          
        }
        else {
          coupledSourceBaseEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
        }
        IBaseEntityDTO sourceBaseEntityDTOForCoupledRecord = coupledSourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
        createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTOForCoupledRecord, couplingDTO.getSourceEntityIID(),
            couplingDTO.getTargetEntityIID(), propertyDTO, couplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(couplingDTO, RecordStatus.NOTIFIED);
        break;
        
      }
      targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
      targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
      targetCouplingDTO.setRecordStatus(RecordStatus.NOTIFIED);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
      
    }
  }
  
  private static void createEmptyPropertyRecord(IPropertyDTO propertyDTO, IBaseEntityDAO targetBaseEntityDAO, Long localeIID)
      throws RDBMSException
  {
    if (propertyDTO.getSuperType() == SuperType.ATTRIBUTE) {
      IValueRecordDTO valueRecord = null;
      if (localeIID == 0) {
        valueRecord = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "").build();
      }
      else {
        String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode();
        valueRecord = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, "").localeID(languageCode).build();
      }
      targetBaseEntityDAO.createPropertyRecords(valueRecord);
    }
    else if (propertyDTO.getSuperType() == SuperType.TAGS) {
      ITagsRecordDTO tagsRecord = targetBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
      targetBaseEntityDAO.createPropertyRecords(tagsRecord);
    }
  }
  
  private static void handleForCouplingTypeAndDefaultValueChanged(RDBMSComponentUtils rdbmsComponentUtils,
      ILocaleCatalogDAO localeCatalogDAO,ILocaleCatalogDAO defaultCatalogDAO, ICouplingDAO couplingDAO, IBaseEntityDTO sourceBaseEntityDTO, IPropertyDTO propertyDTO,
      long couplingSourceIID, ICouplingDTO targetCouplingDTO, CouplingBehavior couplingBehavior)
      throws RDBMSException, Exception, CSFormatException
  {
    RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
    if (couplingBehavior == CouplingBehavior.TIGHTLY) {
      
      switch (recordStatus) {
        case COUPLED:
          configModificationFormDynamicToTightCoupledRecordWithValueChange(localeCatalogDAO, couplingDAO, sourceBaseEntityDTO, propertyDTO,
              couplingSourceIID, targetCouplingDTO);
          break;
        
        case FORKED:
          configModificationForDynamicToTightForkedRecord(couplingDAO, targetCouplingDTO);
          break;
        
        case NOTIFIED:
          configModificationForDynamicToTightNotifiedRecord(couplingDAO, targetCouplingDTO);
          break;
        
        default:
          break;
      }
    }
    else if (couplingBehavior == CouplingBehavior.DYNAMIC) {
      switch (recordStatus) {
        case COUPLED:
          configModificationFormTightlyToDynamicCoupledRecord(couplingDAO, targetCouplingDTO);
          break;
        
        case FORKED:
          configModificationFormTightlyToDynamicForkedRecord(rdbmsComponentUtils, localeCatalogDAO, defaultCatalogDAO, couplingDAO,
              propertyDTO, targetCouplingDTO);
          break;
        
        case NOTIFIED:
          configModificationFormTightlyToDynamicNotifiedRecord(rdbmsComponentUtils, couplingDAO, targetCouplingDTO);
          break;
        default:
          break;
      }
    }
    else if (couplingBehavior == CouplingBehavior.NONE) {
      
      if (recordStatus == RecordStatus.COUPLED) {
        
        createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTO, targetCouplingDTO.getSourceEntityIID(),
            targetCouplingDTO.getTargetEntityIID(), propertyDTO, targetCouplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
      }
      couplingDAO.deleteCouplingRecordFromConflictingValues(targetCouplingDTO);
      
    }
  }
  
  private static void handleForDefaultValueChanged(RDBMSComponentUtils rdbmsComponentUtils, ILocaleCatalogDAO localeCatalogDAO, ILocaleCatalogDAO defaultCatalogDAO,
      ICouplingDAO couplingDAO, IBaseEntityDTO sourceBaseEntityDTO, IPropertyDTO propertyDTO, long couplingSourceIID,
      ICouplingDTO targetCouplingDTO) throws RDBMSException, Exception, CSFormatException
  {
    RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
    if (targetCouplingDTO.getCouplingType() == CouplingBehavior.TIGHTLY && recordStatus == RecordStatus.COUPLED) {
      createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTO, targetCouplingDTO.getSourceEntityIID(),
          targetCouplingDTO.getTargetEntityIID(), propertyDTO, targetCouplingDTO.getLocaleIID());
      couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
          targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
    }
    else if (recordStatus == RecordStatus.FORKED) {
      List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO,
          targetCouplingDTO.getCouplingType());
      Boolean coupledRecordFound = false;
      for (ICouplingDTO couplingDTO : sourceConflictingValues) {
        if (couplingDTO.getRecordStatus() != RecordStatus.COUPLED) {
          continue;
        }
        IBaseEntityDAO sourceBaseEntityDAO = null;
        if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION)
            || couplingDTO.getCouplingSourceType().equals(CouplingType.DYN_CLASSIFICATION)) {
          sourceBaseEntityDAO = defaultCatalogDAO.openBaseEntity(defaultCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
          
        }
        else {
          sourceBaseEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
        }
        
        IBaseEntityDTO sourceBaseEntityDTOForCoupledRecord = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
        createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTOForCoupledRecord, couplingDTO.getSourceEntityIID(),
            couplingDTO.getTargetEntityIID(), propertyDTO, couplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(couplingDTO, RecordStatus.NOTIFIED);
        coupledRecordFound = true;
        
      }
      if (coupledRecordFound || targetCouplingDTO.getCouplingType() == CouplingBehavior.TIGHTLY) {
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
      }
      else {
        couplingDAO.createCoupledRecord(targetCouplingDTO, localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
        DataTransferUtil.deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
        
      }
    }
    else if (targetCouplingDTO.getCouplingType() == CouplingBehavior.DYNAMIC && recordStatus == RecordStatus.COUPLED) {
      
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        
        IBaseEntityDTO entityByIID = localeCatalogDAO.getEntityByIID(targetCouplingDTO.getTargetEntityIID());
        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(entityByIID);
        IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(targetCouplingDTO.getPropertyIID());
        baseEntityDAO.loadPropertyRecords(propertyByIID);
        EntityEventDAS eventDAS = new EntityEventDAS(currentConn, localeCatalogDAO.getUserSessionDTO(),
            localeCatalogDAO.getLocaleCatalogDTO(), entityByIID);
        eventDAS.registerChange(ITimelineDTO.ChangeCategory.CouplingSource,
            baseEntityDAO.getBaseEntityDTO().getPropertyRecord(targetCouplingDTO.getPropertyIID()));
        eventDAS.postRegisteredChanges();
      });
    }
  }
  
  private static void handleForCouplingTypeChanged(RDBMSComponentUtils rdbmsComponentUtils, ILocaleCatalogDAO localeCatalogDAO,
      ILocaleCatalogDAO defaultCatalogDAO, ICouplingDAO couplingDAO, IBaseEntityDTO sourceBaseEntityDTO, IPropertyDTO propertyDTO,
      long couplingSourceIID, ICouplingDTO targetCouplingDTO, CouplingBehavior couplingBehavior)
      throws RDBMSException, Exception, CSFormatException
  {
    RecordStatus recordStatus = targetCouplingDTO.getRecordStatus();
    if (couplingBehavior == CouplingBehavior.TIGHTLY) {
      
      switch (recordStatus) {
        case COUPLED:
          configModificationFormDynamicToTightCoupledRecord(localeCatalogDAO, couplingDAO, sourceBaseEntityDTO, propertyDTO,
              couplingSourceIID, targetCouplingDTO);
          break;
        
        case FORKED:
          configModificationForDynamicToTightForkedRecord(couplingDAO, targetCouplingDTO);
          break;
        
        case NOTIFIED:
          configModificationForDynamicToTightNotifiedRecord(couplingDAO, targetCouplingDTO);
          break;
        
        default:
          break;
      }
    }
    else if (couplingBehavior == CouplingBehavior.DYNAMIC) {
      switch (recordStatus) {
        case COUPLED:
          configModificationFormTightlyToDynamicCoupledRecord(couplingDAO, targetCouplingDTO);
          break;
        
        case FORKED:
          configModificationFormTightlyToDynamicForkedRecord(rdbmsComponentUtils, localeCatalogDAO, defaultCatalogDAO, couplingDAO,
              propertyDTO, targetCouplingDTO);
          break;
        
        case NOTIFIED:
          configModificationFormTightlyToDynamicNotifiedRecord(rdbmsComponentUtils, couplingDAO, targetCouplingDTO);
          break;
        default:
          break;
      }
    }
    else if (couplingBehavior == CouplingBehavior.NONE) {
      
      if (recordStatus == RecordStatus.COUPLED) {
        
        createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTO, targetCouplingDTO.getSourceEntityIID(),
            targetCouplingDTO.getTargetEntityIID(), propertyDTO, targetCouplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
      }
      couplingDAO.deleteCouplingRecordFromConflictingValues(targetCouplingDTO);
    }
  }
  
  private static void configModificationFormTightlyToDynamicNotifiedRecord(RDBMSComponentUtils componentUtils, ICouplingDAO couplingDAO,
      ICouplingDTO targetCouplingDTO) throws CSFormatException, Exception
  {
    List<ICouplingDTO> sourceConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
    
    for (ICouplingDTO sourceValues : sourceConflictingValues) {
      couplingDAO.updateRecordStatusForConflictingValues(sourceValues, RecordStatus.FORKED);
    }
    
    targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
    targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
    couplingDAO.createCoupledRecord(targetCouplingDTO, componentUtils.getLocaleCatlogDAO());
    DataTransferUtil.deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
  }
  
  private static void configModificationFormTightlyToDynamicForkedRecord(RDBMSComponentUtils rdbmsComponentUtils,
      ILocaleCatalogDAO localeCatalogDAO,ILocaleCatalogDAO defaultCatalogDAO, ICouplingDAO couplingDAO, IPropertyDTO propertyDTO, ICouplingDTO targetCouplingDTO)
      throws Exception
  {
    
    List<ICouplingDTO> sourceDynamicConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.DYNAMIC);
    if (sourceDynamicConflictingValues.size() == 0) {
      
      List<ICouplingDTO> sourceTightlyConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
      
      if(sourceTightlyConflictingValues.size() > 0) {
        
        for(ICouplingDTO sourceTightlyConflictingValue : sourceTightlyConflictingValues) {
          
          couplingDAO.updateRecordStatusForConflictingValues(sourceTightlyConflictingValue, RecordStatus.FORKED);
          couplingDAO.deleteCoupledRecord(sourceTightlyConflictingValue.getSourceEntityIID(), sourceTightlyConflictingValue.getTargetEntityIID(),
              sourceTightlyConflictingValue.getPropertyIID(), sourceTightlyConflictingValue.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
        }
      }
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
      couplingDAO.createCoupledRecord(targetCouplingDTO, localeCatalogDAO);
      couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
    }
    else {
      Boolean coupledRecordFound = false;
      for (ICouplingDTO couplingDTO : sourceDynamicConflictingValues) {
        if (couplingDTO.getRecordStatus() != RecordStatus.COUPLED) {
          continue;
        }
        IBaseEntityDAO sourceBaseEntityDAO = null;
        if (couplingDTO.getCouplingSourceType().equals(CouplingType.TIGHT_CLASSIFICATION)) {
          sourceBaseEntityDAO = defaultCatalogDAO.openBaseEntity(defaultCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
          
        }
        else {
          sourceBaseEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(couplingDTO.getSourceEntityIID()));
        }
        IBaseEntityDTO sourceBaseEntityDTOForCoupledRecord = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
        createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTOForCoupledRecord, couplingDTO.getSourceEntityIID(),
            couplingDTO.getTargetEntityIID(), propertyDTO, couplingDTO.getLocaleIID());
        couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
            targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(couplingDTO, RecordStatus.NOTIFIED);
        coupledRecordFound = true;
        break;
        
      }
      
      targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
      targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
      
      if (coupledRecordFound) {
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
      }
      else {
        couplingDAO.createCoupledRecord(targetCouplingDTO, localeCatalogDAO);
        couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
        DataTransferUtil.deletePropertyRecordFromTargetBaseEntity(targetCouplingDTO);
      }
      
    }
    
    List<ICouplingDTO> sourceTightConflictingValues = couplingDAO.getSourceConflictingValues(targetCouplingDTO, CouplingBehavior.TIGHTLY);
    for (ICouplingDTO couplingDTO : sourceTightConflictingValues) {
      couplingDAO.updateCouplingTypeForConflictingValues(couplingDTO, RecordStatus.FORKED);
    }
    
  }
  
  private static void configModificationFormTightlyToDynamicCoupledRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO)
      throws RDBMSException, Exception
  {
    targetCouplingDTO.setCouplingType(CouplingBehavior.DYNAMIC);
    targetCouplingDTO.setCouplingSourceType(CouplingType.DYN_CLASSIFICATION);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.COUPLED);
    couplingDAO.updateCouplingTypeForCoupledRecord(targetCouplingDTO, RecordStatus.COUPLED);
  }
  
  private static void configModificationForDynamicToTightNotifiedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO)
      throws RDBMSException
  {
    
    targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
    targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
  }
  
  private static void configModificationForDynamicToTightForkedRecord(ICouplingDAO couplingDAO, ICouplingDTO targetCouplingDTO)
      throws RDBMSException
  {
    targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
    targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
    couplingDAO.updateCouplingTypeForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
  }
  
  private static void configModificationFormDynamicToTightCoupledRecord(ILocaleCatalogDAO localeCatalogDAO, ICouplingDAO couplingDAO,
      IBaseEntityDTO sourceBaseEntityDTO, IPropertyDTO propertyDTO, long relationshipIID, ICouplingDTO targetCouplingDTO)
      throws RDBMSException, Exception
  {
    couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
        targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
    
    createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTO, targetCouplingDTO.getSourceEntityIID(),
        targetCouplingDTO.getTargetEntityIID(), propertyDTO, targetCouplingDTO.getLocaleIID());
    
    targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
    targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
    couplingDAO.updateRecordStatusForConflictingValues(targetCouplingDTO, RecordStatus.FORKED);
  }
  
  private static void configModificationFormDynamicToTightCoupledRecordWithValueChange(ILocaleCatalogDAO localeCatalogDAO,
      ICouplingDAO couplingDAO, IBaseEntityDTO sourceBaseEntityDTO, IPropertyDTO propertyDTO, long relationshipIID,
      ICouplingDTO targetCouplingDTO) throws RDBMSException, Exception
  {
    couplingDAO.deleteCoupledRecord(targetCouplingDTO.getSourceEntityIID(), targetCouplingDTO.getTargetEntityIID(),
        targetCouplingDTO.getPropertyIID(), targetCouplingDTO.getLocaleIID(), localeCatalogDAO);
    
    createPropertyRecord(localeCatalogDAO, sourceBaseEntityDTO, targetCouplingDTO.getSourceEntityIID(),
        targetCouplingDTO.getTargetEntityIID(), propertyDTO, targetCouplingDTO.getLocaleIID());
    
    targetCouplingDTO.setCouplingType(CouplingBehavior.TIGHTLY);
    targetCouplingDTO.setCouplingSourceType(CouplingType.TIGHT_CLASSIFICATION);
    couplingDAO.updateRecordStatusForConflictingValues(targetCouplingDTO, RecordStatus.NOTIFIED);
  }
  
  private static void createPropertyRecord(ILocaleCatalogDAO localeCatalogDAO, IBaseEntityDTO sourceBaseEntityDTO, long sourceEntityIID,
      long targetEntityIID, IPropertyDTO propertyDTO, long localeIID) throws Exception
  {
    
    IPropertyRecordDTO propertyRecord = sourceBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
    
    IBaseEntityDAO targetBaseEntityDAO = localeCatalogDAO.openBaseEntity(localeCatalogDAO.getEntityByIID(targetEntityIID));
    IBaseEntityDTO targetBaseEntity = targetBaseEntityDAO.loadPropertyRecords(propertyRecord.getProperty());
    IPropertyRecordDTO targetPropertyRecord = targetBaseEntity.getPropertyRecord(propertyDTO.getPropertyIID());
    if(targetPropertyRecord == null ) {
      if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
        if (localeIID == 0) {
          IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue())
              .asNumber(valueRecord.getAsNumber()).unitSymbol(valueRecord.getUnitSymbol()).build();
          targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
        }
        else {
          String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode();
          IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue())
              .localeID(languageCode).asNumber(valueRecord.getAsNumber()).unitSymbol(valueRecord.getUnitSymbol()).build();
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
  }
  
  private static IBaseEntityDAO getBaseEntityDAO(RDBMSComponentUtils rdbmsComponentUtils, long entityIID, String localeCode) throws Exception
  {
    if (localeCode.equals(null)) {
      return rdbmsComponentUtils.getBaseEntityDAO(entityIID);
    }
    return rdbmsComponentUtils.getBaseEntityDAO(entityIID, localeCode);
  }
  
}
