package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleEntityRule;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedGovernanceRuleEntityModel extends IModel {
  
  public static final String ID             = "id";
  public static final String ADDED_RULES    = "addedRules";
  public static final String MODIFIED_RULES = "modifiedRules";
  public static final String DELETED_RULES  = "deletedRules";
  
  public String getId();
  
  public void setId(String id);
  
  public List<IGovernanceRuleEntityRule> getAddedRules();
  
  public void setAddedRules(List<IGovernanceRuleEntityRule> addedRules);
  
  public List<IGovernanceRuleEntityRule> getModifiedRules();
  
  public void setModifiedRules(List<IGovernanceRuleEntityRule> modifiedRules);
  
  public List<String> getDeletedRules();
  
  public void setDeletedRules(List<String> deletedRules);
}
