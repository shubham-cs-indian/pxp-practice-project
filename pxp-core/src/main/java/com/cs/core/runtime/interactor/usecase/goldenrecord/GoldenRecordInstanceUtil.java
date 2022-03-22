package com.cs.core.runtime.interactor.usecase.goldenrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.CreatedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.CreatedOnAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.LastModifiedByAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.NameAttribute;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingValueSource;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.usecase.templating.IGetArticleInstanceForCustomTabStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Component
public class GoldenRecordInstanceUtil {
  
  @Autowired
  protected KlassInstanceUtils                      klassInstanceUtils;
  
  @Autowired
  protected IGetArticleInstanceForCustomTabStrategy getArticleInstanceForCustomTabStrategy;
  
  @Autowired
  protected PermissionUtils                         permissionUtils;
  
  @Autowired
  protected ISessionContext                         context;
  
  public List<IContentAttributeInstance> prepareAttributeInstances(List<String> attributeIds,
      Map<String, IAttribute> referencedAttributes,
      Map<String, IReferencedSectionElementModel> referencedElement, String klassId) throws Exception
  {
    List<IContentAttributeInstance> returnList = new ArrayList<>();
    for (String attributeId : attributeIds) {
      IAttribute attribute = referencedAttributes.get(attributeId);
      IReferencedSectionAttributeModel element = (IReferencedSectionAttributeModel) referencedElement
          .get(attributeId);
      String defaultValue = element.getDefaultValue();
      String couplingType = element.getCouplingType();
      IContentAttributeInstance attributeInstance = klassInstanceUtils.createAttributeInstance(
          klassId, null, attribute, defaultValue, couplingType,
          CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE, element.getValueAsHtml());
      
      returnList.add(attributeInstance);
    }
    return returnList;
  }
  
  public List<ITagInstance> prepareTagsInstances(List<String> tagIds,
      Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedElement, String klassId)
  {
    List<ITagInstance> returnList = new ArrayList<>();
    for (String tagId : tagIds) {
      ITag tag = referencedTags.get(tagId);
      IReferencedSectionTagModel element = (IReferencedSectionTagModel) referencedElement
          .get(tagId);
      
      ITagInstance tagInstance = klassInstanceUtils.createTagInstance(klassId, element, tag);
      returnList.add(tagInstance);
    }
    return returnList;
  }
  
