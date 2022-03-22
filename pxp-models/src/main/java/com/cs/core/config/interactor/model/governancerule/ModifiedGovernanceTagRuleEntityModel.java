package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleTagRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTagRule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedGovernanceTagRuleEntityModel implements IModifiedGovernanceTagRuleEntityModel {
  
  /**
   *
   */
  private static final long              serialVersionUID = 1L;
  
  protected String                       id;
  protected List<IGovernanceRuleTagRule> addedRules;
  protected List<IGovernanceRuleTagRule> modifiedRules;
  protected List<String>                 deletedRules;
  
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
  public List<IGovernanceRuleTagRule> getAddedRules()
  {
    if (addedRules == null) {
      addedRules = new ArrayList<>();
    }
    return addedRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleTagRule.class)
  @Override
  public void setAddedRules(List<IGovernanceRuleTagRule> addedRules)
  {
    this.addedRules = addedRules;
  }
  
  @Override
  public List<IGovernanceRuleTagRule> getModifiedRules()
  {
    if (modifiedRules == null) {
      modifiedRules = new ArrayList<>();
    }
    return modifiedRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleTagRule.class)
  @Override
  public void setModifiedRules(List<IGovernanceRuleTagRule> modifiedRules)
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
