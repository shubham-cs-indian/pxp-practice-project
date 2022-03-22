package com.cs.core.config.interactor.model.governancerule;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateKeyPerformanceIndexModel implements ICreateKeyPerformanceIndexModel {
  
  private static final long            serialVersionUID     = 1L;
  protected String                     id;
  protected String                     label;
  protected String                     frequency;
  protected List<IGovernanceRuleBlock> governanceRuleBlocks = new ArrayList<>();
  protected String                     code;
  
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
  public String getFrequency()
  {
    return frequency;
  }
  
  @Override
  public void setFrequency(String frequency)
  {
    this.frequency = frequency;
  }
  
  @Override
  public List<IGovernanceRuleBlock> getGovernanceRuleBlocks()
  {
    return governanceRuleBlocks;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleBlock.class)
  @Override
  public void setGovernanceRuleBlocks(List<IGovernanceRuleBlock> governanceRuleBlocks)
  {
    this.governanceRuleBlocks = governanceRuleBlocks;
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

  @JsonIgnore
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }

  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }

  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
