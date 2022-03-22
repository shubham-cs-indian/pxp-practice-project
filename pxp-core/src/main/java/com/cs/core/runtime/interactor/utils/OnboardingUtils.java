package com.cs.core.runtime.interactor.utils;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentParameterModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.*;
import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.interactor.model.processdetails.ProcessStatusDetailsModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEndpointEventModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.config.interactor.usecase.endpoint.IGetEndpoint;
import com.cs.core.config.interactor.usecase.mapping.IGetMapping;
import com.cs.core.config.strategy.usecase.taxonomy.IGetTaxonomyIdsByCodeIdForOnboardingStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.OnboardingConstants;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.*;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.taskexecutor.IGetImportedEntityStatusResponseModel;
import com.cs.core.runtime.interactor.model.taskexecutor.IImportedEntityStatusModel;
import com.cs.core.runtime.interactor.model.taskexecutor.ImportedEntityStatusModel;
import com.cs.core.runtime.interactor.usecase.runtimemapping.IGetTagValuesFromColumn;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetProcessEventByEndpointIdStrategy;
import com.cs.core.runtime.strategy.usecase.transfer.processstatus.IGetProcessStatusByIdStrategy;
import com.cs.core.runtime.strategy.usecase.transfer.processstatus.IGetProcessStatusByProcessInstanceIdStrategy;
import com.cs.core.runtime.strategy.usecase.transfer.processstatus.ISaveProcessStatusStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.exception.NullValueException;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Future;

@SuppressWarnings("unchecked")
@Component
public class OnboardingUtils {
  
  @Autowired
  protected IGetProcessEventByEndpointIdStrategy         getProcessEventByEndpointIdStrategy;
  
  @Autowired
  protected ISessionContext                              context;
  
  @Autowired
  protected TransactionThreadData                        transactionThread;
  
  @Autowired
  protected IGetTagValuesFromColumn                      getTagValuesFromColumn;
  
  @Autowired
  protected IGetTaxonomyIdsByCodeIdForOnboardingStrategy getTaxonomyIdsByCodeIdForOnboardingStrategy;
  
  protected static String                                separator;
  
  @Autowired
  RepositoryService                                      repositoryService;
  
  /*@Autowired
  protected IGetImportedEntityStatusStrategy             getProcessInstanceUpdatesStrategy;
  
  @Autowired
  ISaveImportedEntityStatusStrategy                      saveImportedEntityStatusStrategy;*/

  @Autowired
  protected IGetEndpoint                                 getEndPoint;
  
  @Autowired
  protected IGetMapping                                  getMapping;
  
  @Autowired
  protected IGetProcessStatusByIdStrategy                getProcessStatusByIdStrategy;
  
  @Autowired
  protected ISaveProcessStatusStrategy                   saveProcessStatusStrategy;
  
  @Autowired
  protected IGetProcessStatusByProcessInstanceIdStrategy getProcessStatusByProcessInstanceIdStrategy;
  
  @Value("${di.valueSeparator}")
  public void setDatabase(String valueSeparator)
  {
    separator = valueSeparator;
  }
  
  public static void manageAndFillTagInstances(IKlassInstance klassInstance,
      List<ITagInstance> addedTagInstances,
      List<IModifiedContentTagInstanceModel> modifiedTagInstances, Map<String, Object> itemMap,
      String idColumn, Map<String, String> tagsMapping,
      HashMap<String, HashMap<String, Object>> tagValuesMapping, Map<String, ITag> refrencedTags) throws RDBMSException
  {
    Map<String, Object> tagMap = new HashMap<>();
    Set<String> referencedTagIds = refrencedTags.keySet();
    List<IContentTagInstance> tags = (List<IContentTagInstance>) klassInstance.getTags();
    if (tags == null) {
      tags = new ArrayList<>();
    }
    for (IContentTagInstance tag : tags) {
      tagMap.put(tag.getTagId(), tag);
    }
    
    for (String key : itemMap.keySet()) {
      if (key.equals(idColumn) || tagsMapping.get(key) == null || key.equals(Constants.FILEPATH))
        continue;
      
      String tagGroupId = tagsMapping.get(key);
      // Importing properties which are part of klass/taxonomy
      if (!referencedTagIds.contains(tagGroupId)) {
        continue;
      }
      String tagValueId = itemMap.get(key)
          .toString();
      ITagInstance tagInstance = (ITagInstance) tagMap.get(tagGroupId);
      List<ITagInstanceValue> tagValues = new LinkedList<>();
      List<ITagInstanceValue> modifiedTagValues = new LinkedList<>();
      List<ITagInstanceValue> adddedTagValues = new LinkedList<>();
      
      if (tagInstance == null) {
        
        tagInstance = createNewTagInstanceFromMappings(itemMap, tagsMapping, tagValuesMapping,
            refrencedTags, tagGroupId, tagValueId, tagValues, modifiedTagValues, adddedTagValues);
        
        addedTagInstances.add(tagInstance);
      }
      else {
        
        IModifiedContentTagInstanceModel modifiedTagInstance = new ModifiedTagInstanceModel(
            tagInstance);
        tagValues = tagInstance.getTagValues();
        
        ITag referencedTag = refrencedTags.get(tagGroupId);
        if (referencedTag != null && referencedTag.getTagType()
            .equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
          String booleanTagValueId = referencedTag.getTagValuesSequence()
              .get(0);
          prepareBooleanTagDataForSave(itemMap, tagsMapping, tagGroupId, tagValueId, tagValues,
              booleanTagValueId, modifiedTagValues, adddedTagValues);
        }
        else {
          setTagValues(tagValues, adddedTagValues, modifiedTagValues, tagValueId, tagValuesMapping,
              tagGroupId);
        }
        modifiedTagInstance.setAddedTagValues(adddedTagValues);
        modifiedTagInstance.setModifiedTagValues(modifiedTagValues);
        if (!modifiedTagValues.isEmpty() || !adddedTagValues.isEmpty())
          modifiedTagInstances.add(modifiedTagInstance);
      }
    }
  }
  
