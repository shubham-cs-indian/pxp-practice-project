package com.cs.core.bgprocess.services.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.config.standard.IConfigMap;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BaseEntityBulkUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.datarules.DataRuleUtil;
import com.cs.core.bgprocess.utils.BgprocessUtils;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.data.Text;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IChangedPropertiesDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class BaseEntityBulkUpdate extends AbstractBaseEntityProcessing {
  
  private final BaseEntityBulkUpdateDTO bulkUpdateDTO = new BaseEntityBulkUpdateDTO();
  // property records information issued from specification
  private final Map<Long, IPropertyRecordDTO> sourceRecordsMap = new HashMap<>();
  private final Set<IPropertyDTO> sourceProperties = new HashSet<>();
  // Current list of property IIDs that are applicable and that must be updated / added
  private final Set<Long> applicablePropertyIIDs = new HashSet<>();
  private final Set<Long> updatedPropertyIIDs = new HashSet<>();
  private final Set<Long> addedPropertyIIDs = new HashSet<>();
  private final Set<Long> propertiesToSkip = new HashSet<>();
  private final Set<Long> versionableAttributeIds = new HashSet<>();
  private final Set<Long> versionableTagIds = new HashSet<>();
  // config details of attributes
  private Map<String,IAttribute> referencedAttributes = new HashMap<>();
  private  Map<String,IReferencedSectionElementModel> referencedElements = new HashMap<>();
  private JSONObject klassIdVsType  = new JSONObject();
  private Map<String, ITag> referencedTags = new HashMap<>();
//  private JSONObject referencedTaxonomies = new JSONObject();
//  private JSONObject referencedKlasses = new JSONObject();
  private Integer numberOfVersionsToMaintain = 0;
  RDBMSComponentUtils rdbmsComponentUtils = BGProcessApplication.getApplicationContext().getBean(RDBMSComponentUtils.class);
//  private JSONObject referencedTaxonomies = new JSONObject();
//  private JSONObject referencedKlasses = new JSONObject();
  private WorkflowUtils workflowUtils;
  private GoldenRecordUtils goldenRecordUtils;

  /**
   * @param currentEntity
   * @return the list of applicable property IIDs from entity's classification information
   * @throws Exception
   * @throws JsonProcessingException
   * @throws CSFormatException
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private void collectApplicablePropertyIIDs ( IBaseEntityDTO currentEntity, Boolean isTranslationPresent)
          throws JsonProcessingException, JsonParseException, JsonMappingException, IOException, Exception {
    
    // Collect classifiers in order to know applicable properties 
    Map<String, Object> requestModel = DataRuleUtil.prepareConfigBulkPropagtionRequestModel(currentEntity, bulkUpdateDTO.getUserId());
    applicablePropertyIIDs.clear();
    try {
      JSONObject response
              = CSConfigServer.instance().request(requestModel, "GetConfigDetailsForBulkPropagation", null);

      IConfigDetailsForBulkPropagationResponseModel configDetails = ObjectMapperUtil
          .readValue(ObjectMapperUtil.writeValueAsString(response), ConfigDetailsForBulkPropagationResponseModel.class);

      manageKlassInstancePermissions(configDetails);

      referencedElements  = configDetails.getReferencedElements();

      referencedAttributes = configDetails.getReferencedAttributes();
      referencedAttributes.keySet().forEach(key -> {
        IAttribute attribute = referencedAttributes.get(key);
        IReferencedSectionElementModel attributeElement = referencedElements.get(key);
        if(attributeElement != null) {
          Boolean isDisabled = (Boolean) attributeElement.getIsDisabled();
          if(attributeElement.getAttributeVariantContext() == null && !attributeElement.getIsSkipped()
              && !isDisabled) {
            if(attribute.getIsTranslatable()) {
              if(isTranslationPresent) {
                applicablePropertyIIDs.add(attribute.getPropertyIID());
              }
            } else {
              applicablePropertyIIDs.add(attribute.getPropertyIID());
            }
          }
          if(attributeElement.getIsSkipped() || isDisabled) {
            propertiesToSkip.add(attribute.getPropertyIID());
          }
        }
      });
      referencedTags = configDetails.getReferencedTags();
      referencedTags.keySet().forEach(key -> {
        ITag tag = referencedTags.get(key);
        IReferencedSectionElementModel attributeElement = referencedElements.get(key);
        if (attributeElement != null) {
          Boolean isDisabled = (Boolean) attributeElement.getIsDisabled();
          if (!isDisabled) {
            applicablePropertyIIDs.add((Long) tag.getPropertyIID());
          }
          if (attributeElement.getIsSkipped() || isDisabled) {
            propertiesToSkip.add((Long) tag.getPropertyIID());
          }
        }
      });
      JSONArray versionableAttr = (JSONArray) response.get("versionableAttributes");
      for (Object attr: versionableAttr) {
        versionableAttributeIds.add(ConfigurationDAO.instance().getPropertyByCode((String)attr).getPropertyIID());
      }

      JSONArray versionableTags= (JSONArray) response.get("versionableTags");
      for (Object tag: versionableTags) {
        versionableTagIds.add(ConfigurationDAO.instance().getPropertyByCode((String)tag).getPropertyIID());
      }
      
      numberOfVersionsToMaintain = configDetails.getNumberOfVersionsToMaintain();
      

    } catch (CSInitializationException ex) {
      throw new RDBMSException(0, "Initialization Error", ex.getMessage());
    }
  }

/**
 * @param sourceRecordsMap the map of modified records per property IID (as provided by bulk update specification)
 * @param baseEntityDao service interface of the current entity
 * @return the list of property records that have to be updated
 */
private List<IPropertyRecordDTO> getUpdatedPropertyRecords(
        Map<Long, IPropertyRecordDTO> sourceRecordsMap, IBaseEntityDAO baseEntityDao,
        List<String> changedAttributeIds, List<String> changedTagIds, AtomicBoolean shouldCreateRevision) {

  List<IPropertyRecordDTO> updatedPropertyRecords = new ArrayList<>();
  updatedPropertyIIDs.clear();
  // Navigate through the lastly loaded records and prepare a record to be updated by comparison with source records
  IBaseEntityDTO currentEntity = baseEntityDao.getBaseEntityDTO();

  for(IPropertyRecordDTO currentRecord: currentEntity.getPropertyRecords()) {
      IPropertyRecordDTO sourceRecord = sourceRecordsMap.get(currentRecord.getProperty().getPropertyIID());
      IPropertyDTO property = sourceRecord.getProperty();
      if(propertiesToSkip.contains(property.getPropertyIID())) {
        continue;
        }
      IReferencedSectionElementModel attributeElement = referencedElements.get(property.getPropertyCode());
      if (sourceRecord != null && !propertiesToSkip.contains(property.getPropertyIID())) {
        updatedPropertyIIDs.add(currentRecord.getProperty().getPropertyIID());
      }
      if (checkIfDynamicallyCoupledProperty(property)) {
        continue;
      }
      if (sourceRecord != null && currentRecord.getProperty().getSuperType() == SuperType.ATTRIBUTE) {
        IValueRecordDTO currentValueRecord = (IValueRecordDTO) currentRecord;
        if (currentValueRecord.getContextualObject().isNull()
            && !propertiesToSkip.contains(currentValueRecord.getProperty().getPropertyIID())) { // Bulk update is only available for non-contextual data

          IValueRecordDTO sourceValueRecord = (IValueRecordDTO) sourceRecord;
          String sourceValue = sourceValueRecord.getValue();
          String sourceValueAsHtml = sourceValueRecord.getAsHTML();
          IValueRecordDTO propertyToUpdate = baseEntityDao.newValueRecordDTOBuilder(currentRecord.getProperty(), sourceValue)
              .valueIID(currentValueRecord.getValueIID()).localeID(currentValueRecord.getLocaleID())
              .isVersionable(((IReferencedSectionAttributeModel) attributeElement).getIsVersionable()).build();
			createOrUpdateValueDTO(sourceValue, sourceValueAsHtml, propertyToUpdate);
            IPropertyRecordDTO updatedRecord = (IPropertyRecordDTO) propertyToUpdate;
            if(versionableAttributeIds.contains(updatedRecord.getProperty().getPropertyIID())){
              shouldCreateRevision.set(true);;
            }
            updatedPropertyRecords.add(updatedRecord);
            changedAttributeIds.add(property.getCode());
        }
      }
       else if ( sourceRecord != null  && currentRecord.getProperty().getSuperType() == SuperType.TAGS ) {
          ITagsRecordDTO tagsRecord = (ITagsRecordDTO) currentRecord;
          ITagsRecordDTO sourceTagRecords = (ITagsRecordDTO) sourceRecord;
          tagsRecord.setTags( sourceTagRecords .getTags().toArray(new ITagDTO[0]));
          if(versionableTagIds.contains(tagsRecord.getProperty().getPropertyIID())){
            shouldCreateRevision.set(true);;
          }
        updatedPropertyRecords.add(tagsRecord);
        changedTagIds.add(property.getCode());
      }
    }
    return updatedPropertyRecords;
  }

  private Boolean checkIfDynamicallyCoupledProperty(IPropertyDTO propertyDTO)
  {
    String code = propertyDTO.getCode();
    if (referencedElements.containsKey(code)) {
      IReferencedSectionElementModel referenceElement = referencedElements.get(code);
      String couplingType = (String) referenceElement.getCouplingType();
      if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
        return true;
      }
    }
    return false;
  }

 /**
 * This method set value asNumber/asHTML/UnitSymbol for corresponding attributes
 * @param : string value value of attribute
 * @param propertyRecord
 */
