package com.cs.core.runtime.strategy.model.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassInstanceDiffHelperModel implements IKlassInstanceDiffHelperModel {
  
  private static final long           serialVersionUID = 1L;
  protected List<String>              changedLanguageIndependentAttributeVariantIds;
  protected List<String>              changedLanguageIndependentAttributeIds;
  protected Map<String, List<String>> changedLanguageDependentAttributeIdsMap;
  protected Map<String, List<String>> changedLanguageDependentAttributeVariantsIds;
  protected List<String>              changedTagIds;
  protected List<String>              addedKlassIds;
  protected List<String>              removedKlassIds;
  protected List<String>              addedTaxonomyIds;
  protected List<String>              removedTaxonomyIds;
  protected Boolean                   isVersionOfChange;
  protected Boolean                   isCloneOfChange;
  protected Boolean                   isParentInstanceIdChange;
  protected Boolean                   isKlassInstanceModified;
  protected List<String>              modifiedLanguageCodes;
  protected Map<String, List<String>> addedLanguageDependentAttributeIdsMap;
  protected List<String>              addedLanguageIndependentAttributeIds;
  protected List<String>              addedTagIds;
  protected Boolean                   isContextModified;
  protected Boolean                   isMamValidityChanged;
  protected Boolean                   isKlassInstanceTypeChanged;
  protected List<String>              changeRedlationshipIds;
  protected List<String>              changedNatureRelationshipIds;
  protected Boolean                   isDefaultAssetInstanceIdChanged;
  protected List<String>              instancesToUpdateIsMerged;
  protected List<Map<String, Object>> removeConflictData;
  protected Map<String, List<String>> changedLanguageDependentAttributeIdsForViolation;
  protected List<String>              changedLanguageIndependentAttributeIdsForViolation;
  protected List<String>              deletedLanguageIndependentAttributeIds;
  protected Map<String, List<String>> deletedLanguageDependentAttributeIds;
  protected List<String>              deletedTagIds;
  protected List<String>              changedTagIdsForViolation;
  protected List<String>              deletedLanguageTranslations;
  
  @Override
  public List<String> getChangedLanguageIndependentAttributeVariantIds()
  {
    if (changedLanguageIndependentAttributeVariantIds == null) {
      changedLanguageIndependentAttributeVariantIds = new ArrayList<>();
    }
    return changedLanguageIndependentAttributeVariantIds;
  }
  
  @Override
  public void setChangedLanguageIndependentAttributeVariantIds(
      List<String> changedLanguageIndependentAttributeVariantIds)
  {
    this.changedLanguageIndependentAttributeVariantIds = changedLanguageIndependentAttributeVariantIds;
  }
  
  @Override
  public List<String> getChangedTagIds()
  {
    if (changedTagIds == null) {
      changedTagIds = new ArrayList<>();
    }
    return changedTagIds;
  }
  
  @Override
  public void setChangedTagIds(List<String> changedTagIds)
  {
    this.changedTagIds = changedTagIds;
  }
  
  @Override
  public List<String> getAddedKlassIds()
  {
    if (addedKlassIds == null) {
      addedKlassIds = new ArrayList<>();
    }
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getRemovedKlassIds()
  {
    if (removedKlassIds == null) {
      removedKlassIds = new ArrayList<>();
    }
    return removedKlassIds;
  }
  
  @Override
  public void setRemovedKlassIds(List<String> removedKlassIds)
  {
    this.removedKlassIds = removedKlassIds;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    if (addedTaxonomyIds == null) {
      addedTaxonomyIds = new ArrayList<>();
    }
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getRemovedTaxonomyIds()
  {
    if (removedTaxonomyIds == null) {
      removedTaxonomyIds = new ArrayList<>();
    }
    return removedTaxonomyIds;
  }
  
  @Override
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds)
  {
    this.removedTaxonomyIds = removedTaxonomyIds;
  }
  
  @Override
  public Boolean getIsVersionOfChange()
  {
    if (isVersionOfChange == null) {
      isVersionOfChange = false;
    }
    return isVersionOfChange;
  }
  
  @Override
  public void setIsVersionOfChange(Boolean isVersionOfChange)
  {
    this.isVersionOfChange = isVersionOfChange;
  }
  
  @Override
  public Boolean getIsCloneOfChange()
  {
    if (isCloneOfChange == null) {
      isCloneOfChange = false;
    }
    return isCloneOfChange;
  }
  
  @Override
  public void setIsCloneOfChange(Boolean isCloneOfChange)
  {
    this.isCloneOfChange = isCloneOfChange;
  }
  
  @Override
  public Boolean getIsParentInstanceIdChange()
  {
    if (isParentInstanceIdChange == null) {
      isParentInstanceIdChange = false;
    }
    return isParentInstanceIdChange;
  }
  
  @Override
  public void setIsParentInstanceIdChange(Boolean isParentInstanceIdChange)
  {
    this.isParentInstanceIdChange = isParentInstanceIdChange;
  }
  
  @Override
  public Boolean getIsKlassInstanceModified()
  {
    if (isKlassInstanceModified == null) {
      isKlassInstanceModified = false;
    }
    return isKlassInstanceModified;
  }
  
  @Override
  public void setIsKlassInstanceModified(Boolean isKlassInstanceModified)
  {
    this.isKlassInstanceModified = isKlassInstanceModified;
  }
  
  @Override
  public List<String> getModifiedLanguageCodes()
  {
    if (modifiedLanguageCodes == null) {
      modifiedLanguageCodes = new ArrayList<>();
    }
    return modifiedLanguageCodes;
  }
  
  @Override
  public void setModifiedLanguageCodes(List<String> isLanguageInstancesModified)
  {
    
    this.modifiedLanguageCodes = modifiedLanguageCodes;
  }
  
  @Override
  public Map<String, List<String>> getAddedLanguageDependentAttributeIdsMap()
  {
    if (addedLanguageDependentAttributeIdsMap == null) {
      addedLanguageDependentAttributeIdsMap = new HashMap<>();
    }
    return addedLanguageDependentAttributeIdsMap;
  }
  
  @Override
  public void setAddedLanguageDependentAttributeIdsMap(
      Map<String, List<String>> addedLanguageDependentAttributeIdsMap)
  {
    this.addedLanguageDependentAttributeIdsMap = addedLanguageDependentAttributeIdsMap;
  }
  
  @Override
  public List<String> getAddedLanguageIndependentAttributeIds()
  {
    if (addedLanguageIndependentAttributeIds == null) {
      addedLanguageIndependentAttributeIds = new ArrayList<>();
    }
    return addedLanguageIndependentAttributeIds;
  }
  
  @Override
  public void setAddedLanguageIndependentAttributeIds(
      List<String> addedLanguageIndependentAttributeIds)
  {
    this.addedLanguageIndependentAttributeIds = addedLanguageIndependentAttributeIds;
  }
  
  @Override
  public List<String> getAddedTagIds()
  {
    if (addedTagIds == null) {
      addedTagIds = new ArrayList<>();
    }
    return addedTagIds;
  }
  
  @Override
  public void setAddedTagIds(List<String> addedTagIds)
  {
    this.addedTagIds = addedTagIds;
  }
  
  @Override
  public Boolean getIsContextModified()
  {
    if (isContextModified == null) {
      isContextModified = false;
    }
    return isContextModified;
  }
  
  @Override
  public void setIsContextModified(Boolean isContextModified)
  {
    this.isContextModified = isContextModified;
  }
  
  public List<String> getChangedLanguageIndependentAttributeIds()
  {
    if (changedLanguageIndependentAttributeIds == null) {
      changedLanguageIndependentAttributeIds = new ArrayList<>();
    }
    return changedLanguageIndependentAttributeIds;
  }
  
  @Override
  public void setChangedLanguageIndependentAttributeIds(
      List<String> changedLanguageIndependentAttributeIds)
  {
    this.changedLanguageIndependentAttributeIds = changedLanguageIndependentAttributeIds;
  }
  
  @Override
  public Map<String, List<String>> getChangedLanguageDependentAttributeIdsMap()
  {
    if (changedLanguageDependentAttributeIdsMap == null) {
      changedLanguageDependentAttributeIdsMap = new HashMap<>();
    }
    return changedLanguageDependentAttributeIdsMap;
  }
  
  @Override
  public void setChangedLanguageDependentAttributeIdsMap(
      Map<String, List<String>> changedLanguageDependentAttributeIdsMap)
  {
    this.changedLanguageDependentAttributeIdsMap = changedLanguageDependentAttributeIdsMap;
  }
  
  @Override
  public Boolean getIsMamValidityChanged()
  {
    return isMamValidityChanged;
  }
  
  @Override
  public void setIsMamValidityChanged(Boolean isMamValidityChanged)
  {
    this.isMamValidityChanged = isMamValidityChanged;
  }
  
  @Override
  public Boolean getIsKlassInstanceTypeChanged()
  {
    return isKlassInstanceTypeChanged;
  }
  
  @Override
  public void setIsKlassInstanceTypeChanged(Boolean isKlassInstanceTypeChanged)
  {
    this.isKlassInstanceTypeChanged = isKlassInstanceTypeChanged;
  }
  
  @Override
  public List<String> getChangedRelationshipIds()
  {
    if (changeRedlationshipIds == null) {
      changeRedlationshipIds = new ArrayList<>();
    }
    return changeRedlationshipIds;
  }
  
  @Override
  public void setChangedRelationshipIds(List<String> changeRedlationshipIds)
  {
    this.changeRedlationshipIds = changeRedlationshipIds;
  }
  
  @Override
  public List<String> getChangedNatureRelationshipIds()
  {
    if (changedNatureRelationshipIds == null) {
      changedNatureRelationshipIds = new ArrayList<>();
    }
    return changedNatureRelationshipIds;
  }
  
  @Override
  public void setChangedNatureRelationshipIds(List<String> changedNatureRelationshipIds)
  {
    this.changedNatureRelationshipIds = changedNatureRelationshipIds;
  }
  
  @Override
  public Boolean getIsDefaultAssetInstanceIdChanged()
  {
    if (isDefaultAssetInstanceIdChanged == null) {
      isDefaultAssetInstanceIdChanged = false;
    }
    return isDefaultAssetInstanceIdChanged;
  }
  
  @Override
  public void setIsDefaultAssetInstanceIdChanged(Boolean isDefaultAssetInstanceIdChanged)
  {
    this.isDefaultAssetInstanceIdChanged = isDefaultAssetInstanceIdChanged;
  }
  
  @Override
  public List<String> getInstancesToUpdateIsMerged()
  {
    if (instancesToUpdateIsMerged == null) {
      instancesToUpdateIsMerged = new ArrayList<>();
    }
    return instancesToUpdateIsMerged;
  }
  
  @Override
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged)
  {
    this.instancesToUpdateIsMerged = instancesToUpdateIsMerged;
  }
  
  @Override
  public List<Map<String, Object>> getRemoveConflictData()
  {
    if (removeConflictData == null) {
      removeConflictData = new ArrayList<>();
    }
    return removeConflictData;
  }
  
  @Override
  public void setRemoveConflictData(List<Map<String, Object>> removeConflictData)
  {
    this.removeConflictData = removeConflictData;
  }
  
  public Map<String, List<String>> getChangedLanguageDependentAttributeIdsForViolation()
  {
    if (changedLanguageDependentAttributeIdsForViolation == null) {
      changedLanguageDependentAttributeIdsForViolation = new HashMap<>();
    }
    return changedLanguageDependentAttributeIdsForViolation;
  }
  
  @Override
  public void setChangedLanguageDependentAttributeIdsForViolation(
      Map<String, List<String>> changedLanguageDependentAttributeIdsForViolation)
  {
    this.changedLanguageDependentAttributeIdsForViolation = changedLanguageDependentAttributeIdsForViolation;
  }
  
  @Override
  public List<String> getChangedLanguageIndependentAttributeIdsForViolation()
  {
    if (changedLanguageIndependentAttributeIdsForViolation == null) {
      changedLanguageIndependentAttributeIdsForViolation = new ArrayList<>();
    }
    return changedLanguageIndependentAttributeIdsForViolation;
  }
  
  @Override
  public void setChangedLanguageIndependentAttributeIdsForViolation(
      List<String> changedLanguageIndependentAttributeIdsForViolation)
  {
    this.changedLanguageIndependentAttributeIdsForViolation = changedLanguageIndependentAttributeIdsForViolation;
  }
  
  @Override
  public List<String> getDeletedLanguageIndependentAttributeIds()
  {
    if (deletedLanguageIndependentAttributeIds == null) {
      deletedLanguageIndependentAttributeIds = new ArrayList<>();
    }
    return deletedLanguageIndependentAttributeIds;
  }
  
  @Override
  public void setDeletedLanguageIndependentAttributeIds(
      List<String> deletedLanguageIndependentAttributeIds)
  {
    this.deletedLanguageIndependentAttributeIds = deletedLanguageIndependentAttributeIds;
  }
  
  @Override
  public List<String> getChangedTagIdsForViolation()
  {
    if (changedTagIdsForViolation == null) {
      changedTagIdsForViolation = new ArrayList<>();
    }
    return changedTagIdsForViolation;
  }
  
  @Override
  public void setChangedTagIdsForViolation(List<String> changedTagIdsForViolation)
  {
    this.changedTagIdsForViolation = changedTagIdsForViolation;
  }
  
  @Override
  public Map<String, List<String>> getDeletedLanguageDependentAttributeIds()
  {
    if (deletedLanguageDependentAttributeIds == null) {
      deletedLanguageDependentAttributeIds = new HashMap<>();
    }
    return deletedLanguageDependentAttributeIds;
  }
  
  @Override
  public void setDeletedLanguageDependentAttributeIds(
      Map<String, List<String>> deletedLanguageDependentAttributeIds)
  {
    this.deletedLanguageDependentAttributeIds = deletedLanguageDependentAttributeIds;
  }
  
  @Override
  public List<String> getDeletedTagIds()
  {
    if (deletedTagIds == null) {
      deletedTagIds = new ArrayList<>();
    }
    return deletedTagIds;
  }
  
  @Override
  public void setDeletedTagIds(List<String> deletedTagIds)
  {
    this.deletedTagIds = deletedTagIds;
  }
  
  @Override
  public List<String> getDeletedLanguageTranslations()
  {
    if (deletedLanguageTranslations == null) {
      deletedLanguageTranslations = new ArrayList<>();
    }
    return deletedLanguageTranslations;
  }
  
  @Override
  public void setDeletedLanguageTranslations(List<String> deletedLanguageTranslations)
  {
    this.deletedLanguageTranslations = deletedLanguageTranslations;
  }
  
  @Override
  public Map<String, List<String>> getChangedLanguageDependentAttributeVariantsIds()
  {
    if (changedLanguageDependentAttributeVariantsIds == null) {
      changedLanguageDependentAttributeVariantsIds = new HashMap<>();
    }
    return changedLanguageDependentAttributeVariantsIds;
  }
  
  @Override
  public void setChangedLanguageDependentAttributeVariantsIds(
      Map<String, List<String>> changedLanguageDependentAttributeVariantsMap)
  {
    this.changedLanguageDependentAttributeVariantsIds = changedLanguageDependentAttributeVariantsMap;
  }
}
