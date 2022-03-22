package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IGovernanceRuleTags extends IEntity {
  
  public static final String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public List<IGovernanceRuleTagRule> getRules();
  
  public void setRules(List<IGovernanceRuleTagRule> rules);
}
