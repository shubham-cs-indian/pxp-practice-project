package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetRuleTags;
import com.cs.core.config.interactor.entity.smartdocument.preset.SmartDocumentPresetRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.smartdocument.preset.SmartDocumentPresetRuleTags;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveSmartDocumentPresetModel implements ISaveSmartDocumentPresetModel {
  
  private static final long                                      serialVersionUID       = 1L;
  protected ISmartDocumentPresetModel                            smartDocumentPreset    = new SmartDocumentPresetModel();
  protected List<String>                                         addedAttributeIds      = new ArrayList<>();
  protected List<String>                                         deletedAttributeIds    = new ArrayList<>();
  protected List<String>                                         addedTagIds            = new ArrayList<>();
  protected List<String>                                         deletedTagIds          = new ArrayList<>();
  protected List<String>                                         addedKlassIds          = new ArrayList<>();
  protected List<String>                                         deletedKlassIds        = new ArrayList<>();
  protected List<String>                                         addedTaxonomyIds       = new ArrayList<>();
  protected List<String>                                         deletedTaxonomyIds     = new ArrayList<>();
  protected List<ISmartDocumentPresetRuleIntermediateEntity>     addedAttributeRules    = new ArrayList<>();
  protected List<String>                                         deletedAttributeRules  = new ArrayList<>();
  protected List<IModifiedSmartDocumentPresetRuleEntityModel>    modifiedAttributeRules = new ArrayList<>();
  protected List<ISmartDocumentPresetRuleTags>                   addedTagRules          = new ArrayList<>();
  protected List<String>                                         deletedTagRules        = new ArrayList<>();
  protected List<IModifiedSmartDocumentPresetTagRuleEntityModel> modifiedTagRules       = new ArrayList<>();
  
  @Override
  public ISmartDocumentPresetModel getSmartDocumentPreset()
  {
    return smartDocumentPreset;
  }
  
  @Override
  @JsonDeserialize(as = SmartDocumentPresetModel.class)
  public void setSmartDocumentPreset(ISmartDocumentPresetModel smartDocumentPreset)
  {
    this.smartDocumentPreset = smartDocumentPreset;
  }
  
  @Override
  public List<String> getAddedAttributeIds()
  {
    return addedAttributeIds;
  }
  
  @Override
  public void setAddedAttributeIds(List<String> addedAttributes)
  {
    this.addedAttributeIds = addedAttributes;
  }
  
  @Override
  public List<String> getDeletedAttributeIds()
  {
    return deletedAttributeIds;
  }
  
  @Override
  public void setDeletedAttributeIds(List<String> deletedAttributes)
  {
    this.deletedAttributeIds = deletedAttributes;
  }
  
  @Override
  public List<String> getAddedTagIds()
  {
    return addedTagIds;
  }
  
  @Override
  public void setAddedTagIds(List<String> addedTags)
  {
    this.addedTagIds = addedTags;
  }
  
  @Override
  public List<String> getDeletedTagIds()
  {
    return deletedTagIds;
  }
  
  @Override
  public void setDeletedTagIds(List<String> deletedTags)
  {
    this.deletedTagIds = deletedTags;
  }
  
  @Override
  public List<String> getAddedKlassIds()
  {
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  @Override
  public List<ISmartDocumentPresetRuleIntermediateEntity> getAddedAttributeRules()
  {
    return addedAttributeRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetRuleIntermediateEntity.class)
  public void setAddedAttributeRules(
      List<ISmartDocumentPresetRuleIntermediateEntity> addedAttributeRules)
  {
    this.addedAttributeRules = addedAttributeRules;
  }
  
  @Override
  public List<String> getDeletedAttributeRules()
  {
    return deletedAttributeRules;
  }
  
  @Override
  public void setDeletedAttributeRules(List<String> deletedAttributeRules)
  {
    this.deletedAttributeRules = deletedAttributeRules;
  }
  
  @Override
  public List<IModifiedSmartDocumentPresetRuleEntityModel> getModifiedAttributeRules()
  {
    return modifiedAttributeRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedSmartDocumentPresetRuleEntityModel.class)
  public void setModifiedAttributeRules(
      List<IModifiedSmartDocumentPresetRuleEntityModel> modifiedAttributeRules)
  {
    this.modifiedAttributeRules = modifiedAttributeRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetRuleTags.class)
  public List<ISmartDocumentPresetRuleTags> getAddedTagRules()
  {
    return addedTagRules;
  }
  
  @Override
  public void setAddedTagRules(List<ISmartDocumentPresetRuleTags> addedTagRules)
  {
    this.addedTagRules = addedTagRules;
  }
  
  @Override
  public List<String> getDeletedTagRules()
  {
    return deletedTagRules;
  }
  
  @Override
  public void setDeletedTagRules(List<String> deletedTagRules)
  {
    this.deletedTagRules = deletedTagRules;
  }
  
  @Override
  public List<IModifiedSmartDocumentPresetTagRuleEntityModel> getModifiedTagRules()
  {
    return modifiedTagRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedSmartDocumentPresetTagRuleEntityModel.class)
  public void setModifiedTagRules(
      List<IModifiedSmartDocumentPresetTagRuleEntityModel> modifiedTagRules)
  {
    this.modifiedTagRules = modifiedTagRules;
  }
}
