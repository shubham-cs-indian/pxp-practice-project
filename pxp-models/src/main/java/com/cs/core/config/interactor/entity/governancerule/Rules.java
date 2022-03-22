package com.cs.core.config.interactor.entity.governancerule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Rules implements IRules {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  id;
  protected String                                  label;
  protected List<IGovernanceRuleIntermediateEntity> attributes;
  protected List<IGovernanceRuleIntermediateEntity> relationships;
  protected List<IGovernanceRuleIntermediateEntity> roles;
  protected List<IGovernanceRuleTags>               tags;
  
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    return roles;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setRoles(List<IGovernanceRuleIntermediateEntity> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    return relationships;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setRelationships(List<IGovernanceRuleIntermediateEntity> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<IGovernanceRuleIntermediateEntity>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setAttributes(List<IGovernanceRuleIntermediateEntity> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IGovernanceRuleTags> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleTags.class)
  @Override
  public void setTags(List<IGovernanceRuleTags> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
