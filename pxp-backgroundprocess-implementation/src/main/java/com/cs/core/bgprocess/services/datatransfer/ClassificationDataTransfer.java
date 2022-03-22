package com.cs.core.bgprocess.services.datatransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.ClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClassificationDataTransfer extends AbstractBGProcessJob implements IBGProcessJob {
  
  protected int                          nbBatches                     = 1;
  protected int                          batchSize;
  protected int                          currentBatchNo                = 0;
  private IClassificationDataTransferDTO classificationDataTransferDTO = new ClassificationDataTransferDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    
    classificationDataTransferDTO.fromJSON(jobData.getEntryData().toString());
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException, PluginException, Exception
  {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IBaseEntityDAO targetBaseEntityDAO = localeCatlogDAO
        .openBaseEntity(localeCatlogDAO.getEntityByIID(classificationDataTransferDTO.getBaseEntityIID()));
    IBaseEntityDTO targetBaseEntityDTO = targetBaseEntityDAO.getBaseEntityDTO();
    
    Set<IClassifierDTO> classifiers = new HashSet<>(classificationDataTransferDTO.getAddedOtherClassifiers());
    classifiers.addAll(classificationDataTransferDTO.getRemovedOtherClassifiers());
    
    List<String> types = new ArrayList<>();
    List<String> taxonomyIds = new ArrayList<>();
    fillTypesAndTaxonomies(targetBaseEntityDTO, types, taxonomyIds, classifiers, classificationDataTransferDTO.isTranslationChanged());
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(types, taxonomyIds);
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    
    Set<String> removedClassifierCodes = new HashSet<>();
    for (IClassifierDTO classifierDTO : classificationDataTransferDTO.getRemovedOtherClassifiers()) {
      removedClassifierCodes.add(classifierDTO.getCode());
    }
    
    Set<String> addedClassifierCodes = new HashSet<>();
    for (IClassifierDTO classifierDTO : classificationDataTransferDTO.getAddedOtherClassifiers()) {
      addedClassifierCodes.add(classifierDTO.getCode());
    }
    
    Set<String> classifierCodes = new HashSet<>();
    classifierCodes.add(targetBaseEntityDTO.getNatureClassifier().getCode());
    for (IClassifierDTO classifierDTO : classifiers) {
      classifierCodes.add(classifierDTO.getCode());
    }
    
    Map<String, List<IPropertyRecordDTO>> addedClassifierIndependantPropertyMap = new HashMap<>();
    Map<String, List<IPropertyRecordDTO>> addedClassifierDependantPropertyMap = new HashMap<>();
    Map<String, List<IPropertyDTO>> removedClassifierIndependantPropertyMap = new HashMap<>();
    Map<String, List<IPropertyDTO>> removedClassifierDependantPropertyMap = new HashMap<>();
    
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    IConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    // if translation added or removed then just perform coupled operation for
    // language dependent attribute
    if (classificationDataTransferDTO.isTranslationChanged()) {
      fillValueRecordsIfTranslationChanged(targetBaseEntityDAO, configDetails, referencedElements, removedClassifierCodes, classifierCodes,
          addedClassifierDependantPropertyMap, configurationDAO);
    }
    else {
      fillCoupledValueRecords(targetBaseEntityDAO, configDetails, referencedElements, removedClassifierCodes, addedClassifierCodes,
          addedClassifierIndependantPropertyMap, addedClassifierDependantPropertyMap, removedClassifierIndependantPropertyMap,
          removedClassifierDependantPropertyMap, configurationDAO);
      
      fillCoupledTagsRecord(targetBaseEntityDAO, configDetails, referencedElements, removedClassifierCodes, addedClassifierCodes,
          addedClassifierIndependantPropertyMap, removedClassifierIndependantPropertyMap, configurationDAO);
    }

    ILocaleCatalogDAO defalutLocaleCatlogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    // create independent coupled records
    for (String classifierCode : addedClassifierIndependantPropertyMap.keySet()) {
      List<IPropertyRecordDTO> propertyRecords = addedClassifierIndependantPropertyMap.get(classifierCode);
      if (propertyRecords != null && !propertyRecords.isEmpty()) {
        
        IBaseEntityDTO entityDTO = defalutLocaleCatlogDAO.getEntityByID(classifierCode);
        DataTransferUtil.createCLassificationCoupledRecords(propertyRecords, targetBaseEntityDAO,
            entityDTO.getBaseEntityIID(), entityDTO.getNatureClassifier().getClassifierIID(), couplingDAO, 0l);
      }
    }
    
    // create dependent coupled records
    for (String classifierCode : addedClassifierDependantPropertyMap.keySet()) {
      List<IPropertyRecordDTO> propertyRecords = addedClassifierDependantPropertyMap.get(classifierCode);
      
      if (propertyRecords != null && !propertyRecords.isEmpty()) {
        
        Set<String> localeCodes = new HashSet<>();
        
        if (classificationDataTransferDTO.isTranslationChanged()) {
          localeCodes.addAll(classificationDataTransferDTO.getAddedTranslations());
        }
        else {
          localeCodes.addAll(targetBaseEntityDTO.getLocaleIds());
        }
        
        IBaseEntityDTO entityDTO = defalutLocaleCatlogDAO.getEntityByID(classifierCode);
        for (String languageCode : localeCodes) {
          ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfig(languageCode);
          DataTransferUtil.createCLassificationCoupledRecords(propertyRecords, targetBaseEntityDAO, entityDTO.getBaseEntityIID(),
              entityDTO.getNatureClassifier().getClassifierIID(), couplingDAO, languageConfigDTO.getLanguageIID());
        }
      }
    }
    
    for (String classifierCode : removedClassifierCodes) {
      IBaseEntityDAO sourceBaseEntityDAO = null;
      
      List<IPropertyDTO> propertyRecords = removedClassifierIndependantPropertyMap.get(classifierCode);
      if (propertyRecords != null && !propertyRecords.isEmpty()) {
        sourceBaseEntityDAO = defalutLocaleCatlogDAO.openBaseEntity(defalutLocaleCatlogDAO.getEntityByID(classifierCode));
        ;
        DataTransferUtil.deleteCLassificationCoupledRecord(localeCatlogDAO,
            defalutLocaleCatlogDAO.openBaseEntity(defalutLocaleCatlogDAO.getEntityByID(classifierCode)), targetBaseEntityDAO, couplingDAO,
            propertyRecords, 0l);
      }
      
      List<IPropertyDTO> propertyDTOs = removedClassifierDependantPropertyMap.get(classifierCode);
      if (propertyDTOs != null && !propertyDTOs.isEmpty()) {
        if (sourceBaseEntityDAO == null)
          sourceBaseEntityDAO = defalutLocaleCatlogDAO.openBaseEntity(defalutLocaleCatlogDAO.getEntityByID(classifierCode));
        
        Set<String> localeCodes = new HashSet<>();
        
        if (classificationDataTransferDTO.isTranslationChanged()) {
          localeCodes.addAll(classificationDataTransferDTO.getAddedTranslations());
        }
        else {
          localeCodes.addAll(targetBaseEntityDTO.getLocaleIds());
        }
        
        for (String languageCode : localeCodes) {
          ILanguageConfigDTO languageConfigDTO = ConfigurationDAO.instance().getLanguageConfig(languageCode);
          
          DataTransferUtil.deleteCLassificationCoupledRecord(localeCatlogDAO, sourceBaseEntityDAO, targetBaseEntityDAO, couplingDAO,
              propertyDTOs, languageConfigDTO.getLanguageIID());
        }
      }
    }
    
    setCurrentBatchNo(++currentBatchNo);
    IBGProcessDTO.BGPStatus status = null;
    jobData.getProgress().setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    if (jobData.getProgress().getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    
    RuleHandler ruleHandler = new RuleHandler();
    IDataRulesHelperModel dataRules = ruleHandler.prepareDataForMustShouldIdentifiers(configDetails.getReferencedElements(),
        configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    ruleHandler.evaluateDQMustShould(targetBaseEntityDTO.getBaseEntityIID(), dataRules, targetBaseEntityDAO, localeCatlogDAO);
    
    localeCatlogDAO.postUsecaseUpdate(targetBaseEntityDTO.getBaseEntityIID(), EventType.END_OF_TRANSACTION);
    return status;
  }
  
  private void fillCoupledValueRecords(IBaseEntityDAO targetBaseEntityDAO, IGetConfigDetailsForCustomTabModel configDetails,
      Map<String, IReferencedSectionElementModel> referencedElements, Set<String> removedClassifierCodes, Set<String> addedClassifierCodes,
      Map<String, List<IPropertyRecordDTO>> addedClassifierIndependantPropertyMap,
      Map<String, List<IPropertyRecordDTO>> addedClassifierDependantPropertyMap,
      Map<String, List<IPropertyDTO>> removedClassifierIndependantPropertyMap,
      Map<String, List<IPropertyDTO>> removedClassifierDependantPropertyMap, IConfigurationDAO configurationDAO)
      throws RDBMSException, CSFormatException
  {
    String localeID = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID();
    for (IAttribute attribute : configDetails.getReferencedAttributes().values()) {
      String attributeId = attribute.getId();
      IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) referencedElements.get(attributeId);
      if (attributeElement != null && !attributeElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
        IPropertyDTO propertyDTO = configurationDAO.getPropertyByCode(attributeId);
        List<IElementConflictingValuesModel> conflictingSources = attributeElement.getConflictingSources();
        for (IElementConflictingValuesModel conflictSource : conflictingSources) {
            if (removedClassifierCodes.contains(conflictSource.getId())) {
              if (attribute.getIsTranslatable()) {
                List<IPropertyDTO> list = removedClassifierDependantPropertyMap.get(conflictSource.getId());
                if (list == null)
                  removedClassifierDependantPropertyMap.put(conflictSource.getId(), new ArrayList<>());
                removedClassifierDependantPropertyMap.get(conflictSource.getId()).add(propertyDTO);
                
              }
              else {
                List<IPropertyDTO> list = removedClassifierIndependantPropertyMap.get(conflictSource.getId());
                if (list == null)
                  removedClassifierIndependantPropertyMap.put(conflictSource.getId(), new ArrayList<>());
                removedClassifierIndependantPropertyMap.get(conflictSource.getId()).add(propertyDTO);
              }
            }
            else if (addedClassifierCodes.contains(conflictSource.getId())) {
              if (attribute.getIsTranslatable()) {
                fillAttributes(targetBaseEntityDAO, addedClassifierDependantPropertyMap, configurationDAO, attributeElement, propertyDTO,
                    conflictSource, localeID);
              }
              else {
                fillAttributes(targetBaseEntityDAO, addedClassifierIndependantPropertyMap, configurationDAO, attributeElement, propertyDTO,
                    conflictSource, "");
              }
          }
        }
      }
      
    }
  }
  
  private void fillValueRecordsIfTranslationChanged(IBaseEntityDAO targetBaseEntityDAO, IGetConfigDetailsForCustomTabModel configDetails,
      Map<String, IReferencedSectionElementModel> referencedElements, Set<String> removedClassifierCodes, Set<String> classifierCodes,
      Map<String, List<IPropertyRecordDTO>> addedClassifierDependantPropertyMap, IConfigurationDAO configurationDAO)
      throws RDBMSException, CSFormatException
  {
    String localeID = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID();
    for (IAttribute attribute : configDetails.getReferencedAttributes().values()) {
      String attributeId = attribute.getId();
      if (attribute.getIsTranslatable()) {
        IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) referencedElements.get(attributeId);
        if (attributeElement != null && !attributeElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
          IPropertyDTO propertyDTO = configurationDAO.getPropertyByCode(attributeId);
          List<IElementConflictingValuesModel> conflictingSources = attributeElement.getConflictingSources();
          for (IElementConflictingValuesModel conflictSource : conflictingSources) {
            if (classifierCodes.contains(conflictSource.getId())) {
              fillAttributes(targetBaseEntityDAO, addedClassifierDependantPropertyMap, configurationDAO, attributeElement, propertyDTO,
                  conflictSource, localeID);
              
            }
          }
        }
      }
    }
  }
  
  private void fillCoupledTagsRecord(IBaseEntityDAO targetBaseEntityDAO, IGetConfigDetailsForCustomTabModel configDetails,
      Map<String, IReferencedSectionElementModel> referencedElements, Set<String> removedClassifierCodes, Set<String> addedClassifierCodes,
      Map<String, List<IPropertyRecordDTO>> addedClassifierIndependantPropertyMap,
      Map<String, List<IPropertyDTO>> removedClassifierPropertyMap, IConfigurationDAO configurationDAO)
      throws RDBMSException, CSFormatException
  {
    for (ITag tag : configDetails.getReferencedTags().values()) {
      String tagId = tag.getId();
      IReferencedSectionTagModel tagElement = (IReferencedSectionTagModel) referencedElements.get(tagId);
      if (tagElement != null && !tagElement.getCouplingType().equals(CommonConstants.LOOSELY_COUPLED)) {
        IPropertyDTO propertyDTO = configurationDAO.getPropertyByCode(tagId);
        List<IElementConflictingValuesModel> conflictingSources = tagElement.getConflictingSources();
        for (IElementConflictingValuesModel conflictSource : conflictingSources) {
            if (removedClassifierCodes.contains(conflictSource.getId())) {
              List<IPropertyDTO> list = removedClassifierPropertyMap.get(conflictSource.getId());
              if (list == null)
                removedClassifierPropertyMap.put(conflictSource.getId(), new ArrayList<>());
              removedClassifierPropertyMap.get(conflictSource.getId()).add(propertyDTO);
            }
            else if (addedClassifierCodes.contains(conflictSource.getId())){
              IPropertyRecordDTO coupledTagRecord = DataTransferUtil.createClassificationCoupledTagRecord(targetBaseEntityDAO, propertyDTO,
                  configurationDAO.getClassifierByCode(conflictSource.getId()), tagElement.getCouplingType());
              List<IPropertyRecordDTO> list = addedClassifierIndependantPropertyMap.get(conflictSource.getId());
              if (list == null)
                addedClassifierIndependantPropertyMap.put(conflictSource.getId(), new ArrayList<>());
              addedClassifierIndependantPropertyMap.get(conflictSource.getId()).add(coupledTagRecord);
            }
        }
      }
    }
  }
  
  private void fillAttributes(IBaseEntityDAO targetBaseEntityDAO,
      Map<String, List<IPropertyRecordDTO>> addedClassifierIndependantPropertyMap, IConfigurationDAO configurationDAO,
      IReferencedSectionAttributeModel attributeElement, IPropertyDTO propertyDTO, IElementConflictingValuesModel conflictSource, String localeId)
      throws CSFormatException, RDBMSException
  {
    IValueRecordDTO coupledValueRecord = DataTransferUtil.createClassificationCoupledValueRecord(targetBaseEntityDAO, propertyDTO,
        configurationDAO.getClassifierByCode(conflictSource.getId()), attributeElement.getCouplingType(), localeId);
    List<IPropertyRecordDTO> list = addedClassifierIndependantPropertyMap.get(conflictSource.getId());
    if (list == null)
      addedClassifierIndependantPropertyMap.put(conflictSource.getId(), new ArrayList<>());
    addedClassifierIndependantPropertyMap.get(conflictSource.getId()).add(coupledValueRecord);
  }
  
  private IGetConfigDetailsForCustomTabModel getConfigDetails(List<String> types, List<String> taxonomyIds)
      throws CSFormatException, CSInitializationException, Exception, JsonProcessingException
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(types);
    multiclassificationRequestModel.setSelectedTaxonomyIds(taxonomyIds);
    multiclassificationRequestModel.setUserId(classificationDataTransferDTO.getUserId());
    
    IGetConfigDetailsWithoutPermissionsStrategy configDetailForContextualDataTransfer = BGProcessApplication.getApplicationContext()
        .getBean(IGetConfigDetailsWithoutPermissionsStrategy.class);
    
    IGetConfigDetailsForCustomTabModel configDetails = configDetailForContextualDataTransfer.execute(multiclassificationRequestModel);
    return configDetails;
  }
  
  private void fillTypesAndTaxonomies(IBaseEntityDTO baseEntityDTO, List<String> types, List<String> taxonomyIds,
      Set<IClassifierDTO> classifiers, Boolean translationChanged) throws RDBMSException
  {
    if (!classifiers.contains(baseEntityDTO.getNatureClassifier()))
      types.add(baseEntityDTO.getNatureClassifier().getCode());
    if (translationChanged) {
      classifiers.addAll(baseEntityDTO.getOtherClassifiers());
    }
    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types.add(classifier.getCode());
      }
      else if (!classifier.getClassifierType().equals(ClassifierType.UNDEFINED)) {
        taxonomyIds.add(classifier.getCode());
      }
    }
  }
  
}
