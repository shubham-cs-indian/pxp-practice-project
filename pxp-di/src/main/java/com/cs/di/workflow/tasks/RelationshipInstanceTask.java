package com.cs.di.workflow.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.component.klassinstance.DiContextModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.DiTimeRangeModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiContextModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiTimeRangeModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForOnboarding;
import com.cs.core.runtime.interactor.usecase.articleinstance.ISaveArticleInstanceRelationships;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceForOnboarding;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.runtime.interactor.usecase.assetinstance.ISaveAssetInstanceRelationships;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.EntityAction;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.relationship.IRelationshipContextModel;
import com.cs.di.workflow.model.relationship.IRelationshipInstanceModel;
import com.cs.di.workflow.model.relationship.RelationshipContextModel;
import com.cs.di.workflow.model.relationship.RelationshipInstanceModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("relationshipInstanceTask")
@SuppressWarnings("unchecked")
public class RelationshipInstanceTask extends AbstractTask {
  
  public static final String                  SIDE_1_IID             = "SIDE_1_IID";
  public static final String                  SIDE_2_IIDS            = "SIDE_2_IIDS";
  public static final String                  RELATIONSHIP_CODE      = "RELATIONSHIP_CODE";
  public static final String                  SOURCE_TYPE            = "SOURCE_TYPE";
  public static final String                  CONTEXT                = "CONTEXT";
  public static final String                  ACTION                 = "ACTION";
  public static final String                  INSTANCE_RELATIONSHIPS = "INSTANCE_RELATIONSHIPS";
  public static final List<EventType>         EVENT_TYPES            = Arrays
      .asList(EventType.INTEGRATION);
  public static final List<WorkflowType>      WORKFLOW_TYPES         = Arrays
      .asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<String>            OUTPUT_LIST            = Arrays
      .asList(INSTANCE_RELATIONSHIPS, EXECUTION_STATUS);
  public static final List<String>            INPUT_LIST             = Arrays.asList(SIDE_1_IID,
      RELATIONSHIP_CODE, SIDE_2_IIDS, CONTEXT, SOURCE_TYPE, ACTION);
  
  @Autowired
  protected IGetArticleInstanceForOnboarding  getArticleInstanceForOnboarding;
  
  @Autowired
  protected IGetAssetInstanceForOnboarding    getAssetInstanceForOnboarding;
  
  @Autowired
  protected ISaveArticleInstanceRelationships saveArticleInstanceRelationships;
  
  @Autowired
  protected ISaveAssetInstanceRelationships   saveAssetInstanceRelationships;
  
  @Autowired
  protected TransactionThreadData             transactionThreadData;
  
  @Autowired
  protected SessionContextCustomProxy     context;
    
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
  
