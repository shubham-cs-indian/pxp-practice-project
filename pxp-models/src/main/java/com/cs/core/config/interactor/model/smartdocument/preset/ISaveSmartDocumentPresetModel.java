package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleTags;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveSmartDocumentPresetModel extends IModel {
  
  public static final String SMART_DCOUMENT_PRESET    = "smartDocumentPreset";
  public static final String ADDED_ATTRIBUTE_IDS      = "addedAttributeIds";
  public static final String DELETED_ATTRIBUTE_IDS    = "deletedAttributeIds";
  public static final String ADDED_TAG_IDS            = "addedTagIds";
  public static final String DELETED_TAG_IDS          = "deletedTagIds";
  public static final String ADDED_KLASS_IDS          = "addedKlassIds";
  public static final String DELETED_KLASS_IDS        = "deletedKlassIds";
  public static final String ADDED_TAXONOMY_IDS       = "addedTaxonomyIds";
  public static final String DELETED_TAXONOMY_IDS     = "deletedTaxonomyIds";
  public static final String ADDED_ATTRIBUTE_RULES    = "addedAttributeRules";
  public static final String MODIFIED_ATTRIBUTE_RULES = "modifiedAttributeRules";
  public static final String DELETED_ATTRIBUTE_RULES  = "deletedAttributeRules";
  public static final String ADDED_TAG_RULES          = "addedTagRules";
  public static final String DELETED_TAG_RULES        = "deletedTagRules";
  public static final String MODIFIED_TAG_RULES       = "modifiedTagRules";
  
  public ISmartDocumentPresetModel getSmartDocumentPreset();
  
  public void setSmartDocumentPreset(ISmartDocumentPresetModel smartDocumentPreset);
  
  public List<String> getAddedAttributeIds();
  
  public void setAddedAttributeIds(List<String> addedAttributeIds);
  
  public List<String> getDeletedAttributeIds();
  
  public void setDeletedAttributeIds(List<String> deletedAttributeIds);
  
  public List<String> getAddedTagIds();
  
  public void setAddedTagIds(List<String> addedTagIds);
  
  public List<String> getDeletedTagIds();
  
  public void setDeletedTagIds(List<String> deletedTagIds);
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public List<ISmartDocumentPresetRuleIntermediateEntity> getAddedAttributeRules();
  
  public void setAddedAttributeRules(
      List<ISmartDocumentPresetRuleIntermediateEntity> addedAttributeRules);
  
  public List<String> getDeletedAttributeRules();
  
  public void setDeletedAttributeRules(List<String> deletedAttributeRules);
  
  public List<IModifiedSmartDocumentPresetRuleEntityModel> getModifiedAttributeRules();
  
  public void setModifiedAttributeRules(
      List<IModifiedSmartDocumentPresetRuleEntityModel> modifiedAttributeRules);
  
  public List<ISmartDocumentPresetRuleTags> getAddedTagRules();
  
  public void setAddedTagRules(List<ISmartDocumentPresetRuleTags> addedTagRules);
  
  public List<String> getDeletedTagRules();
  
  public void setDeletedTagRules(List<String> deletedTagRules);
  
  public List<IModifiedSmartDocumentPresetTagRuleEntityModel> getModifiedTagRules();
  
  public void setModifiedTagRules(
      List<IModifiedSmartDocumentPresetTagRuleEntityModel> modifiedTagRules);
}
