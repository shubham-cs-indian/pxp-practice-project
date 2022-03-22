package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.context.IContentInfoForContextualValueInheritanceModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesADMPropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.attribute.AttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.attribute.DependentAttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.bulkpropagation.AddedOrDeletedVariantsDataPreparationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IUpdateCRIIDInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.UpdateCRIIDInfoModel;
import com.cs.core.runtime.interactor.model.relationship.*;
import com.cs.core.runtime.interactor.model.tag.TagConflictMapCustomDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentsPropertyDiffModel implements IContentsPropertyDiffModel {
  
  private static final long                                    serialVersionUID                      = 1L;
  protected String                                             contentId;
  protected String                                             baseType;
  protected List<IDataRuleModel>                               referencedDataRules;
  protected List<IDefaultValueChangeModel>                     defaultValuesDiff;
  protected List<String>                                       deletedKlassIds;
  protected List<String>                                       deletedTaxonomyIds;
  protected List<String>                                       deletedRelationshipIds;
  protected List<String>                                       deletedNatureRelationshipIds;
  protected Map<String, List<String>>                          deletedPropertiesFromSource;
  protected List<IRelationshipPropertiesToInheritModel>        relationshipProperties;
  protected List<String>                                       deletedContentIds;
  protected String                                             updatedContentIdUponAddingContentsToRelationship;
  protected IContentsDeleteFromRelationshipModel               deleteContentFromRelationship;
  protected IGetReferencedRelationshipPropertiesModel          referencedRelationshipProperties;
  protected IRelationshipPropertiesADMPropagationModel         relationshipPropertiesADM;
  protected List<String>                                       removedCollectionIds;
  protected Map<String, IAttribute>                            referencedAttributes;
  protected Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships;
  protected Map<String, IReferencedSectionElementModel>        referencedElements;
  protected IAddedDeletedTypesModel                            addedDeletedTypes;
  protected Map<String, List<String>>                          typeIdIdentifierAttributeIds;
  protected IContextualDataForPropagationModel                 contextualDataForPropagation;
  protected IContentInfoForContextualValueInheritanceModel     contentInfoForContextualValueInheritance;
  protected IAddedOrDeletedVariantsDataPreparationModel        addedOrDeletedVariantsInKlassInstance;
  protected Integer                                            numberOfVersionsToMaintain;
  protected Boolean                                            isMerged;
  protected Map<String, ITag>                                  referencedTags;
  protected Map<String, Map<String, List<IConflictingValue>>>  dependentAttributesConflictMap;
  protected Map<String, List<IConflictingValue>>               attributesConflictMap;
  protected Map<String, List<IConflictingValue>>               tagsConflictMap;
  protected List<IDefaultValueChangeModel>                     dependentDefaultValuesDiff;
  protected List<String>                                       languageDependentSkippedAttributeIds;
  protected List<String>                                       languageIndependentSkippedAttributeIds;
  protected List<String>                                       skippedTagIds;
  protected List<String>                                       dependentAttributeIdsToEvaluate;
  protected List<String>                                       independentAttributeIdsToEvaluate;
  protected List<String>                                       tagIdsToEvaluate;
  protected List<String>                                       klassIds;
  protected List<String>                                       taxonomyIds;
  protected List<String>                                       selectedTaxonomyIds;
  protected Boolean                                            shouldUpdateTypes                     = false;
  protected List<IAttributeIdValueModel>                       attributeInstancesValueToApply;
  protected List<IAttributeIdValueModel>                       dependentAttributeInstancesValueToApply;
  protected List<IAttributeIdValueModel>                       independentAttributeInstancesValueToApply;
  protected List<ITagIdValueModel>                             tagInstancesValueToApply;
  protected Map<String, List<String>>                          deletedTranslationsInfo;
  protected IUpdateCRIIDInfoModel                              updateCriidAndDefaulAssetInstanceIdInfo;
  protected Map<String, List<String>>                          removedElementsFromRelationship;
  protected IRemovedPropertiesFromRelationshipModel            removedPropagablePropertiesFromSource;
  protected List<String>                                       deletedTranslationLanguageCodes;
  protected Boolean                                            isIdentifierAttributeEvaluationNeeded = false;   // this
                                                                                                                // is
                                                                                                                // set
                                                                                                                // to
                                                                                                                // true
                                                                                                                // even
                                                                                                                // when
                                                                                                                // content
                                                                                                                // is
                                                                                                                // deleted.
  protected String                                             languageForBulkApply;
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    if (defaultValuesDiff == null) {
      defaultValuesDiff = new ArrayList<>();
    }
    return defaultValuesDiff;
  }
  
  @Override
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<IDataRuleModel> getReferencedDataRules()
  {
    if (referencedDataRules == null) {
      referencedDataRules = new ArrayList<>();
    }
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    if (deletedKlassIds == null) {
      deletedKlassIds = new ArrayList<>();
    }
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    if (deletedTaxonomyIds == null) {
      deletedTaxonomyIds = new ArrayList<>();
    }
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  @Override
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  @Override
  public List<String> getDeletedRelationshipIds()
  {
    if (deletedRelationshipIds == null) {
      deletedRelationshipIds = new ArrayList<>();
    }
    return deletedRelationshipIds;
  }
  
  @Override
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds)
  {
    this.deletedRelationshipIds = deletedRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    if (deletedNatureRelationshipIds == null) {
      deletedNatureRelationshipIds = new ArrayList<>();
    }
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public List<IRelationshipPropertiesToInheritModel> getRelationshipProperties()
  {
    if (relationshipProperties == null) {
      relationshipProperties = new ArrayList<>();
    }
    return relationshipProperties;
  }
  
  @JsonDeserialize(contentAs = RelationshipPropertiesToInheritModel.class)
  @Override
  public void setRelationshipProperties(
      List<IRelationshipPropertiesToInheritModel> relationshipProperties)
  {
    this.relationshipProperties = relationshipProperties;
  }
  
  @Override
  public List<String> getDeletedContentIds()
  {
    if (deletedContentIds == null) {
      deletedContentIds = new ArrayList<>();
    }
    return deletedContentIds;
  }
  
  @Override
  public void setDeletedContentIds(List<String> deletedContentIds)
  {
    this.deletedContentIds = deletedContentIds;
  }
  
  @Override
  public String getUpdatedContentIdUponAddingContentsToRelationship()
  {
    return updatedContentIdUponAddingContentsToRelationship;
  }
  
  @Override
  public void setUpdatedContentIdUponAddingContentsToRelationship(
      String updatedContentIdUponAddingContentsToRelationship)
  {
    this.updatedContentIdUponAddingContentsToRelationship = updatedContentIdUponAddingContentsToRelationship;
  }
  
  @Override
  public IContentsDeleteFromRelationshipModel getDeleteContentFromRelationship()
  {
    return deleteContentFromRelationship;
  }
  
  @Override
  @JsonDeserialize(as = ContentsDeleteFromRelationshipModel.class)
  public void setDeleteContentFromRelationship(
      IContentsDeleteFromRelationshipModel deleteContentFromRelationship)
  {
    this.deleteContentFromRelationship = deleteContentFromRelationship;
  }
  
  public IGetReferencedRelationshipPropertiesModel getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @Override
  @JsonDeserialize(as = GetReferencedRelationshipPropertiesModel.class)
  public void setReferencedRelationshipProperties(
      IGetReferencedRelationshipPropertiesModel referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
  
  @Override
  public IRelationshipPropertiesADMPropagationModel getRelationshipPropertiesADM()
  {
    return relationshipPropertiesADM;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesADMPropagationModel.class)
  public void setRelationshipPropertiesADM(
      IRelationshipPropertiesADMPropagationModel relationshipPropertiesADM)
  {
    this.relationshipPropertiesADM = relationshipPropertiesADM;
  }
  
  @Override
  public List<String> getRemovedCollectionIds()
  {
    if (removedCollectionIds == null) {
      removedCollectionIds = new ArrayList<>();
    }
    return removedCollectionIds;
  }
  
  @Override
  public void setRemovedCollectionIds(List<String> removedCollectionIds)
  {
    this.removedCollectionIds = removedCollectionIds;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  @Override
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public IAddedDeletedTypesModel getAddedDeletedTypes()
  {
    return addedDeletedTypes;
  }
  
  @JsonDeserialize(as = AddedDeletedTypesModel.class)
  @Override
  public void setAddedDeletedTypes(IAddedDeletedTypesModel addedDeletedTypes)
  {
    this.addedDeletedTypes = addedDeletedTypes;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
  }
  
  @Override
  public IContextualDataForPropagationModel getContextualDataForPropagation()
  {
    return contextualDataForPropagation;
  }
  
  @JsonDeserialize(as = ContextualDataForPropagationModel.class)
  @Override
  public void setContextualDataForPropagation(
      IContextualDataForPropagationModel contextualDataForPropagation)
  {
    this.contextualDataForPropagation = contextualDataForPropagation;
  }
  
  public IContentInfoForContextualValueInheritanceModel getContentInfoForContextualValueInheritance()
  {
    return contentInfoForContextualValueInheritance;
  }
  
  @Override
  public void setContentInfoForContextualValueInheritance(
      IContentInfoForContextualValueInheritanceModel contentInfoForContextualValueInheritance)
  {
    this.contentInfoForContextualValueInheritance = contentInfoForContextualValueInheritance;
  }
  
  @Override
  public IAddedOrDeletedVariantsDataPreparationModel getAddedOrDeletedVariantsInKlassInstance()
  {
    return addedOrDeletedVariantsInKlassInstance;
  }
  
  @JsonDeserialize(contentAs = AddedOrDeletedVariantsDataPreparationModel.class)
  @Override
  public void setAddedOrDeletedVariantsInKlassInstance(
      IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariantsInKlassInstance)
  {
    this.addedOrDeletedVariantsInKlassInstance = addedOrDeletedVariantsInKlassInstance;
  }
  
  @Override
  public Integer getNumberOfVersionsToMaintain()
  {
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
  
  @Override
  public Boolean getIsMerged()
  {
    if (isMerged == null) {
      isMerged = false;
    }
    return isMerged;
  }
  
  @Override
  public void setIsMerged(Boolean isMerged)
  {
    this.isMerged = isMerged;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, Map<String, List<IConflictingValue>>> getDependentAttributesConflictMap()
  {
    if (dependentAttributesConflictMap == null) {
      dependentAttributesConflictMap = new HashMap<>();
    }
    return dependentAttributesConflictMap;
  }
  
  @Override
  @JsonDeserialize(contentUsing = DependentAttributesConflictMapCustomDeserializer.class)
  public void setDependentAttributesConflictMap(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributesConflictMap)
  {
    this.dependentAttributesConflictMap = dependentAttributesConflictMap;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getAttributesConflictMap()
  {
    if (attributesConflictMap == null) {
      attributesConflictMap = new HashMap<>();
    }
    return attributesConflictMap;
  }
  
  @Override
  @JsonDeserialize(contentUsing = AttributesConflictMapCustomDeserializer.class)
  public void setAttributesConflictMap(Map<String, List<IConflictingValue>> attributesConflictMap)
  {
    this.attributesConflictMap = attributesConflictMap;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getTagsConflictMap()
  {
    if (tagsConflictMap == null) {
      tagsConflictMap = new HashMap<>();
    }
    return tagsConflictMap;
  }
  
  @Override
  @JsonDeserialize(contentUsing = TagConflictMapCustomDeserializer.class)
  public void setTagsConflictMap(Map<String, List<IConflictingValue>> tagsConflictMap)
  {
    this.tagsConflictMap = tagsConflictMap;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDependentDefaultValuesDiff()
  {
    if (dependentDefaultValuesDiff == null) {
      dependentDefaultValuesDiff = new ArrayList<>();
    }
    return dependentDefaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDependentDefaultValuesDiff(
      List<IDefaultValueChangeModel> dependentDefaultValuesDiff)
  {
    this.dependentDefaultValuesDiff = dependentDefaultValuesDiff;
  }
  
  @Override
  public List<String> getLanguageDependentSkippedAttributeIds()
  {
    if (languageDependentSkippedAttributeIds == null) {
      languageDependentSkippedAttributeIds = new ArrayList<>();
    }
    return languageDependentSkippedAttributeIds;
  }
  
  @Override
  public void setLanguageDependentSkippedAttributeIds(
      List<String> languageDependentSkippedAttributeIds)
  {
    this.languageDependentSkippedAttributeIds = languageDependentSkippedAttributeIds;
  }
  
  @Override
  public List<String> getLanguageIndependentSkippedAttributeIds()
  {
    if (languageIndependentSkippedAttributeIds == null) {
      languageIndependentSkippedAttributeIds = new ArrayList<>();
    }
    return languageIndependentSkippedAttributeIds;
  }
  
  @Override
  public void setLanguageIndependentSkippedAttributeIds(
      List<String> languageIndependentSkippedAttributeIds)
  {
    this.languageIndependentSkippedAttributeIds = languageIndependentSkippedAttributeIds;
  }
  
  @Override
  public List<String> getSkippedTagIds()
  {
    if (skippedTagIds == null) {
      skippedTagIds = new ArrayList<>();
    }
    return skippedTagIds;
  }
  
  @Override
  public void setSkippedTagIds(List<String> skippedTagIds)
  {
    this.skippedTagIds = skippedTagIds;
  }
  
  @Override
  public List<String> getDependentAttributeIdsToEvaluate()
  {
    if (dependentAttributeIdsToEvaluate == null) {
      dependentAttributeIdsToEvaluate = new ArrayList<>();
    }
    return dependentAttributeIdsToEvaluate;
  }
  
  @Override
  public void setDependentAttributeIdsToEvaluate(List<String> dependentAttributeIdsToEvaluate)
  {
    this.dependentAttributeIdsToEvaluate = dependentAttributeIdsToEvaluate;
  }
  
  @Override
  public List<String> getIndependentAttributeIdsToEvaluate()
  {
    if (independentAttributeIdsToEvaluate == null) {
      independentAttributeIdsToEvaluate = new ArrayList<>();
    }
    return independentAttributeIdsToEvaluate;
  }
  
  @Override
  public void setIndependentAttributeIdsToEvaluate(List<String> independentAttributeIdsToEvaluate)
  {
    this.independentAttributeIdsToEvaluate = independentAttributeIdsToEvaluate;
  }
  
  @Override
  public List<String> getTagIdsToEvaluate()
  {
    if (tagIdsToEvaluate == null) {
      tagIdsToEvaluate = new ArrayList<>();
    }
    return tagIdsToEvaluate;
  }
  
  @Override
  public void setTagIdsToEvaluate(List<String> tagIdsToEvaluate)
  {
    this.tagIdsToEvaluate = tagIdsToEvaluate;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public Boolean getShouldUpdateTypes()
  {
    return shouldUpdateTypes;
  }
  
  @Override
  public void setShouldUpdateTypes(Boolean shouldUpdateTypes)
  {
    this.shouldUpdateTypes = shouldUpdateTypes;
  }
  
  @Override
  public List<IAttributeIdValueModel> getDependentAttributeInstancesValueToApply()
  {
    if (dependentAttributeInstancesValueToApply == null) {
      dependentAttributeInstancesValueToApply = new ArrayList<>();
    }
    return dependentAttributeInstancesValueToApply;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeIdValueModel.class)
  public void setDependentAttributeInstancesValueToApply(
      List<IAttributeIdValueModel> dependentAttributeInstancesValueToApply)
  {
    this.dependentAttributeInstancesValueToApply = dependentAttributeInstancesValueToApply;
  }
  
  @Override
  public List<IAttributeIdValueModel> getIndependentAttributeInstancesValueToApply()
  {
    if (independentAttributeInstancesValueToApply == null) {
      independentAttributeInstancesValueToApply = new ArrayList<>();
    }
    return independentAttributeInstancesValueToApply;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeIdValueModel.class)
  public void setIndependentAttributeInstancesValueToApply(
      List<IAttributeIdValueModel> independentAttributeInstancesValueToApply)
  {
    this.independentAttributeInstancesValueToApply = independentAttributeInstancesValueToApply;
  }
  
  @Override
  public List<ITagIdValueModel> getTagInstancesValueToApply()
  {
    if (tagInstancesValueToApply == null) {
      tagInstancesValueToApply = new ArrayList<>();
    }
    return tagInstancesValueToApply;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagIdValueModel.class)
  public void setTagInstancesValueToApply(List<ITagIdValueModel> tagInstanceToApply)
  {
    this.tagInstancesValueToApply = tagInstanceToApply;
  }
  
  @Override
  public List<IAttributeIdValueModel> getAttributeInstancesValueToApply()
  {
    if (attributeInstancesValueToApply == null) {
      attributeInstancesValueToApply = new ArrayList<>();
    }
    return attributeInstancesValueToApply;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeIdValueModel.class)
  public void setAttributeInstancesValueToApply(
      List<IAttributeIdValueModel> attributeInstancesValueToApply)
  {
    this.attributeInstancesValueToApply = attributeInstancesValueToApply;
  }
  
  @Override
  public Map<String, List<String>> getDeletedTranslationsInfo()
  {
    return deletedTranslationsInfo;
  }
  
  @Override
  public void setDeletedTranslationsInfo(Map<String, List<String>> deletedTranslationsInfo)
  {
    this.deletedTranslationsInfo = deletedTranslationsInfo;
  }
  
  @Override
  public IUpdateCRIIDInfoModel getUpdateCriidAndDefaulAssetInstanceIdInfo()
  {
    return updateCriidAndDefaulAssetInstanceIdInfo;
  }
  
  @JsonDeserialize(as = UpdateCRIIDInfoModel.class)
  @Override
  public void setUpdateCriidAndDefaulAssetInstanceIdInfo(
      IUpdateCRIIDInfoModel updateCriidAndDefaulAssetInstanceIdInfo)
  {
    this.updateCriidAndDefaulAssetInstanceIdInfo = updateCriidAndDefaulAssetInstanceIdInfo;
  }
  
  @Override
  public Map<String, List<String>> getRemovedElementsFromRelationship()
  {
    if (removedElementsFromRelationship == null) {
      removedElementsFromRelationship = new HashMap<>();
    }
    return removedElementsFromRelationship;
  }
  
  @Override
  public void setRemovedElementsFromRelationship(
      Map<String, List<String>> removedElementsFromRelationship)
  {
    this.removedElementsFromRelationship = removedElementsFromRelationship;
  }
  
  @Override
  public IRemovedPropertiesFromRelationshipModel getRemovedPropagablePropertiesFromSource()
  {
    return removedPropagablePropertiesFromSource;
  }
  
  @Override
  @JsonDeserialize(as = RemovedPropertiesFromRelationshipModel.class)
  public void setRemovedPropagablePropertiesFromSource(
      IRemovedPropertiesFromRelationshipModel removedPropagablePropertiesFromSource)
  {
    this.removedPropagablePropertiesFromSource = removedPropagablePropertiesFromSource;
  }
  
  public List<String> getDeletedTranslationLanguageCodes()
  {
    if (deletedTranslationLanguageCodes == null) {
      deletedTranslationLanguageCodes = new ArrayList<>();
    }
    return deletedTranslationLanguageCodes;
  }
  
  @Override
  public void setDeletedTranslationLanguageCodes(List<String> deletedTranslationLanguageCodes)
  {
    this.deletedTranslationLanguageCodes = deletedTranslationLanguageCodes;
  }
  
  @Override
  public Boolean getIsIdentifierAttributeEvaluationNeeded()
  {
    return isIdentifierAttributeEvaluationNeeded;
  }
  
  @Override
  public void setIsIdentifierAttributeEvaluationNeeded(
      Boolean isIdentifierAttributeEvaluationNeeded)
  {
    this.isIdentifierAttributeEvaluationNeeded = isIdentifierAttributeEvaluationNeeded;
  }
  
  @Override
  public String getLanguageForBulkApply()
  {
    return languageForBulkApply;
  }
  
  @Override
  public void setLanguageForBulkApply(String languageForBulkApply)
  {
    this.languageForBulkApply = languageForBulkApply;
  }
}
