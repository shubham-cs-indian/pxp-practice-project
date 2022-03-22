package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.IArticleInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.ArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForOnboarding;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceForOnboarding;
import com.cs.core.runtime.interactor.utils.OnboardingUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveAssetInstanceForTabs;
import com.cs.pim.runtime.strategy.usecase.articleinstance.ISaveArticleInstanceForTabs;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("setAttributesAndTagsTask")
public class SetAttributesAndTagsTask extends AbstractTask {

  public static final String             KLASS_INSTANCE_ID     = "KLASS_INSTANCE_ID";
  public static final String             TAG_VALUES_MAP        = "TAG_VALUES_MAP";
  public static final String             ATTRIBUTES_VALUES_MAP = "ATTRIBUTES_VALUES_MAP";
  public static final String             ATTRIBUTES_TYPES_MAP  = "ATTRIBUTES_TYPES_MAP";
  public static final String             DATA_LANGUAGE         = "DATA_LANGUAGE";
  public static final String             ENTITY_TYPE           = "ENTITY_TYPE";
  public static final String             IS_TRIGGERED_THROUGH_SCHEDULER = "IS_TRIGGERED_THROUGH_SCHEDULER";
  
  public static final List<String>       INPUT_LIST            = Arrays.asList(ENTITY_TYPE, KLASS_INSTANCE_ID, TAG_VALUES_MAP, ATTRIBUTES_VALUES_MAP,
      ATTRIBUTES_TYPES_MAP, DATA_LANGUAGE, IS_TRIGGERED_THROUGH_SCHEDULER);
  public static final List<String>       OUTPUT_LIST           = Arrays.asList(EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES        = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES           = Arrays.asList(EventType.BUSINESS_PROCESS);
  
  @Autowired
  protected ISaveArticleInstanceForTabs      saveArticleInstanceForTabs;
  
  @Autowired
  protected ISaveAssetInstanceForTabs        saveAssetInstanceForTabs;
  
  @Autowired
  protected IGetArticleInstanceForOnboarding getArticleInstanceForOnboarding;
  
  @Autowired
  protected IGetAssetInstanceForOnboarding   getAssetInstanceForOnboarding;
  
  @Autowired
  protected TransactionThreadData            transactionThread;
  
  @Autowired
  protected SessionContextCustomProxy         context;
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    if (Boolean.parseBoolean((String) model.getInputParameters().get(IS_TRIGGERED_THROUGH_SCHEDULER))) {
      model.getWorkflowModel().getTransactionData().setDataLanguage(DiValidationUtil.validateAndGetRequiredString(model, DATA_LANGUAGE));
    }
    
    transactionThread.setTransactionData((TransactionData) model.getWorkflowModel().getTransactionData());
    context.setUserSessionDTOInThreadLocal(model.getWorkflowModel().getUserSessionDto());
    Map<String, String> attributesValueMap = (Map<String, String>) DiValidationUtil.validateAndGetOptionalMap(model, ATTRIBUTES_VALUES_MAP);
    Map<String, String> attributeTypesMap = (Map<String, String>) DiValidationUtil.validateAndGetOptionalMap(model, ATTRIBUTES_TYPES_MAP);
    Map<String, String> tagsValueMap = (Map<String, String>) DiValidationUtil.validateAndGetOptionalMap(model, TAG_VALUES_MAP);
    String klassInstanceId = DiValidationUtil.validateAndGetRequiredString(model, KLASS_INSTANCE_ID);
    attributesValueMap = validateAttributeValuesByAttributeType(attributeTypesMap, attributesValueMap, model.getExecutionStatusTable());
    
    if ((attributesValueMap == null || attributesValueMap.isEmpty())
        && (tagsValueMap == null || tagsValueMap.isEmpty())) {
      return;
    }
    String entityType = DiValidationUtil.validateAndGetRequiredString(model, ENTITY_TYPE);
    IGetKlassInstanceCustomTabModel klassInstance = null;
    try {
      klassInstance = getKlassInstance(entityType, klassInstanceId);
      validateKlassInstanceByBaseType(entityType, klassInstance);
    }
    catch (Exception e) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN020,
          new String[] { klassInstanceId });
      return;
    }
    attributesValueMap = handleReadOnlyAttributesAndTags(attributesValueMap, klassInstance , CommonConstants.ATTRIBUTE);
    tagsValueMap = handleReadOnlyAttributesAndTags(tagsValueMap, klassInstance, CommonConstants.TAGS);
    if ((attributesValueMap == null || attributesValueMap.isEmpty())
        && (tagsValueMap == null || tagsValueMap.isEmpty())) {
      return;
    }
    
    IContentInstance instance = klassInstance.getKlassInstance();
    
    IKlassInstanceSaveModel klassInstancesModel = getSaveInstanceModel(instance, entityType);
    klassInstancesModel.setId(klassInstanceId);
    klassInstancesModel.setLogicalCatalogId("-1");
    klassInstancesModel.setTabId(SystemLevelIds.PROPERTY_COLLECTION_TAB);
    klassInstancesModel.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
    klassInstancesModel.setOriginalInstanceId(instance.getOriginalInstanceId());
    klassInstancesModel.setBaseType(instance.getBaseType());
    klassInstancesModel.setTypes(instance.getTypes());
    klassInstancesModel.setTaxonomyIds(instance.getTaxonomyIds());
    klassInstancesModel.setSelectedTaxonomyIds(instance.getSelectedTaxonomyIds());
    klassInstancesModel.setVersionId(instance.getVersionId());
    klassInstancesModel.setGetKlassInstanceTreeInfo(new GetKlassInstanceTreeStrategyModel());
    
    List<IModifiedContentTagInstanceModel> modifiedTags = new ArrayList<>();
    if (tagsValueMap != null &&  !tagsValueMap.isEmpty()) {
      Map<String, List<String>> tagsToModify = new HashMap<>();
      Map<String, ITag> referencedTags = klassInstance.getConfigDetails()
          .getReferencedTags();
      for (Entry<String, String> tagGroup : tagsValueMap.entrySet()) {
        String tagValue = tagGroup.getValue();
        if (tagValue != null) {
          ITag tag = referencedTags.get(tagGroup.getKey());
          if (tag.getIsMultiselect()) {
            tagsToModify.put(tagGroup.getKey(), CollectionUtils.arrayToList(tagValue.split(",")));
          }
          else {
            tagsToModify.put(tagGroup.getKey(), Arrays.asList(tagValue));
          }
        }
        else {
          tagsToModify.put(tagGroup.getKey(), new ArrayList<String>());
        }
      }
      
      modifiedTags = getModifiedTags(instance.getTags(), tagsToModify);
      klassInstancesModel.setModifiedTags(modifiedTags);
      
      prepareAndSetAddedTags(tagsToModify, klassInstance.getConfigDetails().getReferencedTags(), klassInstancesModel);
    }
    
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = new ArrayList<>();
    if (attributesValueMap != null && !attributesValueMap.isEmpty()) {
      Map<String, String> attributesToAdd = new HashMap<>();
      attributesToAdd.putAll(attributesValueMap);
      modifiedAttributes = getModifiedAttributes(instance.getAttributes(), attributesValueMap,
          model.getWorkflowModel().getTransactionData().getDataLanguage(), instance.getLanguageCodes(), attributesToAdd);
      prepareAndSetAddedAttributes(attributesToAdd, attributeTypesMap, klassInstance.getConfigDetails().getReferencedAttributes(),
          klassInstancesModel);
    }
    
    klassInstancesModel.setModifiedAttributes(modifiedAttributes);
    
    if (!modifiedTags.isEmpty() || !modifiedAttributes.isEmpty() || !klassInstancesModel.getAddedTags().isEmpty() || !klassInstancesModel.getAddedAttributes().isEmpty()) {
       saveInstance(klassInstancesModel, entityType);
    }
  }

  /**
   * This method removes attribute from map if it is read only or dynamically
   * coupled
   * 
   * @param valueMap
   * @param klassInstance
   * @param type 
   * @return
   */
  private Map<String, String> handleReadOnlyAttributesAndTags(Map<String, String> valueMap, IGetKlassInstanceCustomTabModel klassInstance,
      String type)
  {
    Map<String, String> validProperties = new HashMap<String, String>();
    IGetConfigDetailsModel configDetails = klassInstance.getConfigDetails();
    Map<String, IAttribute> refAttribute = configDetails.getReferencedAttributes();
    Map<String, IReferencedSectionElementModel> refElements = configDetails.getReferencedElements();
    List<String> lifeCycleStatusTags = configDetails.getReferencedLifeCycleStatusTags();
    if(valueMap!= null) {
    for (String code : valueMap.keySet()) { 
      if (lifeCycleStatusTags.contains(code)) {
        validProperties.put(code, valueMap.get(code));
      }
      else{
      String couplingType = refElements.get(code).getCouplingType();
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        IAttribute property = refAttribute.get(code);
        if ((property.getIsDisabled() == null || !property.getIsDisabled()) && !couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
          validProperties.put(code, valueMap.get(code));
        }
      }
      else {
        if (!couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
          validProperties.put(code, valueMap.get(code));
        }
      }
    } 
   }
  }    
    return validProperties;
  }

  /**
   * Check the get klass instance is match with the entity type selected in the component.
   * 
   * @param entityType
   * @param klassInstance
   * @throws Exception
   */
  private void validateKlassInstanceByBaseType(String entityType, IGetKlassInstanceCustomTabModel klassInstance) throws Exception
  {
    String baseType = klassInstance.getKlassInstance().getBaseType();
    if (entityType.equals(DiConstants.SOURCE_TYPE_PRODUCT)) {
      if (!baseType.equals(Constants.ARTICLE_INSTANCE_BASE_TYPE)) {
        throw new Exception("Selected entity type on component is not matched with instance base type");
      }
    }
    else if (entityType.equals(DiConstants.SOURCE_TYPE_ASSET)) {
      if (!baseType.equals(Constants.ASSET_INSTANCE_BASE_TYPE)) {
        throw new Exception("Selected entity type on component is not matched with instance base type");
      }
    }
  }
  
  /**
   * Check the given value for the attribute is valid or not based on base type of attribute.
   * 
   * @param attributesValueMap
   * @param attributsTypeMap
   * @param referencedAttributes
   * @param klassInstancesModel
   */
  private void prepareAndSetAddedAttributes(Map<String, String> attributesValueMap, Map<String, String> attributsTypeMap, Map<String, IAttribute> referencedAttributes,
      IKlassInstanceSaveModel klassInstancesModel)
  {
    for (Entry<String, String> attribute : attributesValueMap.entrySet()) {
      IAttribute attributeModel = referencedAttributes.get(attribute.getKey());
      if (attributeModel != null) {
        //prepare tag info to add
        IAttributeInstance attributeInastance = new AttributeInstance();
        attributeInastance.setAttributeId(attribute.getKey());
        attributeInastance.setId(UUID.randomUUID().toString());
        attributeInastance.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
        
        String propertyBaseType = attributsTypeMap.get(attribute.getKey()) == null ? "" : attributsTypeMap.get(attribute.getKey());
        switch (propertyBaseType) {
          case CommonConstants.HTML_TYPE_ATTRIBUTE:
            attributeInastance.setValueAsHtml(attribute.getValue());
            break;
          case CommonConstants.PRICE_ATTRIBUTE_TYPE:
          case CommonConstants.LENGTH_ATTRIBUTE_TYPE:
          case CommonConstants.NUMBER_ATTRIBUTE_TYPE:
            attributeInastance.setValueAsNumber(Double.parseDouble(attribute.getValue()));
            break;
        
        }
        attributeInastance.setValue(attribute.getValue());
        
        klassInstancesModel.getAddedAttributes().add(attributeInastance);
      }
    }
    
  }

  /**
   * @param instance
   * @param entityType
   * @return
   */
  public IKlassInstanceSaveModel getSaveInstanceModel(IContentInstance instance, String entityType)
  {
    switch(entityType) {
      case DiConstants.SOURCE_TYPE_PRODUCT :
        IArticleInstanceSaveModel articleInstancesModel = new ArticleInstanceSaveModel((IArticleInstance) instance);
        return articleInstancesModel;
      case DiConstants.SOURCE_TYPE_ASSET:
        IAssetInstanceSaveModel assetInstancesModel = new AssetInstanceSaveModel((IAssetInstance) instance);
        return assetInstancesModel;
    }
    
    return null;
  }
  
  /**
   * @param attributeTypesMap
   * @param attributeValuesMap
   * @param executionStatus 
   * @return
   * @throws Exception
   */
  private Map<String, String> validateAttributeValuesByAttributeType(Map<String, String> attributeTypesMap, Map<String, String> attributeValuesMap,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatus)
  {
    if (attributeValuesMap == null || attributeValuesMap.isEmpty()) {
      return new HashMap<>();
    }
    
    for (String attributeId : attributeValuesMap.keySet()) {
      Object value = attributeValuesMap.get(attributeId) == null ? "" : attributeValuesMap.get(attributeId);
      
      String propertyBaseType = attributeTypesMap.get(attributeId) == null ? "" : attributeTypesMap.get(attributeId);
      switch (propertyBaseType) {
        case Constants.DATE_ATTRIBUTE_BASE_TYPE:
          if (value instanceof DateTime) {
            value = ((DateTime) value).getMillis();
          }
          else if (value instanceof Date) {
            value = ((Date) value).getTime();
          }
          else if (value instanceof Calendar) {
            value = ((Calendar) value).getTime();
          }
          else if (value instanceof Long) {
            new DateTime((long) value);
          }
          else if (value instanceof String) {
            if (NumberUtils.isNumber(value.toString())) {
              new DateTime(Long.parseLong((String) value));
            }
            else {
              DateTime date = DateTime.parse((String) value);
              value = date.getMillis();
            }
          }
          else {
            executionStatus.addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN041, new String[] { attributeId });
          }
          break;
        
        case CommonConstants.PRICE_ATTRIBUTE_TYPE:
        case CommonConstants.LENGTH_ATTRIBUTE_TYPE:
        case CommonConstants.NUMBER_ATTRIBUTE_TYPE:
          if (!NumberUtils.isNumber(value.toString())) {
            executionStatus.addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN041, new String[] { attributeId });
          }
          break;
        default:
          break;
      }
      attributeValuesMap.put(attributeId, value.toString());
    }
    return attributeValuesMap;
  }
  
  /**
   * @param tags
   * @param tagsToModify
   * @return
   */
  private List<IModifiedContentTagInstanceModel> getModifiedTags(List<? extends IContentTagInstance> tags,
      Map<String, List<String>> tagsToModify)
  {
    List<IModifiedContentTagInstanceModel> modifiedTags = new ArrayList<IModifiedContentTagInstanceModel>();
    for (IContentTagInstance iContentTagInstance : tags) {
      String tagId = iContentTagInstance.getTagId();
      if (tagsToModify.keySet().contains(tagId)) {
        List<ITagInstanceValue> tagValues = iContentTagInstance.getTagValues();
        List<String> tagValuesToUpdate = tagsToModify.get(tagId);
        List<String> selectedTagValues = tagValues.stream()
            .map(x -> x.getTagId())
            .collect(Collectors.toList());
        
        if (!selectedTagValues.containsAll(tagValuesToUpdate) || tagValuesToUpdate.isEmpty()) {
          addInModifiedTags(tagsToModify, modifiedTags, iContentTagInstance, tagId);
        }
        
        tagsToModify.remove(tagId);
      }
    }
    
    return modifiedTags;
  }
  
  /**
   * @param tagsToModify
   * @param modifiedTags
   * @param iContentTagInstance
   * @param tagId
   */
  public static void addInModifiedTags(Map<String, List<String>> tagsToModify,
      List<IModifiedContentTagInstanceModel> modifiedTags, IContentTagInstance iContentTagInstance,
      String tagId)
  {
    List<String> childTagToModifyIds = tagsToModify.get(tagId);
    IModifiedContentTagInstanceModel modifiedTagInstanceModel = new ModifiedTagInstanceModel();
    
    List<ITagInstanceValue> addedTagValues = new ArrayList<>();
    List<String> deletedTagValues = new ArrayList<String>();
    
    for (ITagInstanceValue iTagInstanceValue : iContentTagInstance.getTagValues()) {
      deletedTagValues.add(iTagInstanceValue.getId());
    }
    for (String childTagToModifyId : childTagToModifyIds) {
      addInAddedTagValues(childTagToModifyId, addedTagValues);
    }
    
    fillModifiedTagInstanceModel(iContentTagInstance, modifiedTagInstanceModel, addedTagValues,
        deletedTagValues);
    modifiedTags.add(modifiedTagInstanceModel);
  }
  
  private static void addInAddedTagValues(String tagId, List<ITagInstanceValue> addedTagValues)
  {
    ITagInstanceValue valueTag = new TagInstanceValue();
    valueTag.setId(UUID.randomUUID().toString());
    valueTag.setRelevance(100);
    valueTag.setTagId(tagId);
    addedTagValues.add(valueTag);
  }
  
  /**
   * @param iContentTagInstance
   * @param modifiedTagInstanceModel
   * @param addedTagValues
   * @param deletedTagValues
   */
  private static void fillModifiedTagInstanceModel(IContentTagInstance iContentTagInstance,
      IModifiedContentTagInstanceModel modifiedTagInstanceModel,
      List<ITagInstanceValue> addedTagValues, List<String> deletedTagValues)
  {
    modifiedTagInstanceModel.setId(iContentTagInstance.getId());
    modifiedTagInstanceModel.setTagId(iContentTagInstance.getTagId());
    modifiedTagInstanceModel.setBaseType(iContentTagInstance.getBaseType());
    modifiedTagInstanceModel.setKlassInstanceId(iContentTagInstance.getKlassInstanceId());
    modifiedTagInstanceModel.setAddedTagValues(addedTagValues);
    modifiedTagInstanceModel.setDeletedTagValues(deletedTagValues);
  }
  
  /**
   * This method prepare and set list of tags to add
   * 
   * @param tagsToAdd
   * @param referencedTags
   * @param klassInstancesModel
   */
  private void prepareAndSetAddedTags(Map<String, List<String>> tagsToAdd,
      Map<String, ITag> referencedTags, IKlassInstanceSaveModel klassInstancesModel)
  {
    for (Entry<String, List<String>> tagToAdd : tagsToAdd.entrySet()) {
      ITag tag = referencedTags.get(tagToAdd.getKey());
      if (tag != null) {
        //prepare tag info to add
        ITagInstance tagInastance = new TagInstance();
        tagInastance.setTagId(tagToAdd.getKey());
        tagInastance.setId(UUID.randomUUID().toString());
        tagInastance.setBaseType(Constants.TAG_INSTANCE_PROPERTY_TYPE);
        
        //Prepare child values of tag
        for (ITreeEntity tagvalue : tag.getChildren()) {
          ITagInstanceValue valueTag = new TagInstanceValue();
          valueTag.setId(UUID.randomUUID().toString());
          valueTag.setRelevance(tagToAdd.getValue().contains(tagvalue.getId()) ? 100 : 0);
          valueTag.setTagId(tagvalue.getId());
          valueTag.setVersionId(0l);
          tagInastance.getTagValues().add(valueTag);
        }
        klassInstancesModel.getAddedTags().add(tagInastance);
      }
    }
  }
  
  /**
   * @param attributes
   * @param attributesValueToSet
   * @param language
   * @param languageCodes
   * @param attributesToAdd 
   * @return
   */
  private List<IModifiedContentAttributeInstanceModel> getModifiedAttributes(List<? extends IContentAttributeInstance> attributes,
      Map<String, String> attributesValueToSet, String language, List<String> languageCodes, Map<String, String> attributesToAdd)
  {
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = new ArrayList<>();
    Map<String, IContentAttributeInstance> attributesMap = new HashMap<>();
    for (IContentAttributeInstance attribute : attributes) {
      if (attribute.getVariantInstanceId() == null) {
        attributesMap.put(attribute.getAttributeId(), attribute);
      }
    }
    
    for (String attributeIdToUpdate : attributesValueToSet.keySet()) {
      IAttributeInstance attributeInstance = (IAttributeInstance) attributesMap.get(attributeIdToUpdate);
      if (attributeInstance != null) {
        String newValue = attributesValueToSet.get(attributeIdToUpdate);
        if (!OnboardingUtils.isAttributeValueUnChanged(newValue, attributeInstance.getValue())) {
          attributeInstance.setValue(newValue);
          attributeInstance.setValueAsHtml(newValue);
          if (attributeInstance.getLanguage() != null && languageCodes.contains(language)) {
            attributeInstance.setLanguage(language);
          }
          IModifiedContentAttributeInstanceModel modifiedAttrributeModel = new ModifiedAttributeInstanceModel(attributeInstance);
          modifiedAttributes.add(modifiedAttrributeModel);
        }
        
        attributesToAdd.remove(attributeIdToUpdate);
      }
    }
    
    return modifiedAttributes;
  }
  
  /**
   * Call respective save API by given input entityType
   * @param model
   * @param entityType
   */
  public void saveInstance(IKlassInstanceSaveModel model, String entityType)
  {
    try {
      switch (entityType) {
        case DiConstants.SOURCE_TYPE_PRODUCT:
          saveArticleInstanceForTabs.execute((IArticleInstanceSaveModel) model);
          break;
        case DiConstants.SOURCE_TYPE_ASSET:
          saveAssetInstanceForTabs.execute((IAssetInstanceSaveModel) model);
          break;
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  /**
   *
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String entityType = (String) inputFields.get(ENTITY_TYPE);
    if (DiValidationUtil.isBlank(entityType)) {
      returnList.add(ENTITY_TYPE);
    }
    
    String klassInstanceId = (String) inputFields.get(KLASS_INSTANCE_ID);
    if (DiValidationUtil.isBlank(klassInstanceId)) {
      returnList.add(KLASS_INSTANCE_ID);
    }
    
    if (Boolean.parseBoolean((String) inputFields.get(IS_TRIGGERED_THROUGH_SCHEDULER))) {
      String classpathip = (String) inputFields.get(DATA_LANGUAGE);
      if (DiValidationUtil.isBlank(classpathip)) {
        returnList.add(DATA_LANGUAGE);
      }
    }
    
    List<String> attributeTypes = (List<String>) inputFields.get(ATTRIBUTES_TYPES_MAP);
    List<String> attributeValues = (List<String>) inputFields.get(ATTRIBUTES_VALUES_MAP);
    int index = -1;
    for (String attribute : attributeTypes) {
      index++;
      if (attribute.equals(NameAttribute.class.getName())) {
        String value = (String) attributeValues.get(index);
        if (DiValidationUtil.isBlank(value)) {
          returnList.add(ATTRIBUTES_VALUES_MAP);
        }
      }
    }
    
    return returnList;
  }
  

  /**
   * @param sourceType
   * @param instanceId
   * @return
   * @throws Exception
   */
  protected IGetKlassInstanceCustomTabModel getKlassInstance(String sourceType, String instanceId)
      throws Exception
  {
    IGetInstanceRequestModel requestModel = new GetInstanceRequestModel();
    requestModel.setId(instanceId);
    switch (sourceType) {
      case DiConstants.SOURCE_TYPE_PRODUCT:
        return getArticleInstanceForOnboarding.execute(requestModel);
      case DiConstants.SOURCE_TYPE_ASSET:
        return getAssetInstanceForOnboarding.execute(requestModel);
      default:
        return null;
    }
  }
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }

  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }

  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }

  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }

  @Override
  public TaskType getTaskType()
  {
    return TaskType.SERVICE_TASK;
  }
}