  public IGetKlassInstanceModel prepareDataForResponse(String klassInstanceId,
      IGetConfigDetailsModel configDetails) throws Exception
  {
    Thread.sleep(1000);
    
    IGetInstanceRequestStrategyModel getInstanceRequestStrategymodel = KlassInstanceUtils
        .getInstanceRequestStrategyModel(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE,
            klassInstanceId, null, configDetails, 0, 20, new ArrayList<>());
    
    getInstanceRequestStrategymodel.setReferencedTaskIds(configDetails.getReferencedTasks()
        .keySet());
    getInstanceRequestStrategymodel.setPersonalTaskIds(configDetails.getPersonalTaskIds());
    
    IReferencedTemplatePermissionModel referencedPermissionModel = configDetails
        .getReferencedPermissions();
    getInstanceRequestStrategymodel.setTaskIdsForRolesHavingReadPermission(
        referencedPermissionModel.getTaskIdsForRolesHavingReadPermission());
    getInstanceRequestStrategymodel.setTaskIdsHavingReadPermissions(
        referencedPermissionModel.getTaskIdsHavingReadPermissions());
    getInstanceRequestStrategymodel
        .setKlassIdsHavingRP(referencedPermissionModel.getKlassIdsHavingRP());
    getInstanceRequestStrategymodel.setEntities(referencedPermissionModel.getEntitiesHavingRP());
    ((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel)
        .setReferencedElements(configDetails.getReferencedElements());
    
    IGetKlassInstanceCustomTabModel returnModel = getArticleInstanceForCustomTabStrategy
        .execute((IGetInstanceRequestStrategyModelForCustomTab) getInstanceRequestStrategymodel);
    
    returnModel.setConfigDetails(configDetails);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    
    /* //**
          @author Kshitij
    
          Temporary implementation for showing overridden Calculated Attribute Unit in Normalization
         //*
    */ klassInstanceUtils.replaceCalculatedAttributeUnitInReferencedCustomCalculatedAttributes(
        returnModel.getKlassInstance()
            .getRuleViolation(),
        configDetails.getReferencedAttributes());
    
    klassInstanceUtils.removeTagValuesWithZeroRelevanceFromConfigDetails(returnModel,
        configDetails);
    
    return returnModel;
  }
  
  @SuppressWarnings("unchecked")
  public void removeTagValuesWithZeroRelevanceFromConfigDetails(IGetKlassInstanceModel returnModel,
      IGetConfigDetailsModel configDetails)
  {
    Set<String> tagGroupIdsToIgnore = new HashSet<>();
    IReferencedContextModel referencedVariantContexts = configDetails
        .getReferencedVariantContexts();
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getEmbeddedVariantContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getLanguageVariantContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getProductVariantContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getPromotionalVersionContexts()
            .values());
    fillTagGroupIdsToIgnore(tagGroupIdsToIgnore,
        referencedVariantContexts.getRelationshipVariantContexts()
            .values());
    
    if (configDetails instanceof IGetConfigDetailsForCustomTabModel) {
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = ((IGetConfigDetailsForCustomTabModel) configDetails)
          .getReferencedNatureRelationships();
      for (IGetReferencedNatureRelationshipModel natureRelationship : referencedNatureRelationships
          .values()) {
        List<String> contextTags = natureRelationship.getContextTags();
        if (contextTags != null && !contextTags.isEmpty()) {
          tagGroupIdsToIgnore.addAll(contextTags);
        }
      }
    }
    
    Map<String, Set<String>> tagIdsVsTagValueIdsToRetainMap = new HashMap<>();
    
    List<? extends IContentTagInstance> tagInstances = returnModel.getKlassInstance()
        .getTags();
    for (IContentTagInstance tagInstance : tagInstances) {
      Set<String> tagValueIdsToRetain = new HashSet<>();
      List<ITagInstanceValue> tagValues = tagInstance.getTagValues();
      for (ITagInstanceValue tagValue : tagValues) {
        if (!tagValue.getRelevance()
            .equals(0)) {
          tagValueIdsToRetain.add(tagValue.getTagId());
        }
      }
      List<ITagConflictingValue> conflictingValues = tagInstance.getConflictingValues();
      for (ITagConflictingValue conflictingValue : conflictingValues) {
        List<IIdRelevance> conflictingTagValues = conflictingValue.getTagValues();
        for (IIdRelevance conflictingTagValue : conflictingTagValues) {
          tagValueIdsToRetain.add(conflictingTagValue.getTagId());
        }
      }
      tagIdsVsTagValueIdsToRetainMap.put(tagInstance.getTagId(), tagValueIdsToRetain);
    }
    
    // Only filter tag values for types mentioned in list below
    List<String> tagTypes = Arrays.asList(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
        SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.RANGE_TAG_TYPE_ID);
    
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    List<String> referencedTagsToRemove = new ArrayList<>();
    Set<String> tagsToIterate = new HashSet<>(referencedTags.keySet());
    tagsToIterate.removeAll(tagGroupIdsToIgnore);
    for (String tagGroupId : tagsToIterate) {
      
      ITag referencedTag = referencedTags.get(tagGroupId);
      if (!tagTypes.contains(referencedTag.getTagType())) {
        continue;
      }
      
      Set<String> tagValuesToRetainList = tagIdsVsTagValueIdsToRetainMap.get(tagGroupId);
      if (tagValuesToRetainList == null) {
        referencedTagsToRemove.add(tagGroupId);
        continue;
      }
      
      List<ITag> referencedTagValues = (List<ITag>) referencedTag.getChildren();
      List<ITag> referencedTagValuesToRemove = new ArrayList<>();
      for (ITag referencedTagValue : referencedTagValues) {
        if (!tagValuesToRetainList.contains(referencedTagValue.getId())) {
          referencedTagValuesToRemove.add(referencedTagValue);
        }
      }
      referencedTagValues.removeAll(referencedTagValuesToRemove);
    }
    
    /*for (String tagId : referencedTagsToRemove) {
      referencedTags.remove(tagId);
    }*/
    
  }
  
  private void fillTagGroupIdsToIgnore(Set<String> tagGroupIdsToIgnore,
      Collection<IReferencedVariantContextModel> values)
  {
    for (IReferencedVariantContextModel contextModel : values) {
      List<IReferencedVariantContextTagsModel> tags = contextModel.getTags();
      for (IReferencedVariantContextTagsModel tag : tags) {
        tagGroupIdsToIgnore.add(tag.getTagId());
      }
    }
  }
  
  public Map<String, List<String>> getTagIdTagValueIdsMap(List<ITagInstance> tags)
  {
    Map<String, List<String>> tagIdTagValueIdsMap = new HashMap<>();
    
    for (ITagInstance tag : tags) {
      List<ITagInstanceValue> tagValues = (List<ITagInstanceValue>) tag.getTagValues();
      List<String> tagValueIds = new ArrayList<>();
      for (ITagInstanceValue tagValue : tagValues) {
        Integer relevance = (Integer) tagValue.getRelevance();
        if (!relevance.equals(0)) {
          tagValueIds.add(tagValue.getTagId());
        }
      }
      
      List<ITagConflictingValue> conflictingValues = tag.getConflictingValues();
      for (ITagConflictingValue conflictValue : conflictingValues) {
        List<IIdRelevance> conflictingTagValues = conflictValue.getTagValues();
        for (IIdRelevance conflictingTagValue : conflictingTagValues) {
          tagValueIds.add((String) conflictingTagValue.getTagId());
        }
      }
      
      String tagId = tag.getTagId();
      tagIdTagValueIdsMap.put(tagId, tagValueIds);
    }
    
    return tagIdTagValueIdsMap;
  }
  
