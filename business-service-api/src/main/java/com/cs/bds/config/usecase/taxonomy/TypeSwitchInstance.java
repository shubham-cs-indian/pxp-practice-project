package com.cs.bds.config.usecase.taxonomy;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.LanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.dto.RelationshipDataTransferDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.coupling.idto.ILanguageInheritanceDTO;
import com.cs.core.rdbms.coupling.idto.IRelationshipDataTransferDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConifgDetailsForTypeSwitchOfInstanceStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.DataTransferBGPModelBuilder;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TypeSwitchInstance  extends AbstractRuntimeService<IKlassInstanceTypeSwitchModel,IConfigDetailsForSwitchTypeRequestModel> implements ITypeSwitchInstance {
  
  @Autowired
  protected IGetConifgDetailsForTypeSwitchOfInstanceStrategy getConifgDetailsForTypeSwitchOfInstanceStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                              rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                                       configUtil;
  
  @Autowired
  protected ISessionContext                                  context;

  @Autowired protected DataTransferBGPModelBuilder dataTransferBGPModelBuilder;
  
  
  private static final String                                SERVICE_FOR_RDT                                  = "RELATIONSHIP_DATA_TRANSFER";
  
  private static final String                                SERVICE_FOR_CDT                                  = "CONTEXTUAL_DATA_TRANSFER_TASK";
  
  private static final String                                SERVICE                                          = "CLASSIFICATION_DATA_TRANSFER";
  
  private static final BGPPriority                           BGP_PRIORITY                                     = BGPPriority.HIGH;
  
  private static final String                                SERVICE_FOR_LANG_INHERITANCE                     = "LANGUAGE_INHERITANCE_TASK";
  
  @Override
  public IConfigDetailsForSwitchTypeRequestModel executeInternal(IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    
    IBaseEntityDAO baseEntityDao = rdbmsComponentUtils.getEntityDAO(Long.parseLong(typeSwitchModel.getKlassInstanceId()));

    IBaseEntityDTO baseEntityDto = baseEntityDao.getBaseEntityDTO();
    Map<Boolean, List<String>> isClass = BaseEntityUtils.seggregateTaxonomyAndClasses(baseEntityDto.getOtherClassifiers());

    List<String> existingKlassIds = isClass.getOrDefault(true, new ArrayList<>());
    existingKlassIds.add(baseEntityDto.getNatureClassifier().getCode());
    List<String> existingTaxonomyIds = isClass.getOrDefault(false, new ArrayList<>());

    IConfigDetailsForSwitchTypeRequestModel multiClassificationRequestModel = configUtil
        .getConfigRequestModelForTypeSwitchModel(typeSwitchModel, baseEntityDao);
    
    List<String> addedTypes = typeSwitchModel.getAddedSecondaryTypes();
    // String deletedType = typeSwitchModel.getDeletedSecondaryTypes();
    List<String> deletedSecondaryTypes = typeSwitchModel.getDeletedSecondaryTypes();
    List<String> addedTaxonomyIds = typeSwitchModel.getAddedTaxonomyIds();
    // String deletedTaxonomy = typeSwitchModel.getDeletedTaxonomyId();
    List<String> deletedTaxonomyIds = typeSwitchModel.getDeletedTaxonomyIds();
    
    // Existing klassIds
    List<String> klassIds = multiClassificationRequestModel.getKlassIds();
    List<String> cloneOfAddedTypes = new ArrayList<>(addedTypes);
    cloneOfAddedTypes.removeAll(klassIds);
    klassIds.addAll(cloneOfAddedTypes);
    
    if (deletedSecondaryTypes != null) {
      klassIds.removeAll(deletedSecondaryTypes);
    }
    
    List<String> cloneOfAddedTaxonomyIds = new ArrayList<>(typeSwitchModel.getAddedTaxonomyIds());
    List<String> taxonomyIds = new ArrayList<>();
    taxonomyIds.addAll(existingTaxonomyIds);
    taxonomyIds.addAll(cloneOfAddedTaxonomyIds);
    if (deletedTaxonomyIds != null) {
      taxonomyIds.removeAll(deletedTaxonomyIds);
    }
    
    multiClassificationRequestModel.setKlassIds(klassIds);
    multiClassificationRequestModel.setSelectedTaxonomyIds(taxonomyIds);
    multiClassificationRequestModel.setTaxonomyIds(taxonomyIds);
    multiClassificationRequestModel.setTaxonomyIdsForDetails(taxonomyIds);
    
    IGetConfigDetailsForCustomTabModel multiClassificationDetails = getConifgDetailsForTypeSwitchOfInstanceStrategy
        .execute(multiClassificationRequestModel);
    List<String> klassIdsToAdd =  multiClassificationDetails.getKlassIdsToAdd();
    List<String> taxonomyIdsToAdd =  multiClassificationDetails.getTaxonomyIdsToAdd();
    cloneOfAddedTypes.addAll(klassIdsToAdd);
    cloneOfAddedTaxonomyIds.addAll(taxonomyIdsToAdd);
    addedTaxonomyIds.addAll(taxonomyIdsToAdd);
    multiClassificationRequestModel.getKlassIds().addAll(klassIdsToAdd);
    multiClassificationRequestModel.getTaxonomyIds().addAll(taxonomyIdsToAdd);
    
    Set<IClassifierDTO> addedClassifiers = new HashSet<IClassifierDTO>();
    Set<IClassifierDTO> removedClassifiers = new HashSet<IClassifierDTO>();
    
    if (!cloneOfAddedTypes.isEmpty()) {
      for (String type : cloneOfAddedTypes) {
        IReferencedKlassDetailStrategyModel referencedKlass = multiClassificationDetails.getReferencedKlasses().get(type);
        IClassifierDTO classifierDTO = baseEntityDao.newClassifierDTO(referencedKlass.getClassifierIID(), referencedKlass.getCode(),
            IClassifierDTO.ClassifierType.CLASS);
        baseEntityDao.addClassifiers(classifierDTO);
        addedClassifiers.add(classifierDTO);
      }
    }
    if (addedTaxonomyIds != null) {
      for (String addedTaxonomy : addedTaxonomyIds) {
        
        Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = multiClassificationDetails.getReferencedTaxonomies();
        IReferencedArticleTaxonomyModel referencedTaxonomy = referencedTaxonomies.get(addedTaxonomy);
        
        if (!existingTaxonomyIds.contains(referencedTaxonomy.getId())) {
          
          IClassifierDTO classifierDTO = baseEntityDao.newClassifierDTO(referencedTaxonomy.getClassifierIID(), referencedTaxonomy.getCode(),
              IClassifierDTO.ClassifierType.TAXONOMY);
          baseEntityDao.addClassifiers(classifierDTO);
          addedClassifiers.add(classifierDTO);
        }
      }
    }
    
    boolean executeRuntimeCleanup = false;
    if (deletedTaxonomyIds != null && !deletedTaxonomyIds.isEmpty()) {
      executeRuntimeCleanup = true;
      for (String deletedTaxonomy : deletedTaxonomyIds) {
        
        Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = multiClassificationDetails.getReferencedTaxonomies();
        IClassifierDTO classifierDto = ConfigurationDAO.instance().getClassifierByCode(deletedTaxonomy);
        baseEntityDao.removeClassifiers(classifierDto);
        removedClassifiers.add(classifierDto);
        removeParentTaxonomyIfExist(referencedTaxonomies, baseEntityDao, addedTaxonomyIds);
      }
    }
    if (deletedSecondaryTypes != null && !deletedSecondaryTypes.isEmpty()) {
      executeRuntimeCleanup = true;
      for (String deletedType : deletedSecondaryTypes) {
        IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(deletedType);
        baseEntityDao.removeClassifiers(classifierDTO);
        removedClassifiers.add(classifierDTO);
      }
    }
    
    createAndRemovePropertyRecords(multiClassificationDetails, baseEntityDto, baseEntityDao);
    
    long baseEntityIID = baseEntityDto.getBaseEntityIID();
    
    // This will clean the data which is no longer related with content after removal of NN class/taxonomy.
    if (executeRuntimeCleanup) {
      dataTransferBGPModelBuilder.prepareDataForRuntimeCleanupTask(baseEntityIID, rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO(), klassIds, taxonomyIds, deletedSecondaryTypes,
          deletedTaxonomyIds);
    }
    
    if(typeSwitchModel.getIsResolved()) {
      ITaxonomyInheritanceDAO taxonomyInheritanceDAO = rdbmsComponentUtils.getTaxonomyInheritanceDAO(rdbmsComponentUtils.getTaxonomyInheritanceDTO(baseEntityIID));
      taxonomyInheritanceDAO.resolveTaxonmyConflict();
    }
    
    dataTransferBGPModelBuilder.initiateTaxonomyInheritance(typeSwitchModel, multiClassificationDetails, baseEntityIID);
    
    dataTransferBGPModelBuilder.initiateRelationshipInheritanceSwitchType(typeSwitchModel, existingKlassIds,existingTaxonomyIds, cloneOfAddedTaxonomyIds , addedTypes );
    
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    dataTransferBGPModelBuilder.initiateClassificationDataTransfer(localeCatalogDAO, baseEntityIID, addedClassifiers, removedClassifiers);
    prepareDataForRelationshipDataTransferTask(baseEntityDao.getBaseEntityDTO().getBaseEntityIID(), 
        rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO());

    dataTransferBGPModelBuilder.prepareDataForContextualDataTransferTask(baseEntityIID, rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO());
    
    initiateBackgroundTaskForLanguageInheritance(multiClassificationDetails, baseEntityIID);
    
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandlingForSave(baseEntityDao, rdbmsComponentUtils.getLocaleCatlogDAO(), true,
        multiClassificationDetails.getReferencedElements(), multiClassificationDetails.getReferencedAttributes(),
        multiClassificationDetails.getReferencedTags());
    
    return multiClassificationRequestModel;
  }


  
  private void initiateBackgroundTaskForLanguageInheritance(IGetConfigDetailsForCustomTabModel configDetails, long baseEntityIID)
      throws Exception
  {
    List<Long> dependentPropertyIIDs = new ArrayList<>();
    
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    for (IAttribute referencedAttribute : referencedAttributes.values()) {
      if (referencedAttribute.getIsTranslatable()) {
        dependentPropertyIIDs.add(referencedAttribute.getPropertyIID());
      }
    }
    
    if(dependentPropertyIIDs.isEmpty()) {
      return;
    }
    
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    List<String> languageCodes = couplingDAO.getlanguageCodesByBaseEntityIIDs(baseEntityIID, baseEntityIID);
    
    ILanguageInheritanceDTO languageInheritanceDTO = new LanguageInheritanceDTO();
    languageInheritanceDTO.setDependentPropertyIIDs(dependentPropertyIIDs);
    languageInheritanceDTO.setBaseEntityIID(baseEntityIID);
    
    for(String languageCode : languageCodes) {
      languageInheritanceDTO.getLocaleIIDs().add(languageCode);
    }
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    languageInheritanceDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    languageInheritanceDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    languageInheritanceDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    languageInheritanceDTO.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_LANG_INHERITANCE, "", userPriority,
        new JSONContent(languageInheritanceDTO.toJSON()));
    
    
  }
  
  public static void createTagRecordAtTargetEntity(Long targetEntityIID, IBaseEntityDAO baseEntityDAO, ICouplingDAO couplingDAO,
      ICouplingDTO couplingDTO, IBaseEntityDAO targetBaseEntityDAO) throws RDBMSException, Exception
  {
    List<ICouplingDTO> coupledConflictingValue = couplingDAO.getCoupledConflictingValue(couplingDTO);
    
    if (coupledConflictingValue.size() == 1) {
      ITagsRecordDTO TagRecord = (ITagsRecordDTO) baseEntityDAO.getBaseEntityDTO().
          getPropertyRecord(couplingDTO.getPropertyIID());
      ITagsRecordDTO targetTagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(TagRecord.getProperty()).build();
      Set<ITagDTO> tagDTO = targetTagRecordDTO.getTags();
      
      targetTagRecordDTO.setTags(tagDTO.toArray(new ITagDTO[tagDTO.size()]));
      targetBaseEntityDAO.createPropertyRecords(targetTagRecordDTO);
    }
  }
  
  public void createAttributeAtTarget(Long targetEntityIID, IBaseEntityDAO sourceBaseEntityDAO, ICouplingDAO couplingDAO,
      ICouplingDTO couplingDTO, IBaseEntityDAO targetBaseEntityDAO, String targetLocaleCode) throws RDBMSException, CSFormatException, Exception
 {
   List<ICouplingDTO> coupledConflictingValue = couplingDAO.getCoupledConflictingValue(couplingDTO);
   IPropertyDTO propertyDTO = ConfigurationDAO.instance().getPropertyByIID(couplingDTO.getPropertyIID());
   if (coupledConflictingValue.size() == 1) {
     IBaseEntityDTO loadPropertyRecords = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
     
     IValueRecordDTO valueRecordDTO = (IValueRecordDTO) loadPropertyRecords.getPropertyRecord(propertyDTO.getPropertyIID());
     
     if(coupledConflictingValue.get(0).getLocaleIID() == 0l) {
       
       IValueRecordDTO targetValueRecord = targetBaseEntityDAO
           .newValueRecordDTOBuilder(valueRecordDTO.getProperty(), valueRecordDTO.getValue()).build();
       
       targetBaseEntityDAO.createPropertyRecords(targetValueRecord);
     }else {
       
       IValueRecordDTO targetValueRecord = targetBaseEntityDAO
           .newValueRecordDTOBuilder(valueRecordDTO.getProperty(), valueRecordDTO.getValue()).localeID(targetLocaleCode).build();
       
       targetBaseEntityDAO.createPropertyRecords(targetValueRecord);
     }
   }
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
    entryData.setUserId(rdbmsComponentUtils.getUserID());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_RDT, "", userPriority1, new JSONContent(entryData.toJSON()));
  }

   
  protected void createAndRemovePropertyRecords(IGetConfigDetailsModel configDetails, IBaseEntityDTO baseEntityDto,
      IBaseEntityDAO baseEntityDao) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDao, configDetails, PropertyRecordType.DEFAULT, rdbmsComponentUtils.getLocaleCatlogDAO());
    ILocaleCatalogDAO localCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    long baseEntityIID = baseEntityDto.getBaseEntityIID();
    Map<Long, List<IValueRecordDTO>> baseEntityIIDVsValueRecords = localCatalogDAO.getAllValueRecords(baseEntityIID);
    Map<Long, List<ITagsRecordDTO>> baseEntityIIDVsTagsRecords = localCatalogDAO.getAllTagsRecords(baseEntityIID);
    
    List<IPropertyRecordDTO> addedPropertyRecordsDTO = new ArrayList<>();
    List<IPropertyRecordDTO> deletedPropertyRecordsDTO = new ArrayList<>();
    Map<Long, List<IPropertyRecordDTO>> attributeRecords = new HashMap<>();
    Map<Long, IPropertyRecordDTO> tagRecords = new HashMap<>();
    
    // Filling attribute records map and tag records map using value records & tag records list.
    fillAttributesAndTagsRecord(baseEntityIIDVsValueRecords.get(baseEntityIID),
        baseEntityIIDVsTagsRecords.get(baseEntityIID), attributeRecords, tagRecords);
    
    // Adding new property records & Removing attributes to keep from
    // attributeRecords and adding remaining attributeRecords to deletedPropertyRecordsDTO.
    createAttributePropertyRecordInstance(propertyRecordBuilder, configDetails, addedPropertyRecordsDTO, attributeRecords, deletedPropertyRecordsDTO);

    createTagPropertyRecordInstance(propertyRecordBuilder, configDetails, addedPropertyRecordsDTO, tagRecords, deletedPropertyRecordsDTO);

    if (!deletedPropertyRecordsDTO.isEmpty()) {
      deleteDataTransferPropertiesIfExist(baseEntityDto.getBaseEntityIID(), deletedPropertyRecordsDTO);
      baseEntityDao.deletePropertyRecords(deletedPropertyRecordsDTO.toArray(new IPropertyRecordDTO[deletedPropertyRecordsDTO.size()]));
    }
    
    if(!addedPropertyRecordsDTO.isEmpty()) {
      baseEntityDao.createPropertyRecords(addedPropertyRecordsDTO.toArray(new IPropertyRecordDTO[addedPropertyRecordsDTO.size()]));
    }
  }
  
  private void deleteDataTransferPropertiesIfExist(long baseEntityIID, List<IPropertyRecordDTO> deletedPropertyRecordsDTO) throws Exception
  {
    
    ICouplingDAO couplingDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openCouplingDAO();
    List<ICouplingDTO> conflictingValues = couplingDAO.getConflictingValues(baseEntityIID);
    List<Long> propertyIIDs = new ArrayList<>();
    
    for (IPropertyRecordDTO propertyRecord : deletedPropertyRecordsDTO) {
      propertyIIDs.add(propertyRecord.getProperty().getPropertyIID());
    }
    
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    for (ICouplingDTO couplingDTO : conflictingValues) {
      
      CouplingType couplingSourceType = couplingDTO.getCouplingSourceType();
      if(couplingSourceType == CouplingType.DYN_CLASSIFICATION || couplingSourceType == CouplingType.TIGHT_CLASSIFICATION) {
        continue;
      }
      
      if (!propertyIIDs.contains(couplingDTO.getPropertyIID())) {
        continue;
      }
      
      IPropertyDTO propertyDTO = configurationDAO.getPropertyByIID(couplingDTO.getPropertyIID());
      
      if (couplingDTO.getLocaleIID() == 0l) {
        createPropertyRecordForIndependentProperty(baseEntityIID, couplingDAO, couplingDTO, propertyDTO);
      }
      else {
        createPropertyRecordForDependentProperty(baseEntityIID, couplingDAO, couplingDTO);
      }
      
      couplingDAO.deleteCoupledRecord(couplingDTO.getSourceEntityIID(), couplingDTO.getTargetEntityIID(), couplingDTO.getPropertyIID(),
          couplingDTO.getLocaleIID(), rdbmsComponentUtils.getLocaleCatlogDAO());
      
      couplingDAO.deleteCouplingRecordFromConflictingValues(couplingDTO);
      
    }
  }
  
  private void createPropertyRecordForDependentProperty(long baseEntityIID, ICouplingDAO couplingDAO, ICouplingDTO couplingDTO)
      throws RDBMSException, Exception, CSFormatException
  {
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    IBaseEntityDAO sourceEntityDAO;
    
    if (couplingDTO.getCouplingSourceType() == CouplingType.LANG_INHERITANCE) {
      return;
    }
    
    sourceEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getSourceEntityIID(),
        configurationDAO.getLanguageConfigByLanguageIID(couplingDTO.getLocaleIID()).getLanguageCode());
    String targetLocaleCode = configurationDAO.getLanguageConfigByLanguageIID(couplingDTO.getLocaleIID()).getLanguageCode();
    IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getTargetEntityIID(), targetLocaleCode);
    
    if (couplingDTO.getSourceEntityIID() == baseEntityIID && couplingDTO.getRecordStatus() == RecordStatus.COUPLED) {
      createAttributeAtTarget(couplingDTO.getTargetEntityIID(), sourceEntityDAO, couplingDAO, couplingDTO, targetBaseEntityDAO,
          targetLocaleCode);
    }
  }
  
  private void createPropertyRecordForIndependentProperty(long baseEntityIID, ICouplingDAO couplingDAO, ICouplingDTO couplingDTO,
      IPropertyDTO propertyDTO) throws Exception, RDBMSException, CSFormatException
  {
    IBaseEntityDAO sourceEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getSourceEntityIID());
    IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(couplingDTO.getTargetEntityIID());
    
    if (couplingDTO.getSourceEntityIID() == baseEntityIID && couplingDTO.getRecordStatus() == RecordStatus.COUPLED) {
      
      if (propertyDTO.getSuperType() == SuperType.ATTRIBUTE) {
        createAttributeAtTarget(couplingDTO.getTargetEntityIID(), sourceEntityDAO, couplingDAO, couplingDTO, targetBaseEntityDAO, "");
      }
      else {
        createTagRecordAtTargetEntity(couplingDTO.getTargetEntityIID(), sourceEntityDAO, couplingDAO, couplingDTO, targetBaseEntityDAO);
      }
    }
  }
  
  public void fillAttributesAndTagsRecord(List<IValueRecordDTO> valueRecords, List<ITagsRecordDTO> tagsRecords, Map<Long, List<IPropertyRecordDTO>> attributeRecords,
      Map<Long, IPropertyRecordDTO> tagRecords)
  {
    if (valueRecords != null) {
      for(IValueRecordDTO valueRecord : valueRecords) {

        long propertyIId = valueRecord.getProperty().getPropertyIID();

        if (attributeRecords.containsKey(propertyIId)) {
          attributeRecords.get(propertyIId).add(valueRecord);
        }
        else {
          List<IPropertyRecordDTO> propertyRecordsList = new ArrayList<IPropertyRecordDTO>();
          propertyRecordsList.add(valueRecord);
          attributeRecords.put(propertyIId, propertyRecordsList);
        }
      }
    }
    
    if (tagsRecords != null) {
      for (ITagsRecordDTO tagsRecord : tagsRecords) {
        
        long propertyIId = tagsRecord.getProperty().getPropertyIID();
        tagRecords.put(propertyIId, tagsRecord);
      }
    }
  }
  
  protected void createAttributePropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsModel configDetails, List<IPropertyRecordDTO> addedPropertyRecordsDTO,
      Map<Long, List<IPropertyRecordDTO>> attributeRecords, List<IPropertyRecordDTO> deletedPropertyRecordsDTO) throws Exception
  {
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    configDetails.getReferencedAttributes().values().forEach(referencedAttribute -> {
      try {
        long propertyIID = referencedAttribute.getPropertyIID();
            // property record is not present in db. Create value record for the
            // referenced attribute. (with default value if any) 
        if (!attributeRecords.containsKey(propertyIID)) {
          IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
          if (dto != null) {
            addedPropertyRecordsDTO.add(dto);
          }
        } // Property Record already exist. Check whether the value record needs to be deleted.
        else {
          String attributeCode = referencedAttribute.getCode();
          if (CommonConstants.MANDATORY_ATTRIBUTES_LIST.contains(attributeCode)) {
            attributeRecords.remove(propertyIID);
            return;
          }

          IReferencedSectionElementModel referencedElementAttribute = referencedElements.get(attributeCode);
          if (referencedElementAttribute != null) {

            if (referencedElementAttribute.getIsSkipped()) {
              return;
            }

            List<IPropertyRecordDTO> valueRecordDTOs = attributeRecords.get(referencedAttribute.getPropertyIID());
            String attributeVariantContext = referencedElementAttribute.getAttributeVariantContext();
            long contextualObjectIID = ((IValueRecordDTO) valueRecordDTOs.get(0)).getContextualObject().getContextualObjectIID();
            if ((attributeVariantContext != null && contextualObjectIID != 0) || (attributeVariantContext == null && contextualObjectIID == 0)) {
              attributeRecords.remove(propertyIID);
            }
          }
        }
      }
      catch (Exception e) {
        new RuntimeException(e);
      }
    });

    // Add all Property Records against property Id's from attributeRecords.
    // These property Id's does not present in referencedAttribute map as they
    // belongs to NN Class/Taxonomy removed from Content.
    attributeRecords.values().forEach(propertyRecordsToDelete -> {
      deletedPropertyRecordsDTO.addAll(propertyRecordsToDelete);
    });
    
    if (!deletedPropertyRecordsDTO.isEmpty()) {
      excludeAssetSpecificAttributes(deletedPropertyRecordsDTO);
    }
  }
  
  private void excludeAssetSpecificAttributes(List<IPropertyRecordDTO> deletedPropertyRecordsDTO)
  {
    List<String> assetSpecificAttributes = Arrays.asList(SystemLevelIds.START_DATE_ATTRIBUTE, SystemLevelIds.END_DATE_ATTRIBUTE, SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
    Iterator<IPropertyRecordDTO> iterator = deletedPropertyRecordsDTO.iterator();
    while (iterator.hasNext()) {
      IPropertyRecordDTO propertyRecordsDTO = iterator.next();
      if (assetSpecificAttributes.contains(propertyRecordsDTO.getProperty().getCode())){
        iterator.remove();
      }
    }
  }

  protected void createTagPropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder,
      IGetConfigDetailsModel configDetails, List<IPropertyRecordDTO> addedPropertyRecordsDTO, Map<Long, IPropertyRecordDTO> tagRecords, List<IPropertyRecordDTO> deletedPropertyRecordsDTO)
      throws Exception
  {
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    configDetails.getReferencedTags().values().forEach(referencedTags -> {
      try {
        long tagPropertyIID = referencedTags.getPropertyIID();
        if (!tagRecords.containsKey(tagPropertyIID)) {
          IPropertyRecordDTO dto = propertyRecordBuilder.createTagsRecord(referencedTags);
          if (dto != null) {
            addedPropertyRecordsDTO.add(dto);
          }
        }
        else {
          if (CommonConstants.MANDATORY_TAG_TYPE_LIST.contains(referencedTags.getTagType())) {
            tagRecords.remove(tagPropertyIID);
            return;
          }

          IReferencedSectionElementModel referencedElementTag = referencedElements.get(referencedTags.getCode());
          if (referencedElementTag != null) {

            if (referencedElementTag.getIsSkipped()) {
              return;
            }
            tagRecords.remove(tagPropertyIID);
          }
        }
      }
      catch (Exception e) {
        new RuntimeException(e);
      }
    });

    // Add all Property Records against property Id's from tagRecords.
    // These property Id's does not present in referencedTag map as they
    // belongs to NN Class/Taxonomy removed from Content.
    tagRecords.values().forEach(propertyRecordsToDelete -> {
      deletedPropertyRecordsDTO.add(propertyRecordsToDelete);
    });
  }
  
  public void removeParentTaxonomyIfExist(Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies, IBaseEntityDAO baseEntityDao,
      List<String> addedTaxonomyIds) throws RDBMSException
  {
    for (String addedTaxonomy : addedTaxonomyIds) {
      
      IReferencedArticleTaxonomyModel referencedTaxonomy = referencedTaxonomies.get(addedTaxonomy);
      
      if (referencedTaxonomy != null) {
        IReferencedTaxonomyParentModel parent = referencedTaxonomy.getParent();
        
        while (!parent.getId().equals("-1")) {
          IClassifierDTO classifierDTO = baseEntityDao.newClassifierDTO(0, "", IClassifierDTO.ClassifierType.TAXONOMY);
          baseEntityDao.removeClassifiers(classifierDTO);
          parent = parent.getParent();
        }
      }
    }
  }
  
  @Override
  public WorkflowUtils.UseCases getUsecase()
  {
      return WorkflowUtils.UseCases.CLASSIFICATIONSAVE; //SWITCHTYPE in old PXP
  }
}

