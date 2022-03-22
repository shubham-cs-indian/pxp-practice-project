package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetEntityRule;
import com.cs.core.config.interactor.entity.smartdocument.preset.SmartDocumentPresetEntityRule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedSmartDocumentPresetRuleEntityModel
    implements IModifiedSmartDocumentPresetRuleEntityModel {
  
  private static final long                      serialVersionUID = 1L;
  protected String                               id;
  protected List<ISmartDocumentPresetEntityRule> addedRules       = new ArrayList<>();
  protected List<String>                         deletedRules     = new ArrayList<>();
  protected List<ISmartDocumentPresetEntityRule> modifiedRules    = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<ISmartDocumentPresetEntityRule> getAddedRules()
  {
    return addedRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetEntityRule.class)
  public void setAddedRules(List<ISmartDocumentPresetEntityRule> addedRules)
  {
    this.addedRules = addedRules;
  }
  
  @Override
  public List<ISmartDocumentPresetEntityRule> getModifiedRules()
  {
    return modifiedRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetEntityRule.class)
  public void setModifiedRules(List<ISmartDocumentPresetEntityRule> modifiedRules)
  {
    this.modifiedRules = modifiedRules;
  }
  
  @Override
  public List<String> getDeletedRules()
  {
    return deletedRules;
  }
  
  @Override
  public void setDeletedRules(List<String> deletedRules)
  {
    this.deletedRules = deletedRules;
  }
}