  public static void prepareTagValuesMappings(
      HashMap<String, HashMap<String, Object>> tagValuesMapping,
      List<IConfigRuleTagMappingModel> tagMappings)
  {
    for (IConfigRuleTagMappingModel tagMapping : tagMappings) {
      if (!tagMapping.getTagValueMappings()
          .isEmpty()) {
        HashMap<String, Object> tagvalueMap = new HashMap<>();
        for (ITagValueMappingModel tagValueMapping : tagMapping.getTagValueMappings()
            .get(0)
            .getMappings()) {
          Boolean isIgnoreCase = tagValueMapping.getIsIgnoreCase();
          if (isIgnoreCase != null && isIgnoreCase) {
            tagvalueMap.put(tagValueMapping.getTagValue()
                .toLowerCase(), tagValueMapping.getMappedTagValueId());
            tagvalueMap.put(tagValueMapping.getTagValue()
                .toLowerCase() + "_isIgnoreCase", isIgnoreCase);
          }
          else {
            tagvalueMap.put(tagValueMapping.getTagValue(), tagValueMapping.getMappedTagValueId());
          }
        }
        tagValuesMapping.put(tagMapping.getMappedElementId(), tagvalueMap);
      }
    }
  }
  
  public static ITagInstance createNewTagInstanceFromMappings(Map<String, Object> itemMap,
      Map<String, String> tagsMapping, HashMap<String, HashMap<String, Object>> tagValuesMapping,
      Map<String, ITag> refrencedTags, String tagGroupId, String tagValueId,
      List<ITagInstanceValue> tagValues, List<ITagInstanceValue> modifiedTagValues,
      List<ITagInstanceValue> adddedTagValues) throws RDBMSException
  {
    ITagInstance tag = new TagInstance();
    tag.setTagId(tagGroupId);
    tag.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tag.setBaseType(Constants.TAG_INSTANCE_PROPERTY_TYPE);
    // TODO Conflicts
    // tag.setIsValueChanged(true);
    ITag iTag = refrencedTags.get(tagGroupId);
    if (iTag != null && iTag.getTagType()
        .equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
      String tagValue = OnboardingUtils.getValueFromSheetOfBoolean(itemMap, tagsMapping,
          tagGroupId);
      if (tagValue != null && tagValue.equals(OnboardingConstants.BOOLEAN_RELEVANCE)) {
        OnboardingUtils.prepareTagValuesForBooleanTagType(adddedTagValues, tagGroupId, 100, iTag);
      }
      else {
        OnboardingUtils.prepareTagValuesForBooleanTagType(adddedTagValues, tagGroupId, 0, iTag);
      }
    }
    else {
      setTagValues(tagValues, adddedTagValues, modifiedTagValues, tagValueId, tagValuesMapping,
          tagGroupId);
    }
    tag.setTagValues(adddedTagValues);
    return tag;
  }
  
