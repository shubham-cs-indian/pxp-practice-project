package com.cs.core.config.interactor.model.datarule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedRuleEntityModel implements IModifiedRuleEntityModel {
  
  /**
   *
   */
  private static final long           serialVersionUID = 1L;
  
  protected String                    id;
  protected List<IDataRuleEntityRule> addedRules;
  protected List<IDataRuleEntityRule> modifiedRules;
  protected List<String>              deletedRules;
  
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
  public List<IDataRuleEntityRule> getAddedRules()
  {
    if (addedRules == null) {
      addedRules = new ArrayList<>();
    }
    return addedRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleEntityRule.class)
  @Override
  public void setAddedRules(List<IDataRuleEntityRule> addedRules)
  {
    this.addedRules = addedRules;
  }
  
  @Override
  public List<IDataRuleEntityRule> getModifiedRules()
  {
    if (modifiedRules == null) {
      modifiedRules = new ArrayList<>();
    }
    return modifiedRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleEntityRule.class)
  @Override
  public void setModifiedRules(List<IDataRuleEntityRule> modifiedRules)
  {
    this.modifiedRules = modifiedRules;
  }
  
  @Override
  public List<String> getDeletedRules()
  {
    if (deletedRules == null) {
      deletedRules = new ArrayList<>();
    }
    return deletedRules;
  }
  
  @Override
  public void setDeletedRules(List<String> deletedRules)
  {
    this.deletedRules = deletedRules;
  }
}
