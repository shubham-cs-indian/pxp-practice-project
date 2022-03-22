package com.cs.core.config.interactor.entity.governancerule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GovernanceRuleBlock implements IGovernanceRuleBlock {
  
  private static final long       serialVersionUID = 1L;
  
  protected String                id;
  protected String                label;
  protected Long                  versionId;
  protected Long                  versionTimestamp;
  protected String                lastModifiedBy;
  protected String                type;
  protected IThreshold            threshold;
  protected String                unit;
  protected List<IGovernanceRule> rules;
  protected String                task;
  protected String                code;
  
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public IThreshold getThreshold()
  {
    return threshold;
  }
  
  @JsonDeserialize(as = Threshold.class)
  @Override
  public void setThreshold(IThreshold threshold)
  {
    this.threshold = threshold;
  }
  
  @Override
  public String getUnit()
  {
    return unit;
  }
  
  @Override
  public void setUnit(String unit)
  {
    this.unit = unit;
  }
  
  @Override
  public List<IGovernanceRule> getRules()
  {
    if (rules == null) {
      rules = new ArrayList<>();
    }
    return rules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRule.class)
  @Override
  public void setRules(List<IGovernanceRule> rules)
  {
    this.rules = rules;
  }
  
  @Override
  public String getTask()
  {
    return task;
  }
  
  @Override
  public void setTask(String task)
  {
    this.task = task;
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
