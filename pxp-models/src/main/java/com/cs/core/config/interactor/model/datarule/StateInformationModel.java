package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.state.IStateInformationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateInformationModel implements IStateInformationModel {
  
  protected IDataRule dataRule;
  
  public StateInformationModel()
  {
    this.dataRule = new DataRule();
  }
  
  public StateInformationModel(IDataRule dataRule)
  {
    this.dataRule = dataRule;
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.dataRule;
  }
  
  @Override
  public String getCode()
  {
    return dataRule.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    this.dataRule.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return this.dataRule.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.dataRule.setId(id);
  }
  
  @Override
  public String getLabel()
  {
    return this.dataRule.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.dataRule.setLabel(label);
  }
  
  @Override
  public Long getVersionId()
  {
    return dataRule.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    dataRule.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return dataRule.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    dataRule.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return dataRule.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    dataRule.setLastModifiedBy(lastModifiedBy);
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
