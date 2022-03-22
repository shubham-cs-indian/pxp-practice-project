package com.cs.core.runtime.strategy.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IKlassInstanceDiffHelperModel extends IModel {
  
  String CHANGED_LANGUAGE_INDEPENDENT_ATTRIBUTE_VARIANT_IDS       = "changedLanguageIndependentAttributeVariantIds";
  String CHANGED_LANGUAGE_DEPENDENT_ATTRIBUTE_IDS_FOR_VIOLATION   = "changedLanguageDependentAttributeIdsForViolation";
  String CHANGED_LANGUAGE_INDEPENDENT_ATTRIBUTE_IDS_FOR_VIOLATION = "changedLanguageIndependentAttributeIdsForViolation";
  String DELETED_LANGUAGE_INDEPENDENT_ATTRIBUTE_IDS               = "deletedLanguageIndependentAttributeIds";
  String DELETED_LANGUAGE_DEPENDENT_ATTRIBUTE_IDS                 = "deletedLanguageDependentAttributeIds";
  String CHANGED_TAG_IDS_FOR_VIOLATION                            = "changedTagIdsForViolation";
  String DELETED_TAG_IDS                                          = "deletedTagIds";
  String CHANGED_LANGUAGE_INDEPENDENT_ATTRIBUTE_IDS               = "changedLanguageIndependentAttributeIds";
  String CHANGED_LANGUAGE_DEPENDENT_ATTRIBUTE_IDS_MAP             = "changedLanguageDependentAttributeIdsMap";
  String CHANGED_LANGUAGE_DEPENDENT_ATTRIBUTE_VARIANTS_IDS        = "changedLanguageDependentAttributeVariantsIds";
  String CHANGED_TAG_IDS                                          = "changedTagIds";
  String ADDED_KLASS_IDS                                          = "addedKlassIds";
  String REMOVED_KLASS_IDS                                        = "removedKlassIds";
  String ADDED_TAXONOMY_IDS                                       = "addedTaxonomyIds";
  String REMOVED_TAXONOMY_IDS                                     = "removedTaxonomyIds";
  String IS_VERSION_OF_CHANGE                                     = "isVersionOfChange";
  String IS_CLONE_OF_CHANGE                                       = "isCloneOfChange";
  String IS_PARENT_INSTANCE_ID_CHANGE                             = "isParentInstanceIdChange";
  String IS_KLASS_INSTANCE_MODIFIED                               = "isKlassInstanceModified";
  String MODIFIED_LANGUAGE_CODES                                  = "modifiedLanguageCodes";
  String ADDED_LANGUAGE_DEPENDENT_ATTRIBUTE_IDS                   = "addedLanguageDependentAttributeIds";
  String ADDED_LANGUAGE_INDEPENDENT_ATTRIBUTE_IDS                 = "addedLanguageIndependentAttributeIds";
  String ADDED_TAG_IDS                                            = "addedTagIds";
  String IS_CONTEXT_MODIFIED                                      = "isContextModified";
  String MAM_VALIDITY_CHANGED                                     = "mamValidityChanged";
  String IS_KLASS_INSTANCE_TYPE_CHANGED                           = "isKlassInstanceTypeChanged";
  String CHANGED_RELATIONSHIP_IDS                                 = "changedRelationshipIds";
  String CHANGED_NATURE_RELATIONSHIP_IDS                          = "changedNatureRelationshipIds";
  String IS_DEFAULT_ASSET_INSTANCE_ID_CHANGED                     = "isDefaultAssetInstanceIdChanged";
  String DELETED_LANGUAGE_TRANSLATIONS                            = "deletedLanguageTranslations";
  String REMOVE_CONFLICT_DATA                                     = "removeConflictData";
  String INSTANCES_TO_UPDATE_IS_MERGED                            = "instancesToUpdateIsMerged";
  
  public List<String> getChangedLanguageIndependentAttributeVariantIds();
  
  public void setChangedLanguageIndependentAttributeVariantIds(
      List<String> changedLanguageIndependentAttributeVariantIds);
  
  public Map<String, List<String>> getChangedLanguageDependentAttributeIdsForViolation();
  
  public void setChangedLanguageDependentAttributeIdsForViolation(
      Map<String, List<String>> changedLanguageDependentAttributeIdsForViolation);
  
  public List<String> getChangedLanguageIndependentAttributeIdsForViolation();
  
  public void setChangedLanguageIndependentAttributeIdsForViolation(
      List<String> changedLanguageIndependentAttributeIdsForViolation);
  
  public List<String> getDeletedLanguageIndependentAttributeIds();
  
  public void setDeletedLanguageIndependentAttributeIds(
      List<String> deletedLanguageIndependentAttributeIds);
  
  public Map<String, List<String>> getDeletedLanguageDependentAttributeIds();
  
  public void setDeletedLanguageDependentAttributeIds(
      Map<String, List<String>> deletedLanguageDependentAttributeIds);
  
  public List<String> getChangedTagIdsForViolation();
  
  public void setChangedTagIdsForViolation(List<String> changedTagIdsForViolation);
  
  public List<String> getDeletedTagIds();
  
  public void setDeletedTagIds(List<String> deletedTagIds);
  
  public List<String> getChangedLanguageIndependentAttributeIds();
  
  public void setChangedLanguageIndependentAttributeIds(
      List<String> changedLanguageIndependentAttributeIds);
  
  public Map<String, List<String>> getChangedLanguageDependentAttributeIdsMap();
  
  public void setChangedLanguageDependentAttributeIdsMap(
      Map<String, List<String>> changedLanguageDependentAttributeIdsMap);
  
  public List<String> getChangedTagIds();
  
  public void setChangedTagIds(List<String> changedTagIds);
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getRemovedKlassIds();
  
  public void setRemovedKlassIds(List<String> removedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getRemovedTaxonomyIds();
  
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds);
  
  public Boolean getIsVersionOfChange();
  
  public void setIsVersionOfChange(Boolean isVersionOfChange);
  
  public Boolean getIsCloneOfChange();
  
  public void setIsCloneOfChange(Boolean isCloneOfChange);
  
  public Boolean getIsParentInstanceIdChange();
  
  public void setIsParentInstanceIdChange(Boolean isParentInstanceIdChange);
  
  public Boolean getIsKlassInstanceModified();
  
  public void setIsKlassInstanceModified(Boolean isKlassInstanceModified);
  
  public List<String> getModifiedLanguageCodes();
  
  public void setModifiedLanguageCodes(List<String> modifiedLanguageCodes);
  
  public Map<String, List<String>> getAddedLanguageDependentAttributeIdsMap();
  
  public void setAddedLanguageDependentAttributeIdsMap(
      Map<String, List<String>> addedLanguageDependentAttributeIds);
  
  public List<String> getAddedLanguageIndependentAttributeIds();
  
  public void setAddedLanguageIndependentAttributeIds(
      List<String> addedLanguageIndependentAttributeIds);
  
  public List<String> getAddedTagIds();
  
  public void setAddedTagIds(List<String> addedTagIds);
  
  public Boolean getIsContextModified();
  
  public void setIsContextModified(Boolean isContextModified);
  
  public Boolean getIsMamValidityChanged();
  
  public void setIsMamValidityChanged(Boolean isMamValidityChanged);
  
  public Boolean getIsKlassInstanceTypeChanged();
  
  public void setIsKlassInstanceTypeChanged(Boolean isKlassInstanceTypeChanged);
  
  public List<String> getChangedRelationshipIds();
  
  public void setChangedRelationshipIds(List<String> changeRedlationshipIds);
  
  public List<String> getChangedNatureRelationshipIds();
  
  public void setChangedNatureRelationshipIds(List<String> changedNatureRelationshipIds);
  
  public Boolean getIsDefaultAssetInstanceIdChanged();
  
  public void setIsDefaultAssetInstanceIdChanged(Boolean isDefaultAssetInstanceIdChanged);
  
  public List<Map<String, Object>> getRemoveConflictData();
  
  public void setRemoveConflictData(List<Map<String, Object>> removeConflictData);
  
  public List<String> getInstancesToUpdateIsMerged();
  
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged);
  
  public List<String> getDeletedLanguageTranslations();
  
  public void setDeletedLanguageTranslations(List<String> deletedLanguageTranslations);
  
  public Map<String, List<String>> getChangedLanguageDependentAttributeVariantsIds();
  
  public void setChangedLanguageDependentAttributeVariantsIds(
      Map<String, List<String>> changedLanguageDependentAttributeVariantsIds);
}