  @Override
  public void executeTask(WorkflowTaskModel workflowTaskModel)
  {
    EntityAction action = DiValidationUtil.validateAndGetRequiredEnum(workflowTaskModel, ACTION, EntityAction.class);
    String side1IId = (String) DiValidationUtil.validateAndGetRequiredString(workflowTaskModel,
        SIDE_1_IID);
    List<String> oppositeInstanceIds = (List<String>) DiValidationUtil
        .validateAndGetOptionalCollection(workflowTaskModel, SIDE_2_IIDS);
    String relationshipCode = (String) DiValidationUtil
        .validateAndGetRequiredString(workflowTaskModel, RELATIONSHIP_CODE);
    String sourceType = (String) DiValidationUtil.validateAndGetRequiredString(workflowTaskModel,
        SOURCE_TYPE);
    context.setUserSessionDTOInThreadLocal(workflowTaskModel.getWorkflowModel().getUserSessionDto());
    String contextVariable = null;
    if(EntityAction.UPSERT.equals(action)) {
      contextVariable = (String) DiValidationUtil.validateAndGetRequiredString(workflowTaskModel,
          CONTEXT);
    }
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel
        .getExecutionStatusTable();
    try {
      if (executionStatusTable.isErrorOccurred()) {
        return;
      }
      if (oppositeInstanceIds == null) {
        oppositeInstanceIds = new ArrayList<>();
      }
      transactionThreadData.setTransactionData(DiUtils.getTransactionData(workflowTaskModel));
      IGetKlassInstanceCustomTabModel klassInstance = getKlassInstance(sourceType, side1IId);
      if (klassInstance != null) {
        IGetConfigDetailsForCustomTabModel configDetails = (IGetConfigDetailsForCustomTabModel) klassInstance
            .getConfigDetails();
        boolean isNature = isNatureRelationship(configDetails, relationshipCode);
        
        Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstanceElements;
        List<IKlassInstanceRelationshipInstance> relationships;
        if (isNature) {
          referenceRelationshipInstanceElements = klassInstance
              .getReferenceNatureRelationshipInstanceElements();
          relationships = klassInstance.getNatureRelationships();
        }
        else {
          referenceRelationshipInstanceElements = klassInstance
              .getReferenceRelationshipInstanceElements();
          relationships = klassInstance.getContentRelationships();
        }
        IKlassInstanceRelationshipInstance relationshipInstance = getRelationshipInstance(
            relationships, relationshipCode);
        String sideId = relationshipInstance.getSideId();
        List<String> existingOppositeInstanceIds = collectExistingOppositeInstanceIds(
            referenceRelationshipInstanceElements.get(sideId));
        
        IReferencedSectionRelationshipModel referencedSectionRelationshipModel = (IReferencedSectionRelationshipModel) configDetails
            .getReferencedElements().get(sideId);
        String targetType = referencedSectionRelationshipModel.getRelationshipSide()
            .getTargetType();
        switch (action) {
          case UPSERT:
          case DELETE:
            // restrict UPSERT/ DELETE from non editable side
            if (!isNature) {
              IReferencedRelationshipModel relatonshipModel = configDetails.getReferencedRelationships().get(relationshipCode);
              if (relatonshipModel.getSide1().getElementId().equals(sideId)) {
                if (!relatonshipModel.getSide1().getIsVisible()) { 
                  return;
                }
              }
              else if (!relatonshipModel.getSide2().getIsVisible()) {
                return;
              }
            }
            else {
              IGetReferencedNatureRelationshipModel refRel = configDetails.getReferencedNatureRelationships().get(relationshipCode);
              if (refRel.getSide1().getElementId().equals(sideId)) {
                if (!refRel.getSide1().getIsVisible()) {
                  return;
                }
              }
              else if (!refRel.getSide2().getIsVisible()) {
                return;
              }
            }
            List<IRelationshipContextModel> contextModels = ObjectMapperUtil.readValue(
                contextVariable, new TypeReference<ArrayList<RelationshipContextModel>>(){});
            upsertOrDeleteRelationships(side1IId, relationshipCode, oppositeInstanceIds,
                contextModels, klassInstance, sourceType, action, existingOppositeInstanceIds,
                targetType, relationshipInstance, isNature);
            break;
          case READ:
            readRelationshipInstances(workflowTaskModel.getOutputParameters(), oppositeInstanceIds,
                relationshipInstance, existingOppositeInstanceIds,
                referencedSectionRelationshipModel, side1IId, sourceType);
            break;
          default:
            executionStatusTable.addWarning(MessageCode.GEN500);
        }
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  /**
   * @param outputParameter
   * @param oppositeInstanceIds
   * @param relationshipInstance
   * @param existingOppositeInstanceIds
   * @param referencedSectionRelationshipModel
   * @param side1IId
   * @param sourceType
   */
  private void readRelationshipInstances(Map<String, Object> outputParameter,
      List<String> oppositeInstanceIds, IKlassInstanceRelationshipInstance relationshipInstance,
      List<String> existingOppositeInstanceIds,
      IReferencedSectionRelationshipModel referencedSectionRelationshipModel, String side1IId,
      String sourceType)
  {
    IRelationshipInstanceModel readRelationshipCompModel = new RelationshipInstanceModel();
    String contextId = referencedSectionRelationshipModel.getRelationshipSide()
        .getContextId();
    List<String> side2IdsToRead = oppositeInstanceIds.isEmpty() ? existingOppositeInstanceIds
        : oppositeInstanceIds;
    readRelationshipCompModel.setInstanceId(side1IId);
    readRelationshipCompModel.setRelationshipId(relationshipInstance.getRelationshipId());
    readRelationshipCompModel.setSourceType(sourceType);
    readRelationshipCompModel.setOppositeInstanceId(side2IdsToRead);
    for (String side2Id : side2IdsToRead) {
      IRelationshipContextModel contextModel = new RelationshipContextModel();
      contextModel.setSide2Id(side2Id);
      IDiContextModel dicontextModel = new DiContextModel();
      dicontextModel.setContextId(contextId);
      if (!isBlank(contextId)) {
        Map<String, List<String>> contextTags = dicontextModel.getTags();
        relationshipInstance.getElementTagMapping().get(side2Id)
          .forEach(l -> {contextTags.put(l.getTagId(), l.getTagValues()
            .stream().map(tv -> tv.getCode()).collect(Collectors.toList()));
            });
        IInstanceTimeRange iInstanceTimeRange = relationshipInstance.getElementTimeRangeMapping()
            .get(side2Id);
        IDiTimeRangeModel diTimeRangeModel = new DiTimeRangeModel();
        diTimeRangeModel.setFrom(convertDate(iInstanceTimeRange.getFrom()));
        diTimeRangeModel.setTo(convertDate(iInstanceTimeRange.getTo()));
        dicontextModel.setTimeRange(diTimeRangeModel);
        contextModel.setContext(dicontextModel);
      }
      readRelationshipCompModel.getContext().add(contextModel);
    }
    outputParameter.put(INSTANCE_RELATIONSHIPS, readRelationshipCompModel);
  }
  
  /**
   * @param lDate
   * @return
   */
  private String convertDate(long lDate)
  {
    if (lDate > 0) {
      Date date = new Date(lDate);
      SimpleDateFormat dateFormat = new SimpleDateFormat(DiConstants.DATE_FORMAT);
      String dateText = dateFormat.format(date);
      return dateText;
    }
    return "";
  }
  
  /**
   * @param oppositeInstanceIds
   * @param contextModels
   * @return
   */
  private List<IRelationshipContextModel> getFilteredContextData(
      List<String> oppositeInstanceIds, List<IRelationshipContextModel> contextModels)
  {
    List<IRelationshipContextModel> modifiedContextModels = new ArrayList<>();
    for (IRelationshipContextModel contextModel : contextModels) {
      if (oppositeInstanceIds.contains(contextModel.getSide2Id())) {
        modifiedContextModels.add(contextModel);
      }
    }
    return modifiedContextModels;
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
  
  /**
   * This method prepare Model for updating relationship.
   * 
   * @param instanceId
   * @param relationshipCode
   * @param side2Ids
   * @param contextModels
   * @param klassInstance
   * @param sourceType
   * @param action
   * @param existingOppositeInstanceIds
   * @param targetType
   * @param relationshipInstance
   * @param isNature
   * @throws Exception
   */
  private void upsertOrDeleteRelationships(String instanceId, String relationshipCode,
      List<String> side2Ids, List<IRelationshipContextModel> contextModels,
      IGetKlassInstanceCustomTabModel klassInstance, String sourceType, EntityAction action,
      List<String> existingOppositeInstanceIds, String targetType,
      IKlassInstanceRelationshipInstance relationshipInstance, boolean isNature) throws Exception
  {
    List<String> side2IdsToCreate = side2Ids.stream()
        .filter(v -> !existingOppositeInstanceIds.contains(v))
        .collect(Collectors.toList());
    List<String> side2IdsToUpdate = new ArrayList<>(side2Ids);
    side2IdsToUpdate.retainAll(existingOppositeInstanceIds);
    String baseType = klassInstance.getKlassInstance()
        .getBaseType();
    ISaveRelationshipInstanceModel saveRelationshipInstanceModel = new SaveRelationshipInstanceModel();
    saveRelationshipInstanceModel.setId(instanceId);
    saveRelationshipInstanceModel.setBaseType(baseType);
    saveRelationshipInstanceModel.setTabId(SystemLevelIds.RELATIONSHIP_TAB);
    saveRelationshipInstanceModel.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
    if (!side2IdsToUpdate.isEmpty() || !side2IdsToCreate.isEmpty() || EntityAction.DELETE.equals(action)) {
      IContentRelationshipInstanceModel contentRelationshipInstanceModel = getContentRelationshipInstanceModel(
          targetType, relationshipCode, relationshipInstance.getSideId());
      if (EntityAction.UPSERT.equals(action)) {
        updatedRelationshipInstances(getFilteredContextData(side2IdsToUpdate, contextModels),
            (IGetConfigDetailsForCustomTabModel) klassInstance.getConfigDetails(),
            relationshipInstance, isNature, baseType, contentRelationshipInstanceModel);
        addRelationshipInstances(getFilteredContextData(side2IdsToCreate, contextModels),
            klassInstance, isNature, contentRelationshipInstanceModel);
        if (contentRelationshipInstanceModel.getModifiedElements().isEmpty() && contentRelationshipInstanceModel.getAddedElements().isEmpty()) {
          return;
        }
      }
      else {
        contentRelationshipInstanceModel
            .setDeletedElements(side2Ids.isEmpty() ? existingOppositeInstanceIds : side2Ids);
      }
      getModifiedRelationshipType(isNature, saveRelationshipInstanceModel)
          .add(contentRelationshipInstanceModel);
      saveRelationshipInstance(sourceType, saveRelationshipInstanceModel);
    }
  }
  
  /**
   * @param contextModels
   * @param klassInstance
   * @param isNature
   * @param contentRelationshipInstanceModel
   */
  private void addRelationshipInstances(List<IRelationshipContextModel> contextModels,
      IGetKlassInstanceCustomTabModel klassInstance, Boolean isNature,
      IContentRelationshipInstanceModel contentRelationshipInstanceModel)
  {
    if (contextModels.isEmpty()) {
      return;
    }
    IGetConfigDetailsForCustomTabModel configDetails = (IGetConfigDetailsForCustomTabModel) klassInstance
        .getConfigDetails();
    List<IRelationshipVersion> relationshipVersions = new ArrayList<>();
    for (IRelationshipContextModel relationshipcontextModel : contextModels) {
      IDiContextModel contextModel = relationshipcontextModel.getContext();
      String oppositeInstanceId = relationshipcontextModel.getSide2Id();
      List<IContentTagInstance> contextTags = new ArrayList<>();
      IInstanceTimeRange timeRange = new InstanceTimeRange();
      if (contextModel != null) {
        getContextTagInfo(configDetails.getReferencedTags(), contextModel.getTags());
        if (!isBlank(contextModel.getContextId())) {
          prepareContextTags(contextTags, contextModel, configDetails.getReferencedTags());
          prepareTimeRangeData(timeRange, contextModel.getTimeRange());
        }
      }
      IRelationshipVersion relationshipVersionmodel = new RelationshipVersion();
      relationshipVersionmodel.setId(oppositeInstanceId);
      relationshipVersionmodel.setTags(contextTags);
      relationshipVersionmodel.setContextId(contextModel.getContextId());
      relationshipVersionmodel.setTimeRange(timeRange);
      relationshipVersions.add(relationshipVersionmodel);
    }
    contentRelationshipInstanceModel.setAddedElements(relationshipVersions);
  }
  
  /**
   * @param contextTags
   * @param contextModel
   * @param tagsMap
   */
  private void prepareContextTags(List<IContentTagInstance> contextTags,
      IDiContextModel contextModel, Map<String, ITag> tagsMap)
  {
    Map<String, List<String>> tagsToCreate = contextModel.getTags();
    if (!CollectionUtils.isEmpty(tagsToCreate)) {
      for (String tagId : tagsToCreate.keySet()) {
        IContentTagInstance newTag = new TagInstance();
        List<ITagInstanceValue> tagValues = new ArrayList<>();
        newTag.setTagId(tagId);
        newTag.setBaseType(Constants.TAG_INSTANCE_PROPERTY_TYPE);
        newTag.setId(UUID.randomUUID()
            .toString());
        updateTagValues(tagValues, tagsToCreate.get(tagId), tagsMap.get(tagId)
            .getTagValuesSequence());
        newTag.setTagValues(tagValues);
        contextTags.add(newTag);
      }
    }
  }
  
  /**
   * @param tagValues
   * @param tagValuesToSave
   * @param tagChildren
   */
  public void updateTagValues(List<ITagInstanceValue> tagValues, List<String> tagValuesToSave,
      List<String> tagChildren)
  {
    for (String tagValue : tagChildren) {
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setId(UUID.randomUUID()
          .toString());
      tagInstanceValue.setTagId(tagValue);
      if (tagValuesToSave.contains(tagValue)) {
        tagInstanceValue.setRelevance(100);
      }
      else {
        tagInstanceValue.setRelevance(0);
      }
      tagInstanceValue.setTimestamp(Long.toString(System.currentTimeMillis()));
      tagValues.add(tagInstanceValue);
    }
  }
  
  /**
   * @param targetType
   * @param relationshipId
   * @param sideId
   * @return
   */
  private IContentRelationshipInstanceModel getContentRelationshipInstanceModel(String targetType,
      String relationshipId, String sideId)
  {
    IContentRelationshipInstanceModel contentRelationshipInstanceModel = new ContentRelationshipInstanceModel();
    contentRelationshipInstanceModel.setRelationshipId(relationshipId);
    contentRelationshipInstanceModel.setBaseType(DiUtils.getBaseTypeByKlassType(targetType));
    contentRelationshipInstanceModel.setSideId(sideId);
    return contentRelationshipInstanceModel;
  }
  
  /**
   * @param isNature
   * @param saveRelationshipInstanceModel
   * @return
   */
  private List<IContentRelationshipInstanceModel> getModifiedRelationshipType(boolean isNature,
      ISaveRelationshipInstanceModel saveRelationshipInstanceModel)
  {
    return isNature ? saveRelationshipInstanceModel.getModifiedNatureRelationships()
        : saveRelationshipInstanceModel.getModifiedRelationships();
  }
  
  /**
   * @param contextModels
   * @param configDetails
   * @param relationshipInstance
   * @param isNature
   * @param baseType
   * @param contentRelationshipInstanceModel
   * @throws Exception
   */
  private void updatedRelationshipInstances(List<IRelationshipContextModel> contextModels,
      IGetConfigDetailsForCustomTabModel configDetails,
      IKlassInstanceRelationshipInstance relationshipInstance, boolean isNature, String baseType,
      IContentRelationshipInstanceModel contentRelationshipInstanceModel) throws Exception
  {
    if (contextModels.isEmpty()) {
      return;
    }
    Map<String, List<IContentTagInstance>> elementTagMappings = relationshipInstance
        .getElementTagMapping();
    Map<String, IInstanceTimeRange> elementTimeRangeMapping = relationshipInstance
        .getElementTimeRangeMapping();
    List<IRelationshipVersion> modifiedElements = new ArrayList<>();
    for (IRelationshipContextModel contextSideModel : contextModels) {
      String side2 = contextSideModel.getSide2Id();
      IDiContextModel contextModel = contextSideModel.getContext();
      if (contextModel != null) {
        getContextTagInfo(configDetails.getReferencedTags(), contextModel.getTags());
        if (!elementTagMappings.isEmpty()) {
          updateContextTagsAndTimeRange(side2, contextModel, elementTagMappings,
              elementTimeRangeMapping, modifiedElements);
          contentRelationshipInstanceModel.setModifiedElements(modifiedElements);
        }
      }
    }
  }
  
  /**
   * This method updates the tag values.
   * 
   * @param oppositeInstanceId
   * @param contextModel
   * @param elementTagMappings
   * @param elementTimeRangeMapping
   * @param modifiedElements
   * @throws Exception
   */
  private void updateContextTagsAndTimeRange(String oppositeInstanceId,
      IDiContextModel contextModel, Map<String, List<IContentTagInstance>> elementTagMappings,
      Map<String, IInstanceTimeRange> elementTimeRangeMapping,
      List<IRelationshipVersion> modifiedElements) throws Exception
  {
    IInstanceTimeRange timeRange = new InstanceTimeRange();
    if (!elementTimeRangeMapping.isEmpty()) {
      timeRange = getUpdatedTimeRange(oppositeInstanceId, contextModel.getTimeRange(),
          elementTimeRangeMapping);
    }
    List<IContentTagInstance> elementTags = elementTagMappings.get(oppositeInstanceId);
    Map<String, List<String>> updateTagsMap = contextModel.getTags();
    if (!updateTagsMap.isEmpty()) {
      for (IContentTagInstance tagInstance : elementTags) {
        if (updateTagsMap.containsKey(tagInstance.getTagId())) {
          List<String> updatedValueList = updateTagsMap.get(tagInstance.getTagId());
          List<ITagInstanceValue> tagValues = tagInstance.getTagValues();
          tagValues = updateTagValues(updatedValueList, tagValues);
          tagInstance.setTagValues(tagValues);
        }
      }
    }
    IRelationshipVersion element = new RelationshipVersion();
    element.setContextId(contextModel.getContextId());
    element.setTags(elementTags);
    element.setTimeRange(timeRange);
    element.setId(oppositeInstanceId);
    modifiedElements.add(element);
  }
  
  /**
   * This method retrieves updated time range
   * 
   * @param oppositeInstanceId
   * @param contexTimeRange
   * @param elementTimeRangeMapping
   * @return
   * @throws Exception
   */
  private IInstanceTimeRange getUpdatedTimeRange(String oppositeInstanceId,
      IDiTimeRangeModel contexTimeRange, Map<String, IInstanceTimeRange> elementTimeRangeMapping)
      throws Exception
  {
    IInstanceTimeRange timeRange = new InstanceTimeRange();
    timeRange = elementTimeRangeMapping.get(oppositeInstanceId);
    if (timeRange.getFrom() != null && timeRange.getTo() != null
        && !isBlank(contexTimeRange.getFrom())
        && !isBlank(contexTimeRange.getTo())) {
      prepareTimeRangeData(timeRange, contexTimeRange);
    }
    return timeRange;
  }
  
  /**
   * This method updates Tag values if any.
   * 
   * @param updatedValueList
   * @param tagValues
   * @return
   * @throws RDBMSException
   */
  private List<ITagInstanceValue> updateTagValues(List<String> updatedValueList,
      List<ITagInstanceValue> tagValues) throws RDBMSException
  {
    
    List<ITagInstanceValue> returnList = new ArrayList<ITagInstanceValue>(tagValues);
    for (ITagInstanceValue tagInstanceValue : tagValues) {
      if (updatedValueList.contains(tagInstanceValue.getCode())) {
        if (tagInstanceValue.getRelevance() != 100) {
          tagInstanceValue.setRelevance(100);
          tagInstanceValue.setTimestamp(Long.toString(System.currentTimeMillis()));
        }
      }
      else {
        if (tagInstanceValue.getRelevance() != 0) {
          tagInstanceValue.setRelevance(0);
          tagInstanceValue.setTimestamp(Long.toString(System.currentTimeMillis()));
          
          for (String updatedTagValue : updatedValueList) {
            ITagInstanceValue tagInstanceValueupdated = new TagInstanceValue();
            
            tagInstanceValueupdated.setRelevance(100);
            tagInstanceValueupdated.setTagId(updatedTagValue);
            tagInstanceValueupdated.setCode(updatedTagValue);
            tagInstanceValueupdated
                .setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
            tagInstanceValueupdated.setTimestamp(Long.toString(System.currentTimeMillis()));
            returnList.add(tagInstanceValueupdated);
          }
        }
      }
    }
    return returnList;
  }
  
  /**
   * collect existing opposite instance ids.
   * 
   * @param linkedInstances
   * @return
   */
  private List<String> collectExistingOppositeInstanceIds(
      List<IKlassInstanceInformationModel> linkedInstances)
  {
    List<String> oppositeInstanceIds = new ArrayList<>();
    if (!CollectionUtils.isEmpty(linkedInstances)) {
      linkedInstances.forEach(linkedInstance -> oppositeInstanceIds.add(linkedInstance.getId()));
    }
    return oppositeInstanceIds;
  }
  
  /**
   * @param configDetails
   * @param relationshipCode
   * @return
   */
  protected Boolean isNatureRelationship(IGetConfigDetailsForCustomTabModel configDetails,
      String relationshipCode)
  {
    return configDetails.getReferencedNatureRelationships()
        .values()
        .stream()
        .filter(v -> v.getCode()
            .equals(relationshipCode))
        .findAny()
        .isPresent();
  }
  
  /**
   * @param relationships
   * @param relationshipId
   * @return
   */
  private IKlassInstanceRelationshipInstance getRelationshipInstance(
      List<IKlassInstanceRelationshipInstance> relationships, String relationshipId)
  {
    for (IKlassInstanceRelationshipInstance contentRelationship : relationships) {
      if (contentRelationship.getRelationshipId()
          .equals(relationshipId)) {
        return contentRelationship;
      }
    }
    return null;
  }
  
  /**
   * @param referencedTags
   * @param tags
   */
  private void getContextTagInfo(Map<String, ITag> referencedTags, Map<String, List<String>> tags)
  {
    for (ITag referencedTagModel : referencedTags.values()) {
      String code = referencedTagModel.getCode();
      if (tags.containsKey(code)) {
        List<String> tagValueIds = getTagValuesIdsFromCode(tags.get(code),
            (List<ITag>) referencedTagModel.getChildren());
        tags.put(referencedTagModel.getId(), tagValueIds);
        /*  if (!code.equals(referencedTagModel.getId())) {
          tags.remove(code);
        }*/
      }
    }
  }
  
  /**
   * @param tagValueCodes
   * @param children
   * @return
   */
  private List<String> getTagValuesIdsFromCode(List<String> tagValueCodes, List<ITag> children)
  {
    return children.stream()
        .filter(v -> tagValueCodes.contains(v.getCode()))
        .map(v -> v.getId())
        .collect(Collectors.toList());
  }
  
  /**
   * @param timeRange
   * @param contexTimeRange
   */
  private void prepareTimeRangeData(IInstanceTimeRange timeRange, IDiTimeRangeModel contexTimeRange)
  {
    timeRange.setFrom(DiUtils.getLongValueOfDateString(contexTimeRange.getFrom()));
    timeRange.setTo(DiUtils.getLongValueOfDateString(contexTimeRange.getTo()));
  }
  
  /**
   * @param sourceType
   * @param saveRelationshipInstanceModel
   * @return
   * @throws Exception
   */
  private IGetKlassInstanceModel saveRelationshipInstance(String sourceType,
      ISaveRelationshipInstanceModel saveRelationshipInstanceModel) throws Exception
  {
    switch (sourceType) {
      case DiConstants.SOURCE_TYPE_PRODUCT:
        return saveArticleInstanceRelationships.execute(saveRelationshipInstanceModel);
      case DiConstants.SOURCE_TYPE_ASSET:
        return saveAssetInstanceRelationships.execute(saveRelationshipInstanceModel);
      default:
        return null;
    }
  }

  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String action = (String) inputFields.get(ACTION);
  
    if (!DiValidationUtil.isBlank(action)) {
      String side1iid = (String) inputFields.get(SIDE_1_IID);
      String relationshipCode = (String) inputFields.get(RELATIONSHIP_CODE);
      String sourceType = (String) inputFields.get(SOURCE_TYPE);
    
      if (DiValidationUtil.isBlank(side1iid)) {
        returnList.add(SIDE_1_IID);
      }
      if (DiValidationUtil.isBlank(relationshipCode)) {
        returnList.add(RELATIONSHIP_CODE);
      }
      if (DiValidationUtil.isBlank(sourceType)) {
        returnList.add(SOURCE_TYPE);
      }
    
      if (EntityAction.UPSERT.toString().equals(action)) {
        String context = (String) inputFields.get(CONTEXT);
        if (DiValidationUtil.isBlank(context)) {
          returnList.add(CONTEXT);
        }
      }
    }
    else {
      returnList.add(ACTION);
    }
  
    return returnList;
  }

  private static boolean isBlank(String ptext) {
    return ptext == null || ptext.trim().length() == 0;
  }
}
