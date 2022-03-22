package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ISearchablePropertyInstancesInformationModel extends IModel {
  
  String INSTANCE_ID                                        = "instanceId";
  String BASE_TYPE                                          = "baseType";
  String DELETED_VARIANT_IDS                                = "deletedVariantIds";
  String CHANGED_LANGUAGE_INDEPENDENT_ATTRIBUTE_VARIANT_IDS = "changedLanguageIndependentAttributeVariantIds";
  String LANGUAGE_VS_DEPENDENT_ATTRIBUTE_VARIANT_IDS_MAP    = "languageVsDependentAttributeVariantIdsMap";
  String CHANGED_LANGUAGE_INDEPENDENT_ATTRIBUTE_IDS         = "changedLanguageIndependentAttributeIds";
  String CHANGED_TAG_IDS                                    = "changedTagIds";
  String LANGUAGE_VS_DEPENDENT_PROPERTY_INSTANCE_IDS_MAP    = "languageVsDependentPropertyInstanceIdsMap";
  String READ_ONLY_ATTRIBUTE_IDS                            = "readOnlyAttributeIds";
  String READ_ONLY_TAG_IDS                                  = "readOnlyTagIds";
  String IS_CREATE                                          = "isCreate";
  String DELETED_READ_ONLY_PROPERTY_IDS                     = "deletedReadOnlyPropertyIds";
  String TAXONOMY_IDS_TO_TRANSFER                           = "taxonomyIdsToTransfer";
  String DELETED_LANGUAGE_TRANSLATIONS                      = "deletedLanguageTranslations";
  
  public String getInstanceId();
  
  public void setInstanceId(String instanceId);
  
  public List<String> getDeletedVariantIds();
  
  public void setDeletedVariantIds(List<String> deletedVariantIds);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getChangedLanguageIndependentAttributeVariantIds();
  
  public void setChangedLanguageIndependentAttributeVariantIds(
      List<String> changedLanguageIndependentAttributeVariantIds);
  
  public Map<String, List<String>> getLanguageVsDependentAttributeVariantIdsMap();
  
  public void setLanguageVsDependentAttributeVariantIdsMap(
      Map<String, List<String>> languageVsDependentAttributeVariantIdsMap);
  
  public List<String> getChangedTagIds();
  
  public void setChangedTagIds(List<String> changedTagIds);
  
  public List<String> getChangedLanguageIndependentAttributeIds();
  
  public void setChangedLanguageIndependentAttributeIds(
      List<String> changedLanguageIndependentAttributeIds);
  
  // key: languageId Value: changed dependent attribute ids List
  public Map<String, List<String>> getLanguageVsDependentPropertyInstanceIdsMap();
  
  public void setLanguageVsDependentPropertyInstanceIdsMap(
      Map<String, List<String>> languageVsDependentPropertyInstanceIdsMap);
  
  public List<String> getReadOnlyAttributeIds();
  
  public void setReadOnlyAttributeIds(List<String> readOnlyAttributeIds);
  
  public List<String> getReadOnlyTagIds();
  
  public void setReadOnlyTagIds(List<String> readOnlyTagIds);
  
  public List<String> getTaxonomyIdsToTransfer();
  
  public void setTaxonomyIdsToTransfer(List<String> taxonomyIdsToTransfer);
  
  public Boolean getIsCreate();
  
  public void setIsCreate(Boolean isCreate);
  
  public List<String> getDeletedReadOnlyPropertyIds();
  
  public void setDeletedReadOnlyPropertyIds(List<String> deletedReadOnlyPropertyIds);
  
  public List<String> getDeletedLanguageTranslations();
  
  public void setDeletedLanguageTranslations(List<String> deletedLanguageTranslations);
}