private void createOrUpdateValueDTO(String value, String valueAsHtml, IValueRecordDTO propertyRecord) {
  PropertyType propertyType = propertyRecord.getProperty().getPropertyType();

  IAttribute referencedAttribute = referencedAttributes
      .get(propertyRecord.getProperty().getPropertyCode());
  IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) referencedElements.get(propertyRecord.getProperty().getPropertyCode());
  
  
  switch (propertyType) {
   case DATE:
   case NUMBER:
     if (!StringUtils.isEmpty(value)) {
         propertyRecord.setAsNumber(Double.parseDouble(value));
     }
     break;

   case HTML:
     propertyRecord.setAsHTML(valueAsHtml);
     break;

   case MEASUREMENT:
   case PRICE:
     String unitSymbol = attributeElement.getDefaultUnit() != null
     ? attributeElement.getDefaultUnit()
     : ((IUnitAttribute) referencedAttribute).getDefaultUnit();
     propertyRecord.setUnitSymbol(unitSymbol);
     if (!StringUtils.isEmpty(value)) {
       propertyRecord.setAsNumber(Double.parseDouble(value));
     }
     break;

   default:
     //set valueAsHtml for standard attributes(HTML type) as propertyType for all standard attributes is STANDARD_ATTRIBUTE
     propertyRecord.setAsHTML(valueAsHtml);
  }
}

