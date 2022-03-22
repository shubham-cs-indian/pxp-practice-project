package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.context.IContentInfoForContextualValueInheritanceModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesADMPropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.bulkpropagation.IUpdateCRIIDInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IContentsPropertyDiffModel extends IModel {
  
  public static final String CONTENT_ID                                              = "contentId";
  public static final String BASETYPE                                                = "baseType";
  public static final String DEFAULT_VALUES_DIFF                                     = "defaultValuesDiff";
  public static final String DELETED_KLASS_IDS                                       = "deletedKlassIds";
  public static final String DELETED_TAXONOMY_IDS                                    = "deletedTaxonomyIds";
  public static final String DELETED_RELATIONSHIP_IDS                                = "deletedRelationshipIds";
  public static final String DELETED_NATURE_RELATIONSHIP_IDS                         = "deletedNatureRelationshipIds";
  public static final String DELETED_PROPERTIES_FROM_SOURCE                          = "deletedPropertiesFromSource";
  public static final String RELATIONSHIP_PROPERTIES                                 = "relationshipProperties";
  public static final String UPDATED_CONTENT_ID_UPON_ADDING_CONTENTS_TO_RELATIONSHIP = "updatedContentIdUponAddingContentsToRelationship";
  public static final String DELETED_CONTENT_IDS                                     = "deletedContentIds";
  public static final String DELETE_CONTENT_FROM_RELATIONSHIP                        = "deleteContentFromRelationship";
  public static final String ADDED_DELETED_TYPES                                     = "addedDeletedTypes";
  public static final String REFERENCED_RELATIONSHIP_PROPERTIES                      = "referencedRelationshipProperties";
  public static final String RELATIONSHIP_PROPERTIES_ADM                             = "relationshipPropertiesADM";
  public static final String REMOVED_COLLECTION_IDS                                  = "removedCollectionIds";
  public static final String REFERENCED_DATA_RULES                                   = "referencedDataRules";
  public static final String REFERENCED_ATTRIBUTES                                   = "referencedAttributes";
  public static final String REFERENCED_NATURE_RELATIONSHIPS                         = "referencedNatureRelationships";
  public static final String REFERENCED_ELEMENTS                                     = "referencedElements";
  public static final String ADDED_OR_DELETED_VARIANTS_IN_KLASS_INSTANCE             = "addedOrDeletedVariantsInKlassInstance";
  public static final String TYPE_ID_IDENTIFIER_ATTRIBUTE_IDS                        = "typeIdIdentifierAttributeIds";
  public static final String CONTEXTUAL_DATA_FOR_PROPAGATION                         = "contextualDataForPropagation";
  public static final String CONTENT_INFO_FOR_CONTEXTUAL_VALUE_INHERITANCE           = "contentInfoForContextualValueInheritance";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN                          = "numberOfVersionsToMaintain";
  public static final String IS_MERGED                                               = "isMerged";
  public static final String REFERENCED_TAGS                                         = "referencedTags";
  public static final String DEPENDENT_ATTRIBUTES_CONFLICT_MAP                       = "dependentAttributesConflictMap";
  public static final String ATTRIBUTES_CONFLICTS_MAP                                = "attributesConflictMap";
  public static final String TAGS_CONFLICTS_MAP                                      = "tagsConflictMap";
  public static final String DEPENDENT_DEFAULT_VALUES_DIFF                           = "dependentDefaultValuesDiff";
  public static final String LANGUAGE_DEPENDENT_SKIPPED_ATTRIBUTE_IDS                = "languageDependentSkippedAttributeIds";
  public static final String LANGUAGE_INDEPENDENT_SKIPPED_ATTRIBUTE_IDS              = "languageIndependentSkippedAttributeIds";
  public static final String SKIPPED_TAG_IDS                                         = "skpippedTagIds";
  public static final String DEPENDENT_ATTRIBUTE_IDS_TO_EVALUATE                     = "dependentAttributeIdsToEvaluate";
  public static final String INDEPENDENT_ATTRIBUTE_IDS_TO_EVALUATE                   = "independentAttributeIdsToEvaluate";
  public static final String TAG_IDS_TO_EVALUATE                                     = "tagIdsToEvaluate";
  public static final String KLASS_IDS                                               = "klassIds";
  public static final String TAXONOMY_IDS                                            = "taxonomyIds";
  public static final String SELECTED_TAXONOMY_IDS                                   = "selectedTaxonomyIds";
  public static final String SHOULD_UPDATE_TYPES                                     = "shouldUpdateTypes";
  public static final String DEPENDENT_ATTRIBUTE_INSTANCES_VALUE_TO_APPLY            = "dependentAttributeInstancesValueToApply";
  public static final String INDEPENDENT_ATTRIBUTE_INSTANCES_VALUE_TO_APPLY          = "independentAttributeInstancesValueToApply";
  public static final String TAG_INSTANCE_VALUES_TO_APPLY                            = "tagInstancesValueToApply";
  public static final String ATTRIBUTE_INSTANCES_VALUE_TO_APPLY                      = "attributeInstancesValueToApply";
  public static final String DELETED_TRANSLATIONS_INFO                               = "deletedTranslationsInfo";
  public static final String UPDATE_CRIID_AND_DEFAULT_ASSET_INSTANCE_ID_INFO         = "updateCriidAndDefaulAssetInstanceIdInfo";
  public static final String REMOVED_ELEMENTS_FROM_RELATIONSHIP                      = "removedElementsFromRelationship";
  public static final String REMOVED_PROPAGABLE_PROPERTIES_FROM_SOURCE               = "removedPropagablePropertiesFromSource";
  public static final String DELETED_TRANSLATION_LANGUAGE_CODES                      = "deletedTranslationLanguageCodes";
  public static final String IS_IDENTIFIER_ATTRIBUTE_EVALUATION_NEEDED               = "isIdentifierAttributeEvaluationNeeded";
  public static final String LANGUAGE_FOR_BULK_APPLY                                 = "languageForBulkApply";
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public List<String> getDeletedRelationshipIds();
  
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public List<IRelationshipPropertiesToInheritModel> getRelationshipProperties();
  
  public void setRelationshipProperties(
      List<IRelationshipPropertiesToInheritModel> relationshipProperties);
  
  public List<String> getDeletedContentIds();
  
  public void setDeletedContentIds(List<String> deletedContentIds);
  
  public String getUpdatedContentIdUponAddingContentsToRelationship();
  
  public void setUpdatedContentIdUponAddingContentsToRelationship(
      String updatedContentIdUponAddingContentsToRelationship);
  
  public IContentsDeleteFromRelationshipModel getDeleteContentFromRelationship();
  
  public void setDeleteContentFromRelationship(
      IContentsDeleteFromRelationshipModel deleteContentFromRelationship);
  
  public IGetReferencedRelationshipPropertiesModel getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      IGetReferencedRelationshipPropertiesModel referencedRelationshipProperties);
  
  public IRelationshipPropertiesADMPropagationModel getRelationshipPropertiesADM();
  
  public void setRelationshipPropertiesADM(
      IRelationshipPropertiesADMPropagationModel relationshipPropertiesADM);
  
  public List<String> getRemovedCollectionIds();
  
  public void setRemovedCollectionIds(List<String> removedCollectionIds);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public IAddedDeletedTypesModel getAddedDeletedTypes();
  
  public void setAddedDeletedTypes(IAddedDeletedTypesModel addedDeletedTypes);
  
  public IAddedOrDeletedVariantsDataPreparationModel getAddedOrDeletedVariantsInKlassInstance();
  
  public void setAddedOrDeletedVariantsInKlassInstance(
      IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariantsInKlassInstance);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public IContextualDataForPropagationModel getContextualDataForPropagation();
  
  public void setContextualDataForPropagation(
      IContextualDataForPropagationModel contextualDataForPropagation);
  
  public IContentInfoForContextualValueInheritanceModel getContentInfoForContextualValueInheritance();
  
  public void setContentInfoForContextualValueInheritance(
      IContentInfoForContextualValueInheritanceModel contentInfoForContextualValueInheritance);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
  
  public Boolean getIsMerged();
  
  public void setIsMerged(Boolean isMerged);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  // key:attributeId
  public Map<String, List<IConflictingValue>> getAttributesConflictMap();
  
  public void setAttributesConflictMap(Map<String, List<IConflictingValue>> attributesConflictMap);
  
  // key:tagId
  public Map<String, List<IConflictingValue>> getTagsConflictMap();
  
  public void setTagsConflictMap(Map<String, List<IConflictingValue>> tagsConflictMap);
  
  // key:languageCode
  public Map<String, Map<String, List<IConflictingValue>>> getDependentAttributesConflictMap();
  
  public void setDependentAttributesConflictMap(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributesConflictMap);
  
  public List<IDefaultValueChangeModel> getDependentDefaultValuesDiff();
  
  public void setDependentDefaultValuesDiff(
      List<IDefaultValueChangeModel> dependentDefaultValuesDiff);
  
  public List<String> getLanguageDependentSkippedAttributeIds();
  
  public void setLanguageDependentSkippedAttributeIds(
      List<String> languageDependentSkippedAttributeIds);
  
  public List<String> getLanguageIndependentSkippedAttributeIds();
  
  public void setLanguageIndependentSkippedAttributeIds(
      List<String> languageIndependentSkippedAttributeIds);
  
  public List<String> getSkippedTagIds();
  
  public void setSkippedTagIds(List<String> skippedTagIds);
  
  public List<String> getDependentAttributeIdsToEvaluate();
  
  public void setDependentAttributeIdsToEvaluate(List<String> dependentAttributeIdsToEvaluate);
  
  public List<String> getIndependentAttributeIdsToEvaluate();
  
  public void setIndependentAttributeIdsToEvaluate(List<String> independentAttributeIdsToEvaluate);
  
  public List<String> getTagIdsToEvaluate();
  
  public void setTagIdsToEvaluate(List<String> independentAttributeIdsToEvaluate);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public Boolean getShouldUpdateTypes();
  
  public void setShouldUpdateTypes(Boolean shouldUpdateTypes);
  
  public List<IAttributeIdValueModel> getDependentAttributeInstancesValueToApply();
  
  public void setDependentAttributeInstancesValueToApply(
      List<IAttributeIdValueModel> dependentAttributeInstancesValueToApply);
  
  public List<IAttributeIdValueModel> getIndependentAttributeInstancesValueToApply();
  
  public void setIndependentAttributeInstancesValueToApply(
      List<IAttributeIdValueModel> independentAttributeInstancesValueToApply);
  
  public List<ITagIdValueModel> getTagInstancesValueToApply();
  
  public void setTagInstancesValueToApply(List<ITagIdValueModel> tagInstancesValueToApply);
  
  public List<IAttributeIdValueModel> getAttributeInstancesValueToApply();
  
  public void setAttributeInstancesValueToApply(
      List<IAttributeIdValueModel> attributeInstancesValueToApply);
  
  // key:ProductId //value:List of languageCodes of deleted translations.
  public Map<String, List<String>> getDeletedTranslationsInfo();
  
  public void setDeletedTranslationsInfo(Map<String, List<String>> deletedTranslationsInfo);
  
  public IUpdateCRIIDInfoModel getUpdateCriidAndDefaulAssetInstanceIdInfo();
  
  public void setUpdateCriidAndDefaulAssetInstanceIdInfo(
      IUpdateCRIIDInfoModel updateCriidAndDefaulAssetInstanceIdInfo);
  
  // key:relationshipId
  public Map<String, List<String>> getRemovedElementsFromRelationship();
  
  public void setRemovedElementsFromRelationship(
      Map<String, List<String>> removedElementsFromRelationship);
  
  public IRemovedPropertiesFromRelationshipModel getRemovedPropagablePropertiesFromSource();
  
  public void setRemovedPropagablePropertiesFromSource(
      IRemovedPropertiesFromRelationshipModel removedPropagablePropertiesFromSource);
  
  public List<String> getDeletedTranslationLanguageCodes();
  
  public void setDeletedTranslationLanguageCodes(List<String> deletedTranslationLanguageCodes);
  
  public Boolean getIsIdentifierAttributeEvaluationNeeded();
  
  public void setIsIdentifierAttributeEvaluationNeeded(
      Boolean isIdentifierAttributeEvaluationNeeded);
  
  public String getLanguageForBulkApply();
  
  public void setLanguageForBulkApply(String languageForBulkApply);
}
