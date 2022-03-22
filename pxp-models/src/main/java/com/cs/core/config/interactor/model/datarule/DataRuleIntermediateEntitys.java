package com.cs.core.config.interactor.model.datarule;


import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleEntityRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleIntermediateEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataRuleIntermediateEntitys implements IDataRuleIntermediateEntitys {
  
  /**
   *
   */
  private static final long           serialVersionUID = 1L;
  
  protected String                    entityId;
  protected String                    id;
  protected List<IDataRuleEntityRule> rules;
  protected Long                      versionId;
  protected Long                      versionTimestamp;
  protected String                    lastModifiedBy;
  protected String                    code;
  
  public DataRuleIntermediateEntitys()
  {
    
  }
  
  public DataRuleIntermediateEntitys(IGovernanceRuleIntermediateEntity govRule)
  {
    this.entityId = govRule.getEntityId();        
    this.id = govRule.getId();              
    this.rules = govRule.getRules()
        .stream()
        .map(x -> new DataRuleEntityRule(x))
        .collect(Collectors.toList());
    
    this.code = govRule.getCode();
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
  public List<IDataRuleEntityRule> getRules()
  {
    if (rules == null) {
      rules = new ArrayList<>();
    }
    return rules;
  }
  
  @JsonDeserialize(contentAs = DataRuleEntityRule.class)
  @Override
  public void setRules(List<IDataRuleEntityRule> rules)
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
}