private List<IPropertyRecordDTO> getAddedPropertyRecords(
      Map<Long, IPropertyRecordDTO> sourceRecordsMap, IBaseEntityDAO baseEntityDao,
      List<String> changedAttributeIds, List<String> changedTagIds,  AtomicBoolean shouldCreateRevision) {

  List<IPropertyRecordDTO> addedPropertyRecords = new ArrayList<>();
  addedPropertyIIDs.clear();
  IBaseEntityDTO currentEntity = baseEntityDao.getBaseEntityDTO();
  for (IPropertyDTO sourceProperty : sourceProperties) {
    if ( updatedPropertyIIDs.contains(sourceProperty.getPropertyIID())
            || !applicablePropertyIIDs.contains(sourceProperty.getPropertyIID())) {
      continue; // excluded updated and non applicable properties
    }

    IPropertyRecordDTO sourceRecord = sourceRecordsMap.get(sourceProperty.getPropertyIID());
    IPropertyDTO propertyDTO = sourceRecord.getProperty();
    if(checkIfDynamicallyCoupledProperty(propertyDTO)) {
      continue;
    }
    if (sourceProperty.getSuperType() == SuperType.ATTRIBUTE ) {

    IAttribute referencedAttribute =  referencedAttributes.get(sourceRecord.getProperty().getPropertyCode());
    	IReferencedSectionAttributeModel attributeElement = (IReferencedSectionAttributeModel) referencedElements.get(sourceRecord.getProperty().getPropertyCode());
      IValueRecordDTO addedValueRecord = (IValueRecordDTO) sourceRecord;
      String value = addedValueRecord.getValue();
      String valueAsHtml = addedValueRecord.getAsHTML();
      String localeId = "";

      if(referencedAttribute.getIsTranslatable()) {
        localeId = addedValueRecord.getLocaleID();
      }
      IValueRecordDTO newValueRecordDTO = baseEntityDao
          .newValueRecordDTOBuilder(sourceProperty, value)
          .localeID(localeId)
          .isVersionable(attributeElement.getIsVersionable())
            .build();

        createOrUpdateValueDTO(value, valueAsHtml, newValueRecordDTO);
//		IPropertyRecordDTO valueRecord  = newValueRecordDTO;
        addedPropertyRecords.add(newValueRecordDTO);
        addedPropertyIIDs.add( sourceProperty.getIID());
        changedAttributeIds.add(propertyDTO.getCode());
        if(versionableAttributeIds.contains(newValueRecordDTO.getProperty().getPropertyIID())){
          shouldCreateRevision.set(true);;
        }
      } else if (sourceProperty.getSuperType() == SuperType.TAGS) {
        ITagsRecordDTO addedTagsRecord = (ITagsRecordDTO) sourceRecord;
        addedPropertyRecords.add(addedTagsRecord);
        addedPropertyIIDs.add( sourceProperty.getIID());
        changedTagIds.add(propertyDTO.getCode());
        if(versionableTagIds.contains(addedTagsRecord.getProperty().getPropertyIID())){
          shouldCreateRevision.set(true);
        }
      }
    }
    return addedPropertyRecords;
  }

  private void handleKlassesAndTaxonomies(BaseEntityBulkUpdateDTO bulkUpdateDTO, IBaseEntityDAO baseEntityDao, AtomicBoolean shouldCreateRevision)
  {
    Set<IClassifierDTO> addedClassifiers = bulkUpdateDTO.getAddedClassifiers();
    Set<IClassifierDTO> removedClassifiers = bulkUpdateDTO.getRemovedClassifiers();

    if (!addedClassifiers.isEmpty() || !removedClassifiers.isEmpty()) {
      shouldCreateRevision.set(true);
    List<String> addedSecondaryTypes = new ArrayList<String>();
    List<String> deletedSecondaryTypes = new ArrayList<String>();
    List<String> addedTaxonomyIds = new ArrayList<String>();
    List<String> deletedTaxonomyIds = new ArrayList<String>();

    IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();

    BaseType baseType = baseEntityDao.getBaseEntityDTO().getBaseType();
    addedClassifiers.forEach(classifier -> {
      ClassifierType classifierType = classifier.getClassifierType();
      String classifierCode = classifier.getCode();
      if (classifierType.equals(ClassifierType.CLASS)) {
        String type = (String)klassIdVsType.get(classifierCode);
        BaseType baseTypeFromKlass = IConfigMap.getBaseType(type);
        if(baseTypeFromKlass.equals(baseType)) {
          addedSecondaryTypes.add(classifierCode);
        }
      }
      else {
        addedTaxonomyIds.add(classifierCode);
      }
    });

    removedClassifiers.forEach(classifier -> {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        deletedSecondaryTypes.add(classifier.getCode());
      }
      else {
        deletedTaxonomyIds.add(classifier.getCode());
      }
    });

    if(addedSecondaryTypes.isEmpty() && deletedSecondaryTypes.isEmpty()
        && addedTaxonomyIds.isEmpty() && deletedTaxonomyIds.isEmpty()) {
      return;
    }

    typeSwitchModel.setKlassInstanceId(String.valueOf(baseEntityDao.getBaseEntityDTO().getBaseEntityIID()));
    typeSwitchModel.setAddedSecondaryTypes(addedSecondaryTypes);
    typeSwitchModel.setAddedTaxonomyIds(addedTaxonomyIds);
    typeSwitchModel.setDeletedSecondaryTypes(deletedSecondaryTypes);
    typeSwitchModel.setDeletedTaxonomyIds(deletedTaxonomyIds);

    try {
      ITypeSwitchInstance typeSwitchInstance = BGProcessApplication.getApplicationContext().getBean(ITypeSwitchInstance.class);
      typeSwitchInstance.execute(typeSwitchModel);
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }

}