  private static void prepareBooleanTagDataForSave(Map<String, Object> itemMap,
      Map<String, String> tagsMapping, String tagGroupId, String tagValueId,
      List<ITagInstanceValue> tagValues, String booleanTagValueId,
      List<ITagInstanceValue> modifiedTagValues, List<ITagInstanceValue> adddedTagValues) throws RDBMSException
  {
    String tagValue = OnboardingUtils.getValueFromSheetOfBoolean(itemMap, tagsMapping, tagGroupId);
    if (tagValue != null) {
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      String tagValueInstanceId = tagValues.isEmpty() ? RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix())
          : tagValues.get(0)
              .getId();
      tagInstanceValue.setId(tagValueInstanceId);
      tagInstanceValue.setTagId(booleanTagValueId);
      tagInstanceValue
          .setRelevance(tagValueId.equals(OnboardingConstants.BOOLEAN_RELEVANCE) ? 100 : 0);
      tagInstanceValue.setTimestamp(Long.toString(System.currentTimeMillis()));
      if (tagValues.isEmpty()) {
        adddedTagValues.add(tagInstanceValue);
      }
      else {
        modifiedTagValues.add(tagInstanceValue);
      }
    }
  }
  
  public static void setTagValues(List<ITagInstanceValue> tagValues,
      List<ITagInstanceValue> adddedTagValues, List<ITagInstanceValue> modifiedTagValues,
      String tagValueInString, HashMap<String, HashMap<String, Object>> tagValuesMapping,
      String tagGroupId) throws RDBMSException
  {
    List<String> valueList = OnboardingUtils.StringToList(tagValueInString);
    Set<String> mappedTagValueList = new HashSet<>();
    
    if (tagValuesMapping.containsKey(tagGroupId)) {
      for (String value : valueList) {
        if (tagValuesMapping.get(tagGroupId)
            .containsKey(value)) {
          mappedTagValueList.add((String) tagValuesMapping.get(tagGroupId)
              .get(value));
        }
        else if (tagValuesMapping.get(tagGroupId)
            .containsKey(value.toLowerCase())) {
          mappedTagValueList.add((String) tagValuesMapping.get(tagGroupId)
              .get(value.toLowerCase()));
        }
        else {
          mappedTagValueList.add(value);
        }
        if (tagValuesMapping.get(tagGroupId)
            .get(value.toLowerCase() + "_isIgnoreCase") != null
            && (Boolean) tagValuesMapping.get(tagGroupId)
                .get(value.toLowerCase() + "_isIgnoreCase")) {
          // TODO FIX ME
          for (String key : tagValuesMapping.get(tagGroupId)
              .keySet()) {
            if (key.equalsIgnoreCase(value)) {
              mappedTagValueList.add((String) tagValuesMapping.get(tagGroupId)
                  .get(value.toLowerCase()));
            }
          }
        }
      }
    }
    else {
      mappedTagValueList.addAll(valueList);
    }
    
    for (ITagInstanceValue tagValue : tagValues) {
      Boolean isValueChanged = false;
      if (mappedTagValueList.contains(tagValue.getTagId())) {
        if (tagValue.getRelevance() != 100) {
          isValueChanged = true;
          tagValue.setRelevance(100);
        }
        mappedTagValueList.remove(tagValue.getTagId());
      }
      else {
        if (tagValue.getRelevance() != 0) {
          isValueChanged = true;
          tagValue.setRelevance(0);
        }
      }
      if (isValueChanged)
        modifiedTagValues.add(tagValue);
    }
    if (!mappedTagValueList.isEmpty()) {
      for (String tagValueId : mappedTagValueList) {
        ITagInstanceValue tagValue = new TagInstanceValue();
        tagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
        tagValue.setTagId(tagValueId);
        tagValue.setRelevance(100);
        adddedTagValues.add(tagValue);
      }
    }
  }
  
  public static String manageAndFillAttributeInstances(IKlassInstance klassInstance,
      List<IContentAttributeInstance> addedAttributeInstances,
      List<IModifiedContentAttributeInstanceModel> modifiedAttributeInstances,
      Map<String, Object> itemMap, String idColumn, Map<String, List<String>> attributesMapping,
      List<String> dateAttributeIds, Map<String, IAttribute> referencedAttributes,
      String dataLanguage) throws Exception
  {
    
    String name = null;
    
    Map<String, Object> attributeMap = new HashMap<>();
    List<IAttributeInstance> attributes = (List<IAttributeInstance>) klassInstance.getAttributes();
    for (IContentAttributeInstance attribute : attributes) {
      if (attribute instanceof IImageAttributeInstance) {
        continue;
      }
      attributeMap.put(attribute.getAttributeId(), attribute);
    }
    for (String key : itemMap.keySet()) {
      // TODO remove second condition once tags are handled.
      if (/*key.equals(idColumn)  || */ attributesMapping.get(key) == null)
        continue;
      String attributeValue = (String) itemMap.get(key);
      if (key.equals(CommonConstants.NAME_ATTRIBUTE) || attributesMapping.get(key)
          .contains(CommonConstants.NAME_ATTRIBUTE)) {
        name = attributeValue;
      }
      for (String attributeId : attributesMapping.get(key)) {
        // Importing properties which are part of klass/taxonomy
        if (!referencedAttributes.keySet()
            .contains(attributeId)) {
          continue;
        }
        
        IAttributeInstance attribute = (IAttributeInstance) attributeMap.get(attributeId);
        
        if (attribute == null) {
          attribute = new AttributeInstance();
          attribute.setAttributeId(attributeId);
          attribute.setValue(attributeValue);
          attribute.setValueAsHtml(attributeValue);
          attribute.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
          attribute.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
          attribute.setVersionId((long) 0);
          if (referencedAttributes.get(attributeId)
              .getIsTranslatable()) {
            attribute.setLanguage(dataLanguage);
          }
          
          if (dateAttributeIds.contains(attributeId)) {
            setValueForDateTypeAttribute(attribute, attributeValue);
          }
          // TODO Conflicts
          // attribute.setIsValueChanged(true);
          addedAttributeInstances.add(attribute);
        }
        else {
          
          Boolean isValueUnChanged = false;
          if (referencedAttributes.get(attributeId)
              .getIsTranslatable()) {
            attribute.setLanguage(dataLanguage);
          }
          
          if (dateAttributeIds.contains(attributeId)) {
            String valueBeforeUpdate = attribute.getValue();
            setValueForDateTypeAttribute(attribute, attributeValue);
            if (!valueBeforeUpdate.trim()
                .isEmpty() && valueBeforeUpdate != null) {
              isValueUnChanged = isAttributeValueUnChanged(valueBeforeUpdate, attribute.getValue());
            }
          }
          else {
            isValueUnChanged = isAttributeValueUnChanged((String) itemMap.get(key),
                attribute.getValue());
            // PRODUCT-4705
            if (attributeId.equals(CommonConstants.NAME_ATTRIBUTE)) {
              if (attributeValue != null && !attributeValue.isEmpty()) {
                attribute.setValue(attributeValue);
                attribute.setValueAsHtml(attributeValue);
              }
              else {
                isValueUnChanged = true;
              }
            }
            else {
              attribute.setValue(attributeValue);
              attribute.setValueAsHtml(attributeValue);
            }
          }
          if (!isValueUnChanged) {
            IModifiedContentAttributeInstanceModel modifiedAttributeInstance = new ModifiedAttributeInstanceModel(
                attribute);
            modifiedAttributeInstances.add(modifiedAttributeInstance);
          }
        }
      }
    }
    return name;
  }
  
  private static void setValueForDateTypeAttribute(IAttributeInstance attribute,
      String attributeValue) throws ParseException
  {
    if (attributeValue != null && !attributeValue.trim()
        .isEmpty()) {
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      Date date = formatter.parse(attributeValue);
      long time = date.getTime();
      attribute.setValue(String.valueOf(time));
      attribute.setValueAsNumber((double) time);
      attribute.setValueAsHtml(null);
    }
  }
  
  public static Boolean isAttributeValueUnChanged(String newAttributeValue,
      String OldAttributeValue)
  {
    
    return OldAttributeValue == null ? newAttributeValue == null
        : OldAttributeValue.equals(newAttributeValue);
  }
  
  public static List<String> StringToList(String str)
  {
    List<String> valuesList = new ArrayList<>();
    if (str != null && !str.trim()
        .isEmpty()) {
      valuesList = Arrays.asList(str.split(separator));
      valuesList.replaceAll(String::trim);
    }
    
    return valuesList;
  }
  
  public static List<String> StringToListByNewLine(String str)
  {
    List<String> valuesList = new ArrayList<>();
    if (str != null && !str.trim()
        .isEmpty()) {
      valuesList = Arrays.asList(str.split("\n"));
      valuesList.replaceAll(String::trim);
    }
    
    return valuesList;
  }
  
  public static void waitForThreadsExecution(List<Future<?>> threads)
  {
    while (true) {
      List<Future<?>> listToRemove = new ArrayList<>();
      for (Future<?> future : threads) {
        if (future.isDone()) {
          listToRemove.add(future);
        }
      }
      threads.removeAll(listToRemove);
      if (threads.size() == 0) {
        break;
      }
    }
  }
  
  /*public Map<String, Map<String, Object>> getSheetsInfoAccordingToProcessFlow(String endpointId) throws Exception
  {
    Map<String, Map<String, Object>> mapToReturn = new HashMap<>();
    IIdParameterModel endpointModel = new IdParameterModel();
    endpointModel.setId(endpointId);
    IListModel<IGetProcessEventModel> responseList = getProcessEventsForUserStrategy
        .execute(getModel);
    IGetProcessEndpointEventModel responseList = getProcessEventByEndpointIdStrategy.execute(endpointModel);
  
    Map<String, Object> flow = responseList.getProcessFlow();
    for (IGetProcessEventModel process : responseList.getList()) {
      flow = process.getProcessFlow();
    }
  
    if (flow != null && !flow.isEmpty()) {
      for (String componentInstanceId : flow.keySet()) {
        Map<String, Object> componentConfig = (Map<String, Object>) flow.get(componentInstanceId);
        Map<String, Object> parameters = (Map<String, Object>) componentConfig.get("parameters");
        List<Map<String, Object>> dataSources = (List<Map<String, Object>>) parameters
            .get("dataSources");
        if (dataSources.size() > 0) {
          Map<String, Object> dataSource = dataSources.get(0);
          String sheet = (String) dataSource.get("sheet");
          Map<String, Object> rowInfo = new HashMap<>();
          rowInfo.put("headerRow", dataSource.get("headerRowNumber"));
          rowInfo.put("dataRow", dataSource.get("dataRowNumber"));
          mapToReturn.put(sheet, rowInfo);
        }
      }
    }
    return mapToReturn;
  }*/
  
  public String getRespectivePrcessFlow(String endpointId) throws Exception
  {
    
//    IGetProcessEventsForUserModel getModel = new GetProcessEventsForUserModel();
//    getModel.setUserId(context.getUserId());
//    getModel.setEventType(EventType.BUSINESS_PROCESS);
//    getModel.setBoardingType(CommonConstants.ONBOARDING);
    
    IGetProcessEndpointEventModel response = getProcessEventByEndpointIdStrategy
        .execute(new IdParameterModel(endpointId));
    
    return response.getProcessDefinition();
  }
  
  public Map<String, Object> getKlassAndTaxonomyColumnInfoFromFile(XSSFWorkbook workbook,
      String endpointId, List<String> sheetsFromProcess) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Set<String> klessesFromFile = new HashSet<>();
    Set<String> taxonomiesFromFile = new HashSet<>();
    Set<String> klassAndTaxonomyHeaders = new HashSet<>();
    mapToReturn.put(OnboardingConstants.KLASSES_FROM_FILE, klessesFromFile);
    mapToReturn.put(OnboardingConstants.TAXONOMIES_FORM_FILE, taxonomiesFromFile);
    mapToReturn.put(OnboardingConstants.KLASS_AND_TAXONOMY_HEADERS, klassAndTaxonomyHeaders);
    
    IGetProcessEndpointEventModel processEvent = getProcessEventByEndpointIdStrategy
        .execute(new IdParameterModel(endpointId));
    
    BpmnModelInstance modelInstance = null;
    try {
      modelInstance = repositoryService.getBpmnModelInstance(processEvent.getProcessDefinitionId());
    }
    catch (NullValueException e) {
      throw new WorkflowEngineException(e);
    }
    catch (ProcessEngineException e) {
      throw new WorkflowEngineException(e);
    }
    Collection<ServiceTask> serviceTasks = modelInstance.getModelElementsByType(ServiceTask.class);
    for (ServiceTask serviceTask : serviceTasks) {
      ExtensionElements extensionElements = serviceTask.getExtensionElements();
      if (extensionElements != null) {
        CamundaInputOutput formData = extensionElements.getElementsQuery()
            .filterByType(CamundaInputOutput.class)
            .singleResult();
        List<String> classCoulmns = new ArrayList<>();
        List<String> taxonomyColumns = new ArrayList<>();
        String sheet = null;
        Integer dataRowNumber = 2;
        Integer headerRowNumber = 1;
        for (CamundaInputParameter formField : formData.getCamundaInputParameters()) {
          String camundaName = formField.getCamundaName();
          String camundaTextContent = formField.getTextContent();
          if (camundaName.equals(IComponentParameterModel.SHEET)) {
            sheet = camundaTextContent;
            sheetsFromProcess.add(camundaTextContent);
          }
          else if (camundaName.equals(IComponentParameterModel.DATA_ROW_NUMBER)) {
            // dataRowNumber = Integer.parseInt(camundaTextContent);
            mapToReturn.put(IComponentParameterModel.DATA_ROW_NUMBER, dataRowNumber);
          }
          else if (camundaName.equals(IComponentParameterModel.HEADER_ROW_NUMBER)) {
            // headerRowNumber = Integer.parseInt(camundaTextContent);
            mapToReturn.put(IComponentParameterModel.HEADER_ROW_NUMBER, headerRowNumber);
          }
          else if (camundaName.equals(Constants.KLASSCOLUMN)) {
            String klassColumn = camundaTextContent;
            if (klassColumn != null && !klassColumn.trim()
                .equals("")) {
              classCoulmns.add(klassColumn);
            }
            klassAndTaxonomyHeaders.addAll(classCoulmns);
          }
          else if (camundaName.equals(Constants.SECONDARY_CLASS_COLUMN_NAME)) {
            String secondaryClassColumnName = camundaTextContent;
            if (secondaryClassColumnName != null && !secondaryClassColumnName.trim()
                .equals("")) {
              classCoulmns.add(secondaryClassColumnName);
            }
            klassAndTaxonomyHeaders.addAll(classCoulmns);
          }
          else if (camundaName.equals(ProcessConstants.TAXONOMY_COLUMNS)) {
            if (camundaTextContent != null && !camundaTextContent.isEmpty()) {
              if (!camundaTextContent.equals("")) {
                taxonomyColumns.addAll(OnboardingUtils.StringToListByNewLine(camundaTextContent));
                klassAndTaxonomyHeaders.addAll(taxonomyColumns);
              }
            }
          }
        }
        Map<String, Object> klassTaxonomyFromSheet = FileUtil.getColumnValueFromFile(workbook,
            sheet, classCoulmns, taxonomyColumns, dataRowNumber, headerRowNumber);
        Set<String> klassesFromSheet = (Set<String>) klassTaxonomyFromSheet
            .get(OnboardingConstants.KLASSES_FROM_SHEET);
        
        if (!klassesFromSheet.isEmpty()) {
          klessesFromFile.addAll(klassesFromSheet);
        }
        
        Set<String> taxonomiesFromSheet = (Set<String>) klassTaxonomyFromSheet
            .get(OnboardingConstants.TAXONOMIES_FROM_SHEET);
        if (!taxonomiesFromSheet.isEmpty()) {
          taxonomiesFromFile.addAll(taxonomiesFromSheet);
        }
      }
    }
    /*if (flow != null) {
      for (String componentInstanceId : flow.keySet()) {
        List<String> classCoulmns = new ArrayList<>();
        List<String> taxonomyColumns = new ArrayList<>();
        Map<String, Object> componentConfig = (Map<String, Object>) flow.get(componentInstanceId);
        String componentId = (String) componentConfig.get("componentId");
        if (componentId.equals("Article_Import")
            || componentId.equals("Assets_Import")
            || componentId.equals("Market_Import")
            || componentId.equals("TAM_Import")
            || componentId.equals("Supplier_Import")
            || componentId.equals("Extended_Article_Import")
            || componentId.equals("Extended_Assets_Import")
            || componentId.equals("Extended_TAM_Import")
            || componentId.equals("Extended_Market_Import")
            || componentId.equals("Extended_Supplier_Import")) {
    
          Map<String, Object> parameters = (Map<String, Object>) componentConfig.get(Constants.PARAMETERS);
          List<Map<String, Object>> dataSources = (List<Map<String, Object>>) parameters
              .get(Constants.DATASOURCES);
          if (dataSources.size() > 0) {
            Map<String, Object> dataSource = dataSources.get(0);
            String sheet = (String) dataSource.get(IComponentParameterModel.SHEET);
            if (sheet != null && !sheet.isEmpty()) {
              sheetsFromProcess.add(sheet);
            }
            Integer headerRowNumber =  (Integer) dataSource.get(IComponentParameterModel.HEADER_ROW_NUMBER);
            Integer dataRowNumber = (Integer) dataSource.get(IComponentParameterModel.DATA_ROW_NUMBER);
            mapToReturn.put(IComponentParameterModel.DATA_ROW_NUMBER, dataRowNumber);
            mapToReturn.put(IComponentParameterModel.HEADER_ROW_NUMBER, headerRowNumber);
            Map<String, Object> classInfo = (Map<String, Object>) dataSource.get(Constants.CLASSINFO);
            if (classInfo.get(Constants.TYPE)
                .equals(Constants.KLASSCOLUMN)
                || classInfo.get(Constants.SECONDARY_CLASS_TYPE)
                    .equals(Constants.KLASSCOLUMN)) {
              String klassColumn = (String) classInfo.get(Constants.KLASSCOLUMN);
              if (klassColumn != null && !klassColumn.trim()
                  .equals("")) {
                classCoulmns.add(klassColumn);
              }
              String secondaryClassColumnName = (String) classInfo
                  .get(Constants.SECONDARY_CLASS_COLUMN_NAME);
              if (secondaryClassColumnName != null && !secondaryClassColumnName.trim()
                  .equals("")) {
                classCoulmns.add(secondaryClassColumnName);
              }
              klassAndTaxonomyHeaders.addAll(classCoulmns);
            }
    
            List<Map<String, String>> taxonomyMapping = (List<Map<String, String>>) classInfo
                .get(Constants.TAXONOMIES);
            if (taxonomyMapping != null && !taxonomyMapping.isEmpty()) {
              for (Map<String, String> taxonomy : taxonomyMapping) {
                if (!taxonomy.get(Constants.TAXONOMY_COLUMN)
                    .trim()
                    .equals("")) {
                  taxonomyColumns.addAll(
                      OnboardingUtils.StringToList(taxonomy.get(Constants.TAXONOMY_COLUMN)));
                  klassAndTaxonomyHeaders.addAll(taxonomyColumns);
                }
              }
            }
    
            Map<String, Object> klassTaxonomyFromSheet = FileUtil.getColumnValueFromFile(file,
                sheet, classCoulmns, taxonomyColumns, dataRowNumber, headerRowNumber);
            Set<String> klassesFromSheet = (Set<String>) klassTaxonomyFromSheet
                .get(OnboardingConstants.KLASSES_FROM_SHEET);
    
            if (!klassesFromSheet.isEmpty()) {
              klessesFromFile.addAll(klassesFromSheet);
            }
    
            Set<String> taxonomiesFromSheet = (Set<String>) klassTaxonomyFromSheet
                .get(OnboardingConstants.TAXONOMIES_FROM_SHEET);
            if (!taxonomiesFromSheet.isEmpty()) {
              taxonomiesFromFile.addAll(taxonomiesFromSheet);
            }
          }
        }
        else {
          Map<String, Object> parameters = (Map<String, Object>) componentConfig
              .get(Constants.PARAMETERS);
          List<Map<String, Object>> dataSources = (List<Map<String, Object>>) parameters
              .get(Constants.DATASOURCES);
          if (dataSources.size() > 0) {
            Map<String, Object> dataSource = dataSources.get(0);
            String sheet = (String) dataSource.get(IComponentParameterModel.SHEET);
            if (sheet != null && !sheet.isEmpty()) {
              sheetsFromProcess.add(sheet);
            }
          }
        }
      }
    }*/
    return mapToReturn;
  }
  
  /*  public Set<String> getTaxonomyColumnInfoToSheet(File file,  Map<String, Object> flow) throws Exception
  {
    Set<String> mapToReturn = new HashSet<>();
  
    if (flow != null) {
      for (String componentInstanceId : flow.keySet()) {
        List<String> classCoulmns = new ArrayList<>();
        Map<String, Object> componentConfig = (Map<String, Object>) flow.get(componentInstanceId);
        String componentId = (String) componentConfig.get("componentId");
        if (componentId.equals("Article_Import")
            || componentId.equals("Assets_Import")
            || componentId.equals("Market_Import")
            || componentId.equals("TAM_Import")
            || componentId.equals("Extended_Article_Import")
            || componentId.equals("Extended_Assets_Import")
            || componentId.equals("Extended_TAM_Import")
            || componentId.equals("Extended_Market_Import")) {
  
          Map<String, Object> parameters = (Map<String, Object>) componentConfig.get("parameters");
          List<Map<String, Object>> dataSources = (List<Map<String, Object>>) parameters
              .get("dataSources");
          if (dataSources.size() > 0) {
            Map<String, Object> dataSource = dataSources.get(0);
            String sheet = (String) dataSource.get("sheet");
            Integer headerRowNumber =  (Integer) dataSource.get("headerRowNumber");
            Integer dataRowNumber = (Integer) dataSource.get("dataRowNumber");
            Map<String, Object> classInfo = (Map<String, Object>) dataSource.get(Constants.CLASSINFO);
            if (classInfo.get("type").equals("klassColumn") || classInfo.get("secondaryClassType").equals("klassColumn")) {
              String klassColumn = (String) classInfo.get("klassColumn");
              if (klassColumn != null && !klassColumn.trim().equals("")) {
                classCoulmns.add(klassColumn);
              }
              String secondaryClassColumnName = (String) classInfo.get("secondaryClassColumnName");
              if (secondaryClassColumnName != null && !secondaryClassColumnName.trim().equals("")) {
                classCoulmns.add(secondaryClassColumnName);
              }
              if(!classCoulmns.isEmpty()){
                Set<String> classes = FileUtil.getColumnValueFromFile(file, sheet, classCoulmns, dataRowNumber, headerRowNumber);
                mapToReturn.addAll(classes);
              }
            }
          }
  
        }
      }
    }
    return mapToReturn;
  
  }*/
  
  public void getRuntimeMappings(IRuntimeMappingModel endpoint,
      Map<String, Object> klassesTaxonomiesFromFile, XSSFWorkbook workbook,
      List<String> sheetsFromProcess, String endpointId) throws Exception
  {
    Set<String> mappedColumnNames = new HashSet<>();
    Set<String> mappedClassColumnNames = new HashSet<>();
    Set<String> mappedTaxonomyColumnNames = new HashSet<>();
    Set<String> tagColumnNames = new HashSet<>();
    Map<String, IConfigRuleTagMappingModel> tagMappings = new HashMap<>();
    
    Integer dataRowNumber = (Integer) klassesTaxonomiesFromFile
        .remove(IComponentParameterModel.DATA_ROW_NUMBER);
    Integer headerRowNumber = (Integer) klassesTaxonomiesFromFile
        .remove(IComponentParameterModel.HEADER_ROW_NUMBER);
    
    Map<String, ITag> tags = endpoint.getConfigDetails()
        .getTags();
    
    for (IConfigRuleTagMappingModel configRuleMapping : endpoint.getTagMappings()) {
      String columnName = configRuleMapping.getColumnNames()
          .get(0);
      mappedColumnNames.add(columnName);
      if (configRuleMapping.getTagValueMappings()
          .size() > 0) {
        tagColumnNames.add(columnName);
        tagMappings.put(columnName, configRuleMapping);
      }
      // if
      // (configRuleMapping.getTagValueMappings().get(0).getMappings().isEmpty())
      // {
      // If nothing is passed in tag value mapping it will return mapped element
      // Id as null.
      if (configRuleMapping.getMappedElementId() == null) {
        continue;
      }
      IGetTagValueFromColumnModel getTagValueModel = new GetTagValueFromColumnModel();
      getTagValueModel.setColumnName(columnName);
      getTagValueModel.setFileId(endpoint.getFileInstanceId());
      getTagValueModel.setTagGroupId(configRuleMapping.getMappedElementId());
      getTagValueModel.setDataRowNumber(dataRowNumber);
      getTagValueModel.setHeaderRowNumber(headerRowNumber);
      getTagValueModel.setSheetsFromProcess(sheetsFromProcess);
      getTagValueModel.setWorkBook(workbook);
      IColumnTagValueAutoMappingModel tagValueMapping = getTagValuesFromColumn
          .execute(getTagValueModel);
      ITag tag = tagValueMapping.getTag();
      String tagType = tag.getTagType();
      List<IColumnValueTagValueMappingModel> tagValueMappings = new ArrayList<>();
      tagValueMapping.setColumnName(columnName);
      tagValueMapping.setTagValueIds(configRuleMapping.getTagValueMappings()
          .get(0)
          .getTagValueIds());
      if (tagType.equals(SystemLevelIds.BOOLEAN_TAG_TYPE_ID)) {
        tagValueMapping.setMappings(new ArrayList<>());
      }
      
      tagValueMappings.add(tagValueMapping);
      configRuleMapping.setTagValueMappings(tagValueMappings);
      tags.put(configRuleMapping.getMappedElementId(), tag);
      // }
    }
    endpoint.getConfigDetails()
        .setTags(tags);
    
    for (IConfigRuleAttributeMappingModel configRuleMapping : endpoint.getAttributeMappings()) {
      String columnName = configRuleMapping.getColumnNames()
          .get(0);
      mappedColumnNames.add(columnName);
    }
    
    for (IConfigRuleClassMappingModel classMapping : endpoint.getClassMappings()) {
      for (String columnName : classMapping.getColumnNames()) {
        if (columnName != null && !columnName.equals("")) {
          mappedClassColumnNames.add(columnName);
        }
      }
    }
    
    for (IConfigRuleTaxonomyMappingModel classMapping : endpoint.getTaxonomyMappings()) {
      for (String columnName : classMapping.getColumnNames()) {
        if (columnName != null && !columnName.equals("")) {
          mappedTaxonomyColumnNames.add(columnName);
        }
      }
    }
    
    /*FOR Getting unmapped columns
     */
    
    Set<String> classes = (Set<String>) klassesTaxonomiesFromFile
        .get(OnboardingConstants.KLASSES_FROM_FILE);
    Set<String> taxonomies = (Set<String>) klassesTaxonomiesFromFile
        .get(OnboardingConstants.TAXONOMIES_FORM_FILE);
    Set<String> klassAndTaxonomyHeaders = (Set<String>) klassesTaxonomiesFromFile
        .get(OnboardingConstants.KLASS_AND_TAXONOMY_HEADERS);
    
    classes.removeAll(mappedClassColumnNames);
    
    List<String> unmappedClasses = new ArrayList<>(classes);
    endpoint.setUnmappedKlassColumns(unmappedClasses);
    
    taxonomies.removeAll(mappedTaxonomyColumnNames);
    List<String> unmappedTaxonoies = new ArrayList<>(taxonomies);
    
    endpoint.setUnmappedTaxonomyColumns(unmappedTaxonoies);
    List<String> headerList = FileUtil.getRuntimeMappingsFromFile(workbook, tagColumnNames,
        tagMappings, sheetsFromProcess);
    headerList.removeAll(mappedColumnNames);
    headerList.removeAll(klassAndTaxonomyHeaders);
    endpoint.setUnmappedColumns(headerList);
    processToSetShowMapping(endpoint);
  }
  
  private void processToSetShowMapping(IRuntimeMappingModel endpoint)
  {
    Boolean isAutoMapping = false;
    isAutoMapping = showMappingForUnMappedColumn(endpoint.getUnmappedColumns());
    
    if (!isAutoMapping) {
      isAutoMapping = showMappingForAttribute(endpoint.getAttributeMappings());
    }
    
    if (!isAutoMapping) {
      isAutoMapping = showMappingForTag(endpoint.getTagMappings());
    }
    
    if (!isAutoMapping) {
      isAutoMapping = showMappingForKlass(endpoint.getClassMappings());
    }
    
    if (!isAutoMapping) {
      isAutoMapping = showMappingForTaxonomy(endpoint.getTaxonomyMappings());
    }
    endpoint.setIsShowMapping(isAutoMapping);
  }
  
  private Boolean showMappingForUnMappedColumn(List<String> unmappedColumns)
  {
    if (unmappedColumns != null && unmappedColumns.size() > 0) {
      return true;
    }
    return false;
  }
  
  private Boolean showMappingForTaxonomy(List<IConfigRuleTaxonomyMappingModel> taxonomyMappings)
  {
    for (IConfigRuleTaxonomyMappingModel iConfigRuleTaxonomyMappingModel : taxonomyMappings) {
      if (iConfigRuleTaxonomyMappingModel.getIsAutomapped()) {
        return true;
      }
    }
    return false;
  }
  
  private Boolean showMappingForKlass(List<IConfigRuleClassMappingModel> classMappings)
  {
    for (IConfigRuleClassMappingModel iConfigRuleClassMappingModel : classMappings) {
      if (iConfigRuleClassMappingModel.getIsAutomapped()) {
        return true;
      }
    }
    return false;
  }
  
  private Boolean showMappingForTag(List<IConfigRuleTagMappingModel> listOfTagsMapping)
  {
    for (IConfigRuleTagMappingModel iConfigRuleTagMappingModel : listOfTagsMapping) {
      if (iConfigRuleTagMappingModel.getIsAutomapped()) {
        return true;
      }
    }
    return false;
  }
  
  private Boolean showMappingForAttribute(
      List<IConfigRuleAttributeMappingModel> listofAttributeMapping)
  {
    for (IConfigRuleAttributeMappingModel iConfigRuleAttributeMappingModel : listofAttributeMapping) {
      if (iConfigRuleAttributeMappingModel.getIsAutomapped()) {
        return true;
      }
    }
    return false;
  }
  
  public static String getValueFromSheetOfBoolean(Map<String, Object> eachVariant,
      Map<String, String> tagsMapping, String tagId)
  {
    String tagValueId = null;
    Set<String> keySetOfValues = eachVariant.keySet();
    for (String key : keySetOfValues) {
      String tagIdFromMap = tagsMapping.get(key);
      if (tagIdFromMap != null && tagIdFromMap.equals(tagId)) {
        tagValueId = ((String) eachVariant.get(key)).trim();
      }
    }
    return tagValueId;
  }
  
  public static void prepareTagValuesForBooleanTagType(List<ITagInstanceValue> tagValues,
      String tagId, Integer relevence, ITag iTag) throws RDBMSException
  {
    ITagInstanceValue tagValue = new TagInstanceValue();
    tagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagValue.setTagId(iTag.getChildren()
        .get(0)
        .getId());
    tagValue.setRelevance(relevence);
    tagValue.setTimestamp(Long.toString(System.currentTimeMillis()));
    tagValues.add(tagValue);
  }
  
  public static void prepareTagValuesForModifiedBooleanTagType(List<ITagInstanceValue> tagValues,
      String tagId, Integer relevence, List<ITagInstanceValue> listOfExistTagValue)
  {
    ITagInstanceValue tagValue = new TagInstanceValue();
    tagValue.setId(OnboardingUtils.getIdOfTagValue(listOfExistTagValue, listOfExistTagValue.get(0)
        .getTagId()));
    tagValue
        .setTagId(OnboardingUtils.getTagIdOfTagValue(listOfExistTagValue, listOfExistTagValue.get(0)
            .getTagId()));
    tagValue.setRelevance(relevence);
    tagValue.setTimestamp(Long.toString(System.currentTimeMillis()));
    tagValues.add(tagValue);
  }
  
  public static String getIdOfTagValue(List<ITagInstanceValue> listOfExistTagValue,
      String tagValueIdForModifiedTag)
  {
    for (ITagInstanceValue iTagValue : listOfExistTagValue) {
      if (iTagValue.getTagId()
          .equals(tagValueIdForModifiedTag)) {
        return iTagValue.getId();
      }
    }
    return null;
  }
  
  public static String getTagIdOfTagValue(List<ITagInstanceValue> listOfExistTagValue,
      String tagValueIdForModifiedTag)
  {
    for (ITagInstanceValue iTagValue : listOfExistTagValue) {
      if (iTagValue.getTagId()
          .equals(tagValueIdForModifiedTag)) {
        return iTagValue.getTagId();
      }
    }
    return null;
  }
  
  public static long getLongValueOfDateString(String dateAsString)
  {
    Long time = null;
    if (dateAsString != null) {
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      Date date;
      try {
        date = formatter.parse(dateAsString);
        time = date.getTime();
      }
      catch (ParseException e) {
        // TODO Auto-generated catch block
        RDBMSLogger.instance().exception(e);
      }
    }
    return time;
  }
  
  public static void prepareContextInformation(IComponentParameterModel paramModel,
      IGetMappingForImportResponseModel mappings,
      HashMap<String, HashMap<String, Object>> tagValuesMapping, Map<String, Object> contextMap) throws RDBMSException
  {
    // Prepare context information..
    Map<String, Object> contextTagsMap = (Map<String, Object>) contextMap.get("contextTagsMap");
    List<IContentTagInstance> tags = new ArrayList<>();
    
    // Create context tag instances..
    for (Entry<String, Object> itemEntry : contextTagsMap.entrySet()) {
      String tagGroupId = mappings.getTags()
          .get(itemEntry.getKey());
      String tagValueId = (String) itemEntry.getValue();
      List<ITagInstanceValue> adddedTagValues = new ArrayList<>();
      IContentTagInstance tag = OnboardingUtils.createNewTagInstanceFromMappings(contextTagsMap,
          mappings.getTags(), tagValuesMapping, new HashMap<>(), tagGroupId, tagValueId,
          new ArrayList<>(), new ArrayList<>(), adddedTagValues);
      tags.add(tag);
    }
    contextMap.put("contextTagsMap", tags);
    
    // prepare timerange..
    Map<String, Object> timeRangeMap = (Map<String, Object>) contextMap.get("contextTimeRangeMap");
    IInstanceTimeRange timeRange = new InstanceTimeRange();
    try {
      long fromDate = OnboardingUtils
          .getLongValueOfDateString((String) timeRangeMap.get(paramModel.getFromDateColumn()));
      long toDate = OnboardingUtils
          .getLongValueOfDateString((String) timeRangeMap.get(paramModel.getToDateColumn()));
      
      timeRange.setFrom(fromDate);
      timeRange.setTo(toDate);
    }
    catch (Exception e) {
      // RDBMSLogger.instance().exception(e);
    }
    contextMap.put("contextTimeRangeMap", timeRange);
  }
  
  public static List<String> getListOfIgnoredClass(IMappingModel propertyMapping)
  {
    List<IConfigRuleClassMappingModel> classMappings = propertyMapping.getClassMappings();
    List<String> classMappingIgnored = new ArrayList<>();
    for (IConfigRuleClassMappingModel iConfigRuleClassMappingModel : classMappings) {
      if (iConfigRuleClassMappingModel.getIsIgnored() == true) {
        classMappingIgnored.add(iConfigRuleClassMappingModel.getColumnNames()
            .size() > 0 ? iConfigRuleClassMappingModel.getColumnNames()
                .get(0) : "");
      }
    }
    return classMappingIgnored;
  }
  
  public static void setTransactionData(IComponentModel model, ITransactionData transactionData)
  {
    transactionData.setEndpointId(model.getEndpointId());
    transactionData.setPhysicalCatalogId(model.getPhysicalCatalogId());
    transactionData.setOrganizationId(model.getOrganizationId());
    transactionData.setSystemId(model.getSystemId());
    transactionData.setLogicalCatalogId(model.getLogicalCatalogId());
    transactionData.setDataLanguage(model.getDataLanguage());
  }
  
  public String getUniqueInstanceIdForImport(String klassInstanceId, String endpointId,
      String organizationId)
  {
    return klassInstanceId + "_" + endpointId + "_" + organizationId;
  }
  
  /*
   * @Author Abhaypratap Singh
   * Prepare the list of taxonomyIds from file to import
   * And required dependent method to fetch the taxonomy id on the basis of Code
   *
   *
   */
  public List<String> prepareTaxonomyIdsForImport(IComponentParameterModel componentParameters,
      IGetMappingForImportResponseModel mappings, Map<String, Object> itemMap,
      List<String> skippedTaxonomyList) throws Exception
  {
    Map<String, String> taxonomyMappedIds = mappings.getTaxonomies();
    List<String> taxonomyIds = new ArrayList<String>();
    Set<String> taxonomyColumnList = componentParameters.getTaxonomyColumnList();
    List<String> taxonomyCodeList = new ArrayList<>();
    for (String taxonomyColumn : taxonomyColumnList) {
      String taxonomyValues = ((String) itemMap.get(taxonomyColumn)).trim();
      if (taxonomyValues != null && !taxonomyValues.equals("")) {
        for (String taxonomyValue : OnboardingUtils
            .StringToList((String) itemMap.get(taxonomyColumn))) {
          if (taxonomyMappedIds.containsKey(taxonomyValue)
              && !(skippedTaxonomyList.contains(taxonomyMappedIds.get(taxonomyValue)))) {
            taxonomyIds.add(taxonomyMappedIds.get(taxonomyValue));
          }
          else {
            taxonomyCodeList.add(taxonomyValue);
          }
        }
      }
    }
    if (!taxonomyCodeList.isEmpty()) {
      taxonomyIds.addAll(getTaxonomyIdsForCode(taxonomyCodeList));
    }
    return taxonomyIds;
  }
  
  private List<String> getTaxonomyIdsForCode(List<String> taxonomyCodeList) throws Exception
  {
    IListModel<String> idsModel = new ListModel<>();
    idsModel.setList(taxonomyCodeList);
    
    IIdsListParameterModel response = getTaxonomyIdsByCodeIdForOnboardingStrategy.execute(idsModel);
    return response.getIds();
  }
  
  /*
   * @Author Abhaypratap Singh
   * Prepare the list of skipped taxonomyIds
   * which is not going to import
   *
   */
  
  public List<String> prepareSkippedTaxonomyList(IMappingModel propertyMapping)
  {
    List<IConfigRuleTaxonomyMappingModel> taxonomyMappings = propertyMapping.getTaxonomyMappings();
    List<String> skippedTaxonomyList = new ArrayList<>();
    for (IConfigRuleTaxonomyMappingModel iConfigRuleTaxonomyMappingModel : taxonomyMappings) {
      if (iConfigRuleTaxonomyMappingModel.getIsIgnored()) {
        skippedTaxonomyList.add(iConfigRuleTaxonomyMappingModel.getId());
      }
    }
    return skippedTaxonomyList;
  }
  
  public IGetImportedEntityStatusResponseModel getCompomentStatusOnCompletion(
      Long actualInstancesImported, String componentId, String processInstanceId) throws Exception
  {
    /*
      IGetImportedEntityStatusRequestModel requestModel = new GetImportedEntityStatusRequestModel();
      requestModel.setComponentId(componentId);
      requestModel.setProcessInstanceId(processInstanceId);
      requestModel.setImportedEntityCount(actualInstancesImported);
    
      while(true) {
        IGetImportedEntityStatusResponseModel responseModel = getProcessInstanceUpdatesStrategy.execute(requestModel);
        if(responseModel.getIsComponentCompleted()) {
          return responseModel;
        }
    
        Thread.sleep(fileUploadConsumerMaxPollInterval);
      }
    */
    return null;
  }
  
  public void getCompomentStatusOnCompletion(String systemComponentId, String processInstanceId)
      throws Exception
  {
    /*ProcessStatusDetailsModel model = new ProcessStatusDetailsModel();
    model.setComponentId(systemComponentId);
    model.setProcessInstanceId(processInstanceId);
    
    while (true) {
      IProcessStatusDetailsModel returnModel = getProcessStatusByIdStrategy.execute(model);
      if (returnModel.getInprogressCount() == 0) {
        returnModel.setEndTime(new Long(System.currentTimeMillis()).toString());
        returnModel.setStatus(true);
        saveProcessStatusStrategy.execute(returnModel);
        break;
      }
      
      Thread.sleep(fileUploadConsumerMaxPollInterval);
    }*/
  }
  
  public void saveProcessInstanceUpdates(String processInstanceId, String componentId,
      String instanceId, String failureReason) throws Exception
  {
    IImportedEntityStatusModel updatesModel = new ImportedEntityStatusModel();
    updatesModel.setProcessInstanceId(processInstanceId);
    updatesModel.setComponentId(componentId);
    updatesModel.setKlassInstanceId(instanceId);
    updatesModel.setImportStatus(failureReason == null ? "success" : "failure");
    updatesModel.setExceptionMessage(failureReason);
    
    // saveImportedEntityStatusStrategy.execute(updatesModel);
  }
  
  public void getProcesstStatusOnCompletion(String processInstanceId) throws Exception
  {
    ProcessStatusDetailsModel model = new ProcessStatusDetailsModel();
    model.setProcessInstanceId(processInstanceId);
    
    while (true) {
      Boolean currentStatus = true;
      List<IProcessStatusDetailsModel> processStatusDetailsModels = (List<IProcessStatusDetailsModel>) getProcessStatusByProcessInstanceIdStrategy
          .execute(model)
          .getList();
      for (int i = 0; i < processStatusDetailsModels.size(); i++) {
        if (processStatusDetailsModels.get(i)
            .getEndTime() == null) {
          currentStatus = false;
        }
      }
      
      if (currentStatus == true)
        break;
      Thread.sleep(5000);
    }
  }
  
}
