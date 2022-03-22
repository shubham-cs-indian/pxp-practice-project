package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.searchable.ISearchablePropertyInstancesInformationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchablePropertyInstancesInformationModel
    implements ISearchablePropertyInstancesInformationModel {
  
  private static final long           serialVersionUID = 1L;
  
  protected String                    instanceId;
  protected String                    baseType;
  protected Map<String, List<String>> languageVsDependentPropertyInstanceIdsMap;
  protected List<String>              deletedVariantIds;
  protected List<String>              changedLanguageIndependentAttributeVariantIds;
  protected Map<String, List<String>> languageVsDependentAttributeVariantIdsMap;
  protected List<String>              changedLanguageIndependentAttributeIds;
  protected List<String>              changedTagIds;
  protected List<String>              readOnlyAttributeIds;
  protected List<String>              readOnlyTagIds;
  protected List<String>              taxonomyIdsToTransfer;
  protected Boolean                   isCreate         = false;
  protected List<String>              deletedReadOnlyPropertyIds;
  protected List<String>              deletedLanguageTranslations;
  
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }
  
  @Override
  public void setInstanceId(String instanceId)
  {
    this.instanceId = instanceId;
  }
  
  @Override
  public List<String> getDeletedVariantIds()
  {
    if (deletedVariantIds == null) {
      deletedVariantIds = new ArrayList<>();
    }
    return deletedVariantIds;
  }
  
  @Override
  public void setDeletedVariantIds(List<String> deletedVariantIds)
  {
    this.deletedVariantIds = deletedVariantIds;
  }
  
  @Override
  public Map<String, List<String>> getLanguageVsDependentPropertyInstanceIdsMap()
  {
    if (languageVsDependentPropertyInstanceIdsMap == null) {
      languageVsDependentPropertyInstanceIdsMap = new HashMap<>();
    }
    return languageVsDependentPropertyInstanceIdsMap;
  }
  
  @Override
  public void setLanguageVsDependentPropertyInstanceIdsMap(
      Map<String, List<String>> languageVsDependentPropertyInstanceIdsMap)
  {
    this.languageVsDependentPropertyInstanceIdsMap = languageVsDependentPropertyInstanceIdsMap;
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
  public Map<String, List<String>> getLanguageVsDependentAttributeVariantIdsMap()
  {
    if (languageVsDependentAttributeVariantIdsMap == null) {
      languageVsDependentAttributeVariantIdsMap = new HashMap<>();
    }
    return languageVsDependentAttributeVariantIdsMap;
  }
  
  @Override
  public void setLanguageVsDependentAttributeVariantIdsMap(
      Map<String, List<String>> languageVsDependentAttributeVariantIdsMap)
  {
    this.languageVsDependentAttributeVariantIdsMap = languageVsDependentAttributeVariantIdsMap;
  }
  
  @Override
  public List<String> getReadOnlyAttributeIds()
  {
    if (readOnlyAttributeIds == null) {
      readOnlyAttributeIds = new ArrayList<>();
    }
    return readOnlyAttributeIds;
  }
  
  @Override
  public void setReadOnlyAttributeIds(List<String> readOnlyAttributeIds)
  {
    this.readOnlyAttributeIds = readOnlyAttributeIds;
  }
  
  @Override
  public List<String> getReadOnlyTagIds()
  {
    if (readOnlyTagIds == null) {
      readOnlyTagIds = new ArrayList<>();
    }
    return readOnlyTagIds;
  }
  
  @Override
  public void setReadOnlyTagIds(List<String> readOnlyTagIds)
  {
    this.readOnlyTagIds = readOnlyTagIds;
  }
  
  @Override
  public List<String> getTaxonomyIdsToTransfer()
  {
    if (taxonomyIdsToTransfer == null) {
      taxonomyIdsToTransfer = new ArrayList<>();
    }
    return taxonomyIdsToTransfer;
  }
  
  @Override
  public void setTaxonomyIdsToTransfer(List<String> taxonomyIdsToTransfer)
  {
    this.taxonomyIdsToTransfer = taxonomyIdsToTransfer;
  }
  
  @Override
  public Boolean getIsCreate()
  {
    return isCreate;
  }
  
  @Override
  public void setIsCreate(Boolean isCreate)
  {
    this.isCreate = isCreate;
  }
  
  @Override
  public List<String> getDeletedReadOnlyPropertyIds()
  {
    if (deletedReadOnlyPropertyIds == null) {
      deletedReadOnlyPropertyIds = new ArrayList<>();
    }
    return deletedReadOnlyPropertyIds;
  }
  
  @Override
  public void setDeletedReadOnlyPropertyIds(List<String> deletedReadOnlyPropertyIds)
  {
    this.deletedReadOnlyPropertyIds = deletedReadOnlyPropertyIds;
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
}