@Override
public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
        throws CSInitializationException, CSFormatException, RDBMSException {
  super.initBeforeStart(initialProcessData, userSession);
  bulkUpdateDTO.fromJSON(jobData.getEntryData().toString());
  // collect the records and the properties involved in the bulk update specification
  Set<IPropertyRecordDTO> propertyRecords = bulkUpdateDTO.getPropertyRecords();
  sourceRecordsMap.clear();
  sourceProperties.clear();
  propertyRecords.forEach(propertyRecord -> {
    sourceRecordsMap.put(propertyRecord.getProperty().getPropertyIID(), propertyRecord);
    sourceProperties.add(propertyRecord.getProperty());
  });

  Set<IClassifierDTO> addedClassifiers = bulkUpdateDTO.getAddedClassifiers();
  List<String> classifierCodes = new ArrayList<>();
  addedClassifiers.forEach(classifier -> {
    ClassifierType classifierType = classifier.getClassifierType();
    if (classifierType.equals(ClassifierType.CLASS)) {
      classifierCodes.add(classifier.getCode());
    }
  });
  if(!classifierCodes.isEmpty()) {
    Map<String,Object> requestMap = new HashMap<>();
    requestMap.put("klassIds", classifierCodes);
    klassIdVsType = CSConfigServer.instance().request(requestMap, "GetTypesByKlassIds", null);
  }

  workflowUtils = BGProcessApplication.getApplicationContext().getBean(WorkflowUtils.class);
  goldenRecordUtils = BGProcessApplication.getApplicationContext().getBean(GoldenRecordUtils.class);
}

