package com.cs.core.config.interactor.model.rulelist;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleListInformationModel implements IRuleListInformationModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  protected IRuleList       ruleList;
  
  public RuleListInformationModel()
  {
    this.ruleList = new RuleList();
  }
  
  public RuleListInformationModel(IRuleList ruleList)
  {
    this.ruleList = ruleList;
  }
  
  @Override
  public String getCode()
  {
    return ruleList.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    this.ruleList.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.ruleList;
  }
  
  @Override
  public String getId()
  {
    return this.ruleList.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.ruleList.setId(id);
  }
  
  @Override
  public String getLabel()
  {
    return this.ruleList.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.ruleList.setLabel(label);
  }
  
  @Override
  public Long getVersionId()
  {
    return ruleList.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    ruleList.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return ruleList.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    ruleList.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return ruleList.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    ruleList.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
