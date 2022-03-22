package com.cs.core.config.interactor.entity.governancerule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GovernanceRuleIntermediateEntity implements IGovernanceRuleIntermediateEntity {
  
  /**
   *
   */
  private static final long                 serialVersionUID = 1L;
  
  protected String                          entityId;
  protected String                          id;
  protected List<IGovernanceRuleEntityRule> rules;
  protected Long                            versionId;
  protected Long                            versionTimestamp;
  protected String                          lastModifiedBy;
  protected String                          code;
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public List<IGovernanceRuleEntityRule> getRules()
  {
    if (rules == null) {
      rules = new ArrayList<>();
    }
    return rules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleEntityRule.class)
  @Override
  public void setRules(List<IGovernanceRuleEntityRule> rules)
  {
    this.rules = rules;
  }
  
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
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
}