@Override
protected void runBaseEntityBatch(Set<Long> baseEntityIIDs) throws JsonProcessingException, Exception {

  ILocaleCatalogDAO catalogDao = openUserSession().openLocaleCatalog(userSession,
          new LocaleCatalogDTO(bulkUpdateDTO.getLocaleID(), bulkUpdateDTO.getCatalogCode(), bulkUpdateDTO.getOrganizationCode()));
    RuleHandler ruleHandler = new RuleHandler();
  for (Long baseEntityIID : baseEntityIIDs) {
    userSession.setTransactionId(UUID.randomUUID().toString());
    AtomicBoolean shouldCreateRevision = new AtomicBoolean(false);
    IBaseEntityDTO baseEntity = catalogDao.getEntityByIID(baseEntityIID);
    IBaseEntityDAO baseEntityDao = catalogDao.openBaseEntity(baseEntity);
    IBaseEntityDTO currentEntity = baseEntityDao.loadPropertyRecords( sourceProperties.toArray(new IPropertyDTO[0]));
    
    Boolean isTranslationPresent = currentEntity.getLocaleIds().contains(bulkUpdateDTO.getLocaleID());

    handleKlassesAndTaxonomies(bulkUpdateDTO, baseEntityDao,shouldCreateRevision);
    // get applicable properties to current entity
    collectApplicablePropertyIIDs( currentEntity, isTranslationPresent);

    // Identify and prepare the list of records that must be updated in the current entity
    List<String> changedAttributeIds = new ArrayList<>();
    List<String> changedTagIds = new ArrayList<>();
    List<IPropertyRecordDTO> updatedPropertyRecords = getUpdatedPropertyRecords( sourceRecordsMap, baseEntityDao,
        changedAttributeIds, changedTagIds, shouldCreateRevision);

    // Now prepare the list of added records
    List<IPropertyRecordDTO> addedPropertyRecords = getAddedPropertyRecords( sourceRecordsMap, baseEntityDao,
        changedAttributeIds, changedTagIds, shouldCreateRevision);

    jobData.getLog().info( "Processing entity iid %d with applicable properties: %s",
            baseEntityIID, Text.join(",", applicablePropertyIIDs));
    if (addedPropertyRecords.size() > 0) {
      baseEntityDao.createPropertyRecords(addedPropertyRecords.toArray(new IPropertyRecordDTO[0]));
      jobData.getLog().info( "Added entity iid %d with properties: %s",
              baseEntityIID, Text.join(",", addedPropertyIIDs));
    }
    if (updatedPropertyRecords.size() > 0) {
      baseEntityDao.updatePropertyRecords(updatedPropertyRecords.toArray(new IPropertyRecordDTO[0]));
      jobData.getLog().info( "Updated entity iid %d with properties: %s",
              baseEntityIID, Text.join(",", updatedPropertyIIDs));
    }
    
    if (shouldCreateRevision.get()) {
      rdbmsComponentUtils.createNewRevision(baseEntityDao.getBaseEntityDTO(), numberOfVersionsToMaintain);
    }
    IChangedPropertiesDTO changedProperties = ruleHandler.initiateRuleHandling(baseEntityDao, catalogDao, false, referencedElements, referencedAttributes,
          referencedTags);
    changedAttributeIds.addAll(changedProperties.getAttributeCodes());
    changedTagIds.addAll(changedProperties.getTagCodes());
      
    catalogDao.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
    initiateAfterSaveWorkflow(baseEntityDao, changedAttributeIds, changedTagIds);

    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntity);
  }
}

  private void initiateAfterSaveWorkflow(IBaseEntityDAO baseEntityDao,
      List<String> changedAttributeIds, List<String> changedTagIds) throws Exception, RDBMSException
  {
    IBusinessProcessTriggerModel businessProcessEventModel = BgprocessUtils.getBusinessProcessModelForPropetiesSave(
        baseEntityDao, changedAttributeIds, changedTagIds);

    if(businessProcessEventModel != null) {
      workflowUtils.executeBusinessProcessEvent(businessProcessEventModel);
    }
  }


  private void manageKlassInstancePermissions(IConfigDetailsForBulkPropagationResponseModel configDetails)
  {
    IReferencedTemplatePermissionModel permission = configDetails.getReferencedPermissions();

    Set<String> visiblePCIds = permission.getVisiblePropertyCollectionIds();
    Set<String> visiblePropertyIds = permission.getVisiblePropertyIds();

    Set<String> editablePCIds = permission.getEditablePropertyCollectionIds();
    Set<String> pCIdsClone = new HashSet<>(visiblePCIds);
    pCIdsClone.addAll(editablePCIds);
    Set<String> editablePropertyIds = permission.getEditablePropertyIds(); //contains all editable property id's for current user
    Set<String> mergedEditablePropertyIds = new HashSet<>();               //contains editable property id's only if its containing property collection is also editable
    Set<String> mergedVisiblePropertyIds = new HashSet<>();

    Set<String> noVisiblePermission = new HashSet<>();
    Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollections = configDetails.getReferencedPropertyCollections();

    Map<String, IReferencedPropertyCollectionModel> referencedPropertyCollectionsClone = new HashMap<>(referencedPropertyCollections);

    for (Map.Entry<String, IReferencedPropertyCollectionModel> entry : referencedPropertyCollectionsClone.entrySet()) {
      String pCId = entry.getKey();
      if (!visiblePCIds.contains(pCId)) {
        referencedPropertyCollections.remove(pCId);
        for (IReferencedPropertyCollectionElementModel element : entry.getValue().getElements()) {
          String elementId = element.getId();
          noVisiblePermission.add(elementId);
        }
        continue;
      }

      // add editable property id's to mergedEditablePropertyIds only if its
      // containing property collection is also editable
      IReferencedPropertyCollectionModel referencedPropertyCollection = entry.getValue();
      for (IReferencedPropertyCollectionElementModel propertyCollectionElement : referencedPropertyCollection.getElements()) {
        String propertyId = propertyCollectionElement.getId();
        if (editablePropertyIds.contains(propertyId) && editablePCIds.contains(pCId)) {
          mergedEditablePropertyIds.add(propertyId);
        }
        if (visiblePropertyIds.contains(propertyId)) {
          mergedVisiblePropertyIds.add(propertyId);
        }
      }
    }

    //retains only visible propertyIds (this is done to handle usecase if property is present in more than 2 PCs, 1 visible other is not)
    noVisiblePermission.removeAll(mergedVisiblePropertyIds);
    visiblePropertyIds.removeAll(noVisiblePermission);

    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    for(IReferencedSectionElementModel element:referencedElements.values())
    {
      String id = element.getId();
      if(visiblePropertyIds.contains(id))
      {
        element.setCanRead(true);
      }
    }

    mergedEditablePropertyIds.retainAll(mergedVisiblePropertyIds);
    manageReferencedElementsAndRelationshipForPermissions(configDetails, mergedEditablePropertyIds);
  }

  private void manageReferencedElementsAndRelationshipForPermissions(IConfigDetailsForBulkPropagationResponseModel configDetails,
      Set<String> mergedEditablePropertyIds)
  {
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Set<String> referenecedPropertyIds = new HashSet<String>(referencedElements.keySet());

    for (String elementId : referenecedPropertyIds) {
      IReferencedSectionElementModel referencedElement = referencedElements.get(elementId);

      //For attributes, tags and roles
      if (mergedEditablePropertyIds.contains(elementId)) {
        referencedElement.setIsDisabled(false);
      }
      else {
        referencedElement.setIsDisabled(true);
      }

    }
  }
}
