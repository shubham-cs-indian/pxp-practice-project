package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IRules extends IEntity {
  
  public static final String LABEL         = "label";
  public static final String ATTRIBUTES    = "attributes";
  public static final String TAGS          = "tags";
  public static final String ROLES         = "roles";
  public static final String RELATIONSHIPS = "relationships";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IGovernanceRuleIntermediateEntity> getAttributes();
  
  public void setAttributes(List<IGovernanceRuleIntermediateEntity> attributes);
  
  public List<IGovernanceRuleIntermediateEntity> getRelationships();
  
  public void setRelationships(List<IGovernanceRuleIntermediateEntity> relationships);
  
  public List<IGovernanceRuleIntermediateEntity> getRoles();
  
  public void setRoles(List<IGovernanceRuleIntermediateEntity> roles);
  
  public List<IGovernanceRuleTags> getTags();
  
  public void setTags(List<IGovernanceRuleTags> tags);
}
