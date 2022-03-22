package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleEntityRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleEntityRule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedGovernanceRuleEntityModel implements IModifiedGovernanceRuleEntityModel {
  
  /**
   *
   */
  private static final long                 serialVersionUID = 1L;
  
  protected String                          id;
  protected List<IGovernanceRuleEntityRule> addedRules;
  protected List<IGovernanceRuleEntityRule> modifiedRules;
  protected List<String>                    deletedRules;
  
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
  public List<IGovernanceRuleEntityRule> getAddedRules()
  {
    if (addedRules == null) {
      addedRules = new ArrayList<>();
    }
    return addedRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleEntityRule.class)
  @Override
  public void setAddedRules(List<IGovernanceRuleEntityRule> addedRules)
  {
    this.addedRules = addedRules;
  }
  
  @Override
  public List<IGovernanceRuleEntityRule> getModifiedRules()
  {
    if (modifiedRules == null) {
      modifiedRules = new ArrayList<>();
    }
    return modifiedRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleEntityRule.class)
  @Override
  public void setModifiedRules(List<IGovernanceRuleEntityRule> modifiedRules)
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