  public IAttributeInstance createAttributeInstance(String klassInstanceId, String typeId,
      String entityName, IAttribute referencedAttribute, String defaultValue, String couplingType,
      String conflictingType, IAttributeInstance newAttributeInstance) throws RDBMSException, Exception
  {
    if (newAttributeInstance == null) {
      newAttributeInstance = new AttributeInstance();
      String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix());
      newAttributeInstance.setId(id);
      newAttributeInstance.setAttributeId(referencedAttribute.getId());
      newAttributeInstance.setConflictingValues(new ArrayList<>());
      newAttributeInstance.setLastModified(System.currentTimeMillis());
    }
    
    if (referencedAttribute instanceof CreatedOnAttribute) {
      defaultValue = String.valueOf(System.currentTimeMillis());
      newAttributeInstance.setValue(defaultValue);
    }
    else if (referencedAttribute instanceof LastModifiedAttribute) {
      defaultValue = String.valueOf(System.currentTimeMillis());
      newAttributeInstance.setValue(defaultValue);
    }
    else if (referencedAttribute instanceof CreatedByAttribute) {
      defaultValue = context.getUserId();
      newAttributeInstance.setValue(defaultValue);
    }
    else if (referencedAttribute instanceof LastModifiedByAttribute) {
      defaultValue = context.getUserId();
      newAttributeInstance.setValue(defaultValue);
    }
    else if (couplingType != null && conflictingType != null) {
      IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
      conflictingValue.setValue(defaultValue);
      conflictingValue.setCouplingType(couplingType);
      IConflictingValueSource conflictingValueSource;
      if (conflictingType.equals(CommonConstants.ENTITY_KLASS_TYPE)) {
        conflictingValueSource = new KlassConflictingValueSource();
        conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
      }
      else {
        conflictingValueSource = new TaxonomyConflictingValueSource();
        conflictingValueSource.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
      }
      conflictingValueSource.setId(typeId);
      conflictingValue.setSource(conflictingValueSource);
      List<IAttributeConflictingValue> listOfConflictingValues = newAttributeInstance
          .getConflictingValues();
      listOfConflictingValues.add(conflictingValue);
    }
    else if (referencedAttribute instanceof NameAttribute) {
      newAttributeInstance.setValue(entityName);
    }
    newAttributeInstance.setKlassInstanceId(klassInstanceId);
    return newAttributeInstance;
  }
  
  public ITagInstance createTagInstance(String klassInstanceId, String typeKlassId,
      List<IIdRelevance> tagValues, String couplingType, ITag masterTag, String conflictingType,
      ITagInstance tagInstance) throws RDBMSException, Exception
  {
    if (tagInstance == null) {
      tagInstance = new TagInstance();
      String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix());
      tagInstance.setId(id);
      tagInstance.setTagId(masterTag.getId());
      tagInstance.setLastModified(System.currentTimeMillis());
      tagInstance.setConflictingValues(new ArrayList<>());
    }
    ITagConflictingValue conflictingValue = new TagConflictingValue();
    conflictingValue.setCouplingType(couplingType);
    conflictingValue.setTagValues(tagValues);
    IConflictingValueSource conflictingValueSource;
    if (conflictingType.equals(CommonConstants.ENTITY_KLASS_TYPE)) {
      conflictingValueSource = new KlassConflictingValueSource();
      conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
    }
    else {
      conflictingValueSource = new TaxonomyConflictingValueSource();
      conflictingValueSource.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
    }
    conflictingValueSource.setId(typeKlassId);
    conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
    conflictingValue.setSource(conflictingValueSource);
    List<ITagConflictingValue> listOfConflictingValues = tagInstance.getConflictingValues();
    listOfConflictingValues.add(conflictingValue);
    tagInstance.setKlassInstanceId(klassInstanceId);
    return tagInstance;
  }
  
  public Boolean isCalculatedOrConcatenatedAttribute(String type,
      Map<String, IAttribute> referencedAttributes, String entityId)
  {
    if (!type.equals(CommonConstants.ATTRIBUTE)) {
      return false;
    }
    else {
      IAttribute iAttribute = referencedAttributes.get(entityId);
      return iAttribute.getType()
          .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
          || (iAttribute.getType()
              .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE));
    }
  }
  
  public IAttributeInstance createCalculatedOrConcanatedAttribute(String klassInstanceId,
      String entityId) throws RDBMSException, Exception
  {
    Long timestamp = System.currentTimeMillis();
    IAttributeInstance newAttributeInstance = new AttributeInstance();
    newAttributeInstance.setAttributeId(entityId);
    String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix());
    newAttributeInstance.setId(id);
    newAttributeInstance.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
    newAttributeInstance.setCreatedBy(context.getUserId());
    newAttributeInstance.setLastModifiedBy(context.getUserId());
    newAttributeInstance.setCreatedOn(timestamp);
    newAttributeInstance.setLastModified(timestamp);
    newAttributeInstance.setKlassInstanceId(klassInstanceId);
    newAttributeInstance.setVersionId(Long.valueOf(0));
    
    return newAttributeInstance;
  }
}
