package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.model.klass.IAttributeDefaultValueCouplingTypeModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.ITagDefaultValueCouplingTypeModel;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.datarule.*;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.*;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyInstanceUtils {
  
  /* public void createPropertyInstances(IGetConfigDetailsForCustomTabModel configDetails,
      IKlassInstance klassInstance, String language, String currentUser) throws Exception
  {
    List<ITagInstance> tags = (List<ITagInstance>) klassInstance.getTags();
    String klassInstanceId = klassInstance.getId();
  
    Set<Entry<String, IReferencedSectionElementModel>> referencedElementEntrySet = configDetails.getReferencedElements().entrySet();
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    for (Entry<String, IReferencedSectionElementModel> referencedSectionElementMap: referencedElementEntrySet) {
      IReferencedSectionElementModel referencedSectionElement = referencedSectionElementMap.getValue();
      String referencedSectionElementId = referencedSectionElementMap.getKey();
  
      if (referencedSectionElement.getType().equals(CommonConstants.ATTRIBUTE)) {
        List<IContentAttributeInstance> attributes = (List<IContentAttributeInstance>) klassInstance.getAttributes();
        IAttribute referencedAttribute = referencedAttributes.get(referencedSectionElementId);
        IContentAttributeInstance attributeInstance = AttributeInstanceUtils.createNewAttributeInstance(klassInstance, referencedSectionElement, referencedAttribute, language, currentUser);
        if (attributeInstance != null) {
          attributes.add(attributeInstance);
        }
      } else if (referencedSectionElement.getType().equals(CommonConstants.TAG)) {
        ITag referencedTag = configDetails.getReferencedTags().get(referencedSectionElementId);
        ITagInstance tagInstance = TagInstanceUtils.createTagInstance(klassInstance, referencedTag, referencedSectionElement, currentUser);
        if (tagInstance != null) {
          tags.add(tagInstance);
        }
      }
    }
  
    checkAndAddMissingMandatoryAttributes(attributeIdsList, attributes,
        entityName, referencedKlassId, klassInstance, referencedAttributes);
  
    Collection<ITag> tags = configDetails.getReferencedTags().values();
    for (ITag tag : tags) {
      ITagInstance tagInstance = createTagInstanceOrRemoveIfExists(tags, tag);
      if (tagInstance == null) {
        continue;
      }
      tags.add(tagInstance);
    }
  
    //attributesList.addAll(attributesToBeMaintainedList);
    rolesList.addAll(rolesToBeMaintainedList);
    //tagsList.addAll(tagsToBeMaintainedList);
  }
  
  public void addAttributeInstancesToKlassInstance(String referencedKlassId, String entityName,
      IKlassInstance klassInstance, IAttribute referencedAttribute,
      IReferencedSectionElementModel sectionAttribute, String language, String currentUser)
  {
    List<IContentAttributeInstance> attributes = (List<IContentAttributeInstance>) klassInstance.getAttributes();
    IContentAttributeInstance attributeInstance = AttributeInstanceUtils.createNewAttributeInstance(
        klassInstance, sectionAttribute, referencedAttribute, language, currentUser);
    if(attributeInstance != null) {
      attributes.add(attributeInstance);
    }
  }*/

  private static void handleConflictsForTag(Map<String, List<IConflictingValue>> tagConflictsMap,
      IDefaultValueChangeModel defaultValueDiff, List<String> matchedTypes) throws RDBMSException
  {
    String entityId = defaultValueDiff.getEntityId();
    List<IConflictingValue> tagConflicts = tagConflictsMap.get(entityId);
    if (tagConflicts == null) {
      tagConflicts = new ArrayList<>();
      tagConflictsMap.put(entityId, tagConflicts);
    }
    tagConflicts.addAll(getTagConflictingEntriesForAllMatchedTypes(matchedTypes, defaultValueDiff));
  }
  
  private static void handleConflictsForAttribute(
      Map<String, List<IConflictingValue>> dependentAttributeIdConflictingValuesMap,
      Map<String, List<IConflictingValue>> attributeConflictsMap,
      IDefaultValueChangeModel defaultValueDiff, List<String> matchedTypes)
  {
    String entityId = defaultValueDiff.getEntityId();
    Boolean isDependent = ((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff)
        .getIsDependent();
    if (isDependent) {
      List<IConflictingValue> dependentAttributeConflicts = dependentAttributeIdConflictingValuesMap
          .get(entityId);
      if (dependentAttributeConflicts == null) {
        dependentAttributeConflicts = new ArrayList<>();
        dependentAttributeIdConflictingValuesMap.put(entityId, dependentAttributeConflicts);
      }
      dependentAttributeConflicts
          .addAll(getAttributeConflictingEntriesForAllMatchedTypes(matchedTypes, defaultValueDiff));
    }
    else {
      List<IConflictingValue> attributeConflicts = attributeConflictsMap.get(entityId);
      if (attributeConflicts == null) {
        attributeConflicts = new ArrayList<>();
        attributeConflictsMap.put(entityId, attributeConflicts);
      }
      attributeConflicts
          .addAll(getAttributeConflictingEntriesForAllMatchedTypes(matchedTypes, defaultValueDiff));
    }
  }
  
  /*  private static IConflictingValue getModelByType(String type)
  {
    if(type.equals(CommonConstants.ATTRIBUTE)) {
      return new AttributeConflictingValue();
    }
    else  {
      return new TagConflictingValue();
    }
  }*/
  
  private static IConflictingValueSource getConflictingSourceBySourceType(String sourceType)
  {
    IConflictingValueSource conflictingValuesource = null;
    if (sourceType.equals(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE)) {
      conflictingValuesource = new KlassConflictingValueSource();
      conflictingValuesource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
    }
    else if (sourceType.equals(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE)) {
      conflictingValuesource = new TaxonomyConflictingValueSource();
      conflictingValuesource.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
    }
    return conflictingValuesource;
  }
  
  private static List<String> getFilteredKlassAndChildrenIds(List<String> types,
      List<String> taxonomyIds, String sourceType, List<String> klassAndChildrenIds)
  {
    List<String> matchedTypes = new ArrayList<>();
    switch (sourceType) {
      case CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE:
        matchedTypes.addAll(types);
        matchedTypes.retainAll(klassAndChildrenIds);
        break;
      case CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE:
        matchedTypes.addAll(taxonomyIds);
        matchedTypes.retainAll(klassAndChildrenIds);
        break;
    }
    return matchedTypes;
  }
  
  private static List<IConflictingValue> getAttributeConflictingEntriesForAllMatchedTypes(
      List<String> klassOrTaxonomyIds, IDefaultValueChangeModel defaultValueDiff)
  {
    List<IConflictingValue> conflictingValueList = new ArrayList<>();
    String entityId = defaultValueDiff.getEntityId();
    Boolean isMandatory = defaultValueDiff.getIsMandatory();
    Boolean isShould = defaultValueDiff.getIsShould();
    String sourceType = defaultValueDiff.getSourceType();
    String couplingType = defaultValueDiff.getCouplingType();
    for (String matchedType : klassOrTaxonomyIds) {
      IAttributeConflictingValue newConflictingValue = new AttributeConflictingValue();
      newConflictingValue.setId(entityId);
      newConflictingValue.setIsMandatory(isMandatory);
      newConflictingValue.setIsShould(isShould);
      newConflictingValue.setCouplingType(couplingType);
      newConflictingValue.setSource(getConflictingSourceBySourceType(sourceType));
      newConflictingValue.getSource()
          .setId(matchedType);
      newConflictingValue
          .setValue(((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getValue());
      newConflictingValue.setValueAsHtml(
          ((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getValueAsHtml());
      conflictingValueList.add(newConflictingValue);
    }
    
    return conflictingValueList;
  }
  
  private static List<IConflictingValue> getTagConflictingEntriesForAllMatchedTypes(
      List<String> klassOrTaxonomyIds, IDefaultValueChangeModel defaultValueDiff) throws RDBMSException
  {
    List<IConflictingValue> conflictingValueList = new ArrayList<>();
    String entityId = defaultValueDiff.getEntityId();
    Boolean isMandatory = defaultValueDiff.getIsMandatory();
    Boolean isShould = defaultValueDiff.getIsShould();
    String sourceType = defaultValueDiff.getSourceType();
    String couplingType = defaultValueDiff.getCouplingType();
    for (String matchedType : klassOrTaxonomyIds) {
      ITagConflictingValue newConflictingValue = new TagConflictingValue();
      newConflictingValue.setId(entityId);
      newConflictingValue.setIsMandatory(isMandatory);
      newConflictingValue.setIsShould(isShould);
      newConflictingValue.setCouplingType(couplingType);
      newConflictingValue.setSource(getConflictingSourceBySourceType(sourceType));
      newConflictingValue.getSource()
          .setId(matchedType);
      List<IIdRelevance> tagValues = ((ITagDefaultValueCouplingTypeModel) defaultValueDiff)
          .getValue();
      for (IIdRelevance tagValue : tagValues) {
        tagValue.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
      }
      newConflictingValue.setTagValues(tagValues);
      conflictingValueList.add(newConflictingValue);
    }
    
    return conflictingValueList;
  }
  
  public static void fillAttributeInstances(IPropertyRecordDTO property, IBaseEntityDTO baseEntityDTO,
      List<IContentAttributeInstance> attributes)
      throws NumberFormatException, Exception
  {
    IAttributeInstance attributeInstance = new AttributeInstance();
    IValueRecordDTO existingValueRecord = (IValueRecordDTO) property;
    
    attributeInstance.setId(KlassInstanceBuilder.getAttributeID(existingValueRecord));
    attributeInstance.setKlassInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    attributeInstance.setBaseType(AttributeInstance.class.getName());
    attributeInstance.setLanguage(existingValueRecord.getLocaleID());
    attributeInstance.setValue(existingValueRecord.getValue());
    attributeInstance.setOriginalInstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    attributeInstance.setAttributeId(existingValueRecord.getProperty().getPropertyCode());
    attributeInstance.setCode(existingValueRecord.getProperty().getPropertyCode());
    
    attributeInstance.setValue(existingValueRecord.getValue());
    attributeInstance.setValueAsNumber(existingValueRecord.getAsNumber());
    attributeInstance.setValueAsHtml(existingValueRecord.getAsHTML());
    attributes.add(attributeInstance);
  }
  
  public static void fillTagInstances(IPropertyRecordDTO property, IBaseEntityDTO baseEntityDTO,
      List<ITagInstance> tags)
      throws NumberFormatException, Exception
  {
    ITagInstance tagInstance = new TagInstance();
    ITagsRecordDTO tagsRecordDTO = (ITagsRecordDTO) property;
    tagInstance.setId(tagsRecordDTO.getNodeID());
    tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier().getClassifierCode());
    tagInstance.setBaseType(TagInstance.class.getName());
    tagInstance.setTagId(tagsRecordDTO.getProperty().getPropertyCode());
    
    // Tag values
    List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
    tagsRecordDTO.getTags()
        .forEach(tagRecordDTO -> {
          ITagInstanceValue tagInstanceValue = new TagInstanceValue();
          tagInstanceValue.setTagId(tagRecordDTO.getTagValueCode());
          tagInstanceValue.setId(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
          tagInstanceValue.setCode(KlassInstanceBuilder.getTagValueID(tagRecordDTO));
          tagInstanceValue.setRelevance(tagRecordDTO.getRange());
          tagInstanceValues.add(tagInstanceValue);
        });
    tagInstance.setTagValues(tagInstanceValues);
    tags.add(tagInstance);
  }

  public static void fillContentDiffFromDefaultValueDiffAndGetDiffListForIsSkipped(
     IContentsPropertyDiffModel contentDiff, List<IDefaultValueChangeModel> diffList,
     List<String> types, List<String> taxonomyIds, List<String> languageCodes,
     List<IDefaultValueChangeModel> diffListForIsSkipped,
     List<IDefaultValueChangeModel> dependentDiffForIsSkipped) throws RDBMSException
 {
     Map<String, List<IConflictingValue>> dependentAttributeIdConflictingValuesMap = new HashMap<>();
     Map<String, List<IConflictingValue>> attributeConflictsMap = new HashMap<>();
     Map<String, List<IConflictingValue>> tagConflictsMap = new HashMap<>();
     for (IDefaultValueChangeModel defaultValueDiff : diffList) {
         String type = defaultValueDiff.getType();
         String sourceType = defaultValueDiff.getSourceType();
         if (defaultValueDiff.getIsSkipped()) {
         if (type.equals(Constants.ATTRIBUTE)
                     && ((IAttributeDefaultValueCouplingTypeModel) defaultValueDiff).getIsDependent()) {
               dependentDiffForIsSkipped.add(defaultValueDiff);
             }
         else {
               diffListForIsSkipped.add(defaultValueDiff);
             }
           }
       List<String> matchedTypes = getFilteredKlassAndChildrenIds(types, taxonomyIds, sourceType,defaultValueDiff.getKlassAndChildrenIds());
      if (type.equals(CommonConstants.ATTRIBUTE)) {
            handleConflictsForAttribute(dependentAttributeIdConflictingValuesMap, attributeConflictsMap, defaultValueDiff, matchedTypes);
          }

            else if (type.equals(CommonConstants.TAG)) {
            handleConflictsForTag(tagConflictsMap, defaultValueDiff, matchedTypes);
           }
       }

         Map<String, Map<String, List<IConflictingValue>>> langVsDependendentConflicts = new HashMap<>();
     for (String languageCode : languageCodes) {
         langVsDependendentConflicts.put(languageCode, dependentAttributeIdConflictingValuesMap);
       }

         contentDiff.setAttributesConflictMap(attributeConflictsMap);
      contentDiff.setTagsConflictMap(tagConflictsMap);
      contentDiff.setDependentAttributesConflictMap(langVsDependendentConflicts);
   }

  public static IConflictingValueSource getConflictingValueSourceBySourceType(String sourceType)
  {
    IConflictingValueSource conflictingValueSource = null;
    if (sourceType.equals(CommonConstants.ENTITY_KLASS_TYPE)) {
      conflictingValueSource = new KlassConflictingValueSource();
      conflictingValueSource.setType(CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE);
    }
    else if (sourceType.equals(CommonConstants.TAXONOMY)) {
      conflictingValueSource = new TaxonomyConflictingValueSource();
      conflictingValueSource.setType(CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE);
    }

    return conflictingValueSource;
  }
}
