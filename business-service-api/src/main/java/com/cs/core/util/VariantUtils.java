package com.cs.core.util;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedUniqueSelectorModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.exception.variants.InvalidDefaultTimeRangeException;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.variants.*;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class VariantUtils {
  
  @Autowired
  KlassInstanceUtils klassInstanceUtils;
 
  public static void manageRowsPermissions(
      IConfigDetailsForGetVariantInstancesInTableViewModel configDetails,
      IGetVariantInstancesInTableViewModel returnModel, String contextId)
  {
    Set<String> visiblePropertyIds = configDetails.getVisiblePropertyIds();
    List<String> contextualPropertyIds = getContextualPropertyIds(configDetails, contextId);
    
    removeColumnsNotHavingRP(returnModel, visiblePropertyIds, contextualPropertyIds);
    removeRowPropertiesNotHavingRP(returnModel, visiblePropertyIds, contextualPropertyIds);
  }
  
  public static void skipAttributesHavingAttributeContexts(
      IGetVariantInstancesInTableViewModel returnModel,
      Map<String, IReferencedSectionElementModel> referencedElements)
  {
    List<String> attributesToSkip = new ArrayList<>();
    for (Entry<String, IReferencedSectionElementModel> entry : referencedElements.entrySet()) {
      String contextId = entry.getValue()
          .getAttributeVariantContext();
      if (contextId != null) {
        attributesToSkip.add(entry.getKey());
      }
    }
    List<IIdParameterModel> columns = returnModel.getColumns();
    List<IIdParameterModel> columnsToSkip = new ArrayList<>();
    for (IIdParameterModel column : columns) {
      if (attributesToSkip.contains(column.getId())) {
        columnsToSkip.add(column);
      }
    }
    columns.removeAll(columnsToSkip);
  }
  
  public static List<String> getContextualPropertyIds(
      IConfigDetailsForGetVariantInstancesInTableViewModel configDetails, String contextId)
  {
    IReferencedVariantContextModel referencedVariantContext = configDetails
        .getReferencedVariantContexts()
        .getEmbeddedVariantContexts()
        .get(contextId);
    List<IReferencedVariantContextTagsModel> tags = referencedVariantContext.getTags();
    List<String> contextTagIds = new ArrayList<>();
    for (IReferencedVariantContextTagsModel tag : tags) {
      String tagId = tag.getTagId();
      contextTagIds.add(tagId);
    }
    List<String> propertiesToIgnoreForPermission = new ArrayList<>();
    propertiesToIgnoreForPermission.addAll(contextTagIds);
    propertiesToIgnoreForPermission.add(IInstanceTimeRange.FROM);
    propertiesToIgnoreForPermission.add(IInstanceTimeRange.TO);
    propertiesToIgnoreForPermission.addAll(CommonConstants.KLASS_TYPES);
    return propertiesToIgnoreForPermission;
  }
  
  private static void removeColumnsNotHavingRP(IGetVariantInstancesInTableViewModel returnModel,
      Set<String> visiblePropertyIds, List<String> contextualPropertyIds)
  {
    List<IIdParameterModel> columnsToRemove = new ArrayList<>();
    List<IIdParameterModel> columns = returnModel.getColumns();
    for (IIdParameterModel column : columns) {
      if (contextualPropertyIds.contains(column.getId())) {
        continue;
      }
      if (!visiblePropertyIds.contains(column.getId())) {
        columnsToRemove.add(column);
      }
    }
    columns.removeAll(columnsToRemove);
  }
  
  public static void removeRowPropertiesNotHavingRP(
      IGetVariantInstancesInTableViewModel returnModel, Set<String> visiblePropertyIds,
      List<String> contextualPropertyIds)
  {
    Set<String> propertiesToRemove = new HashSet<>();
    List<IRowIdParameterModel> rows = returnModel.getRows();
    for (IRowIdParameterModel row : rows) {
      Set<String> propertyIds = row.getProperties()
          .keySet();
      for (String propertyId : propertyIds) {
        if (propertyId.equals(IStandardConfig.StandardProperty.nameattribute.toString())
            || contextualPropertyIds.contains(propertyId)) {
          continue;
        }
        
        if (!visiblePropertyIds.contains(propertyId)) {
          propertiesToRemove.add(propertyId);
        }
      }
    }
    
    Set<IRowIdParameterModel> rowsToRemove = new HashSet<>();
    for (int index = 0; index < rows.size(); index++) {
      IRowIdParameterModel row = rows.get(index);
      Map<String, IPropertyInstance> properties = row.getProperties();
      properties.keySet()
          .removeAll(propertiesToRemove);
      if (properties.keySet()
          .size() == 0) {
        rowsToRemove.add(row);
      }
    }
    
    rows.removeAll(rowsToRemove);
  }
  
  public static IInstanceTimeRange getDefaultTimeRange(
      IReferencedVariantContextModel referencedContextMap) throws Exception
  {
    IInstanceTimeRange timeRange = new InstanceTimeRange();
    IDefaultTimeRange defaultTimeRange = referencedContextMap.getDefaultTimeRange();
    Boolean isTimeEnabled = referencedContextMap.getIsTimeEnabled();
    if (!isTimeEnabled || defaultTimeRange == null) {
      return timeRange;
    }
    Boolean isCurrentTime = defaultTimeRange.getIsCurrentTime();
    Long from = (isCurrentTime != null && isCurrentTime) ? getCurrentDate()
        : defaultTimeRange.getFrom();
    Long to = defaultTimeRange.getTo();
    if (from != null && to != null && from > to) {
      throw new InvalidDefaultTimeRangeException();
    }
    timeRange.setFrom(from);
    timeRange.setTo(to);
    return timeRange;
  }
  
  private static List<String> getContextualTagIds(
      IReferencedVariantContextModel variantContextModel)
  {
    List<String> contextTagIds = new ArrayList<String>();
    List<IReferencedVariantContextTagsModel> tags = variantContextModel.getTags();
    for (IReferencedVariantContextTagsModel tag : tags) {
      contextTagIds.add(tag.getTagId());
    }
    return contextTagIds;
  }
  
  private static Long getCurrentDate()
  {
    Calendar cal = Calendar.getInstance(); // locale-specific
    cal.setTime(new Date(System.currentTimeMillis()));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    Long time = cal.getTimeInMillis();
    return time;
  }
  
  public static List<String> getContextTagsIdsAndIntoKlassInstanceTags(IKlassInstance klassInstance,
      List<IContentTagInstance> contextTags) throws JsonProcessingException
  {
    IContextInstance context = ((IContentInstance) klassInstance).getContext();
    List<String> contextTagInstanceIds = new ArrayList<>();
    contextTags.forEach(contextTag -> {
      ((ITagInstance) contextTag).setContextInstanceId(context.getId());
      contextTag.setVariantInstanceId(klassInstance.getId());
      contextTag.setKlassInstanceId(((IContentInstance) klassInstance).getKlassInstanceId());
      contextTagInstanceIds.add(contextTag.getId());
    });
    context.setTagInstanceIds(contextTagInstanceIds);
    
    return contextTagInstanceIds;
  }
  
  public ITagInstance createNewTagInstance(String typeKlassId, ISectionTag sectionTag,
      ITag masterTag) throws Exception
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
    tagInstance.setTagId(masterTag.getId());
    if (sectionTag.getCouplingType()
        .equals(CommonConstants.DYNAMIC_COUPLED)
        || (sectionTag.getDefaultValue() != null && !sectionTag.getDefaultValue()
            .isEmpty())) {
      ITagConflictingValue conflictingValue = new TagConflictingValue();
      conflictingValue.setCouplingType(sectionTag.getCouplingType());
      conflictingValue.setTagValues(sectionTag.getDefaultValue());
      IKlassConflictingValueSource conflictingValueSource = new KlassConflictingValueSource();
      conflictingValueSource.setId(typeKlassId);
      conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
      conflictingValue.setSource(conflictingValueSource);
      List<ITagConflictingValue> listOfConflictingValues = tagInstance.getConflictingValues();
      if (listOfConflictingValues == null) {
        listOfConflictingValues = new ArrayList<>();
      }
      listOfConflictingValues.add(conflictingValue);
    }
    tagInstance.setLastModified(System.currentTimeMillis());
    klassInstanceUtils.addTagsInContentBasedOnTagType(masterTag, tagInstance, sectionTag);
    return tagInstance;
  }
  
  public void createContextInstance(IGetConfigDetailsForCustomTabModel configDetails,
      ICreateVariantRequestNewStrategyModel createVariantRequestStrategyModel,
      ICreateVariantModel createVariantModel) throws Exception
  {
    IReferencedContextModel referencedVariantContexts = configDetails
        .getReferencedVariantContexts();
    Map<String, IReferencedVariantContextModel> embeddedVariantContexts = referencedVariantContexts
        .getEmbeddedVariantContexts();
    
    String contextId = createVariantModel.getContextId();
    IReferencedVariantContextModel referencedContextMap = embeddedVariantContexts.get(contextId);
    Boolean isDuplicateVariantAllowed = referencedContextMap.getIsDuplicateVariantAllowed();
    createVariantRequestStrategyModel.setIsDuplicateVariantAllowed(isDuplicateVariantAllowed);
    
    IKlassInstance klassInstance = createVariantRequestStrategyModel.getKlassInstance();
    createContextInstance(configDetails, referencedContextMap, klassInstance,
        createVariantModel.getTimeRange(), createVariantModel.getContextTags(),
        createVariantModel.getLinkedInstances());
  }
  
  public void createContextInstance(IGetConfigDetailsForCustomTabModel configDetails,
      IReferencedVariantContextModel referencedContextMap, IKlassInstance klassInstance,
      IInstanceTimeRange timeRange, List<IContentTagInstance> contextTags,
      List<IIdAndBaseType> linkedInstances) throws Exception
  {
    String contextId = referencedContextMap.getId();
    IContextInstance contextInstance = new ContextInstance();
    String newContextInstanceId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix());
    
    if (timeRange == null) {
      timeRange = getDefaultTimeRange(referencedContextMap);
    }
    contextInstance.setId(newContextInstanceId);
    contextInstance.setTimeRange(timeRange);
    contextInstance.setContextId(contextId);
    contextInstance.setLinkedInstances(linkedInstances);
    
    ((IContentInstance) klassInstance).setContext(contextInstance);
    
    // on auto create and and create of limited object varaints we already have
    // context tags
    if (contextTags.isEmpty()) {
      // collect all context tag ids for the correct context tags
      Stream<IReferencedVariantContextTagsModel> stream = referencedContextMap.getTags()
          .stream();
      Stream<String> referencedVariantContextMap = stream
          .map(IReferencedVariantContextTagsModel::getTagId);
      List<String> contextTagIds = referencedVariantContextMap.collect(Collectors.toList());
      
      // create context tag instances
      List<ITagInstance> tags = (List<ITagInstance>) klassInstance.getTags();
      Long currentTimeMillis = System.currentTimeMillis();
      Map<String, ITag> referencedTags = configDetails.getReferencedTags();
      for (String contextTagId : contextTagIds) {
        ITag referencedTag = referencedTags.get(contextTagId);
        if (referencedTag == null) {
          continue;
        }
        
        ITagInstance tagInstance = klassInstanceUtils.createTagInstanceOrRemoveIfExists(tags,
            referencedTag, currentTimeMillis);
        if (tagInstance == null) {
          continue;
        }
        tags.add(tagInstance);
      }
      // filter context tagInstances
      Stream<IContentTagInstance> tagInstanceStream = (Stream<IContentTagInstance>) klassInstance
          .getTags()
          .stream();
      Stream<IContentTagInstance> filter = tagInstanceStream
          .filter(tagInstance -> contextTagIds.contains(tagInstance.getTagId()));
      List<IContentTagInstance> contextTagInstances = filter.collect(Collectors.toList());
      
      // add to create model for next method uses this model to set context
      // Instance Id in tag
      // instance and tag instance ids in context object
      contextTags.addAll(contextTagInstances);
    }
  }
  
  public String getLabel(String instanceName, List<IContentTagInstance> contextualTagInstances,
      Map<String, ITag> tagValueMap)
  {
    String label = instanceName;
    int count = 0;
    for (IContentTagInstance contentTagInstance : contextualTagInstances) {
      List<ITagInstanceValue> tagValues = contentTagInstance.getTagValues();
      for (ITagInstanceValue tagValue : tagValues) {
        if (tagValue.getRelevance() == 0) {
          continue;
        }
        if (count == 2) {
          break;
        }
        ITag tag = tagValueMap.get(tagValue.getTagId());
        String tagValueLabel = tag.getLabel();
        label = label + "_" + tagValueLabel;
        count++;
      }
      if (count == 2) {
        break;
      }
    }
    return label;
  }
  
  public List<IContentTagInstance> getContextualTagInstancesFromUniqueSelector(
      IReferencedUniqueSelectorModel uniqueSelector) throws Exception
  {
    List<IContentTagInstance> contextualTagInstances = new ArrayList<>();
    List<IReferencedVariantContextTagsModel> uniqueSelectorTags = uniqueSelector.getTags();
    for (IReferencedVariantContextTagsModel uniqueSelectorTag : uniqueSelectorTags) {
      String tagId = uniqueSelectorTag.getTagId();
      
      List<ITagInstanceValue> tagValues = new ArrayList<>();
      List<String> tagValueIds = uniqueSelectorTag.getTagValueIds();
      for (String tagValueId : tagValueIds) {
        ITagInstanceValue newTagValue = new TagInstanceValue();
        newTagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
        newTagValue.setTagId(tagValueId);
        newTagValue.setRelevance(100);
        tagValues.add(newTagValue);
      }
      
      IContentTagInstance contxtualTagInstance = new TagInstance();
      contxtualTagInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      contxtualTagInstance.setBaseType(Constants.TAG_INSTANCE_PROPERTY_TYPE);
      contxtualTagInstance.setTagId(tagId);
      contxtualTagInstance.setTagValues(tagValues);
      contextualTagInstances.add(contxtualTagInstance);
    }
    return contextualTagInstances;
  }
}
