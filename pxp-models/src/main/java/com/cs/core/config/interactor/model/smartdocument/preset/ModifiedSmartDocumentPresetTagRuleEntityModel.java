package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.config.interactor.entity.smartdocument.preset.ISmartDocumentPresetTagRule;
import com.cs.core.config.interactor.entity.smartdocument.preset.SmartDocumentPresetTagRule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedSmartDocumentPresetTagRuleEntityModel
    implements IModifiedSmartDocumentPresetTagRuleEntityModel {
  
  private static final long                   serialVersionUID = 1L;
  protected String                            id;
  protected List<ISmartDocumentPresetTagRule> addedRules       = new ArrayList<>();
  protected List<String>                      deletedRules     = new ArrayList<>();
  protected List<ISmartDocumentPresetTagRule> modifiedRules    = new ArrayList<>();
  
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
  public List<String> getDeletedRules()
  {
    return deletedRules;
  }
  
  @Override
  public void setDeletedRules(List<String> deletedRules)
  {
    this.deletedRules = deletedRules;
  }
  
  @Override
  public List<ISmartDocumentPresetTagRule> getAddedRules()
  {
    return addedRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetTagRule.class)
  public void setAddedRules(List<ISmartDocumentPresetTagRule> addedRules)
  {
    this.addedRules = addedRules;
  }
  
  @Override
  public List<ISmartDocumentPresetTagRule> getModifiedRules()
  {
    return modifiedRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetTagRule.class)
  public void setModifiedRules(List<ISmartDocumentPresetTagRule> modifiedRules)
  {
    this.modifiedRules = modifiedRules;
  }
}
