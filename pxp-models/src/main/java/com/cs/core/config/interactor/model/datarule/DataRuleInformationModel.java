package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public class DataRuleInformationModel implements IDataRuleInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IDataRule       dataRule;
  
  public DataRuleInformationModel()
  {
    this.dataRule = new DataRule();
  }
  
  public DataRuleInformationModel(IDataRule dataRule)
  {
    this.dataRule = dataRule;
  }
  
  @Override
  public String getCode()
  {
    return dataRule.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    dataRule.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.dataRule;
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
  public Boolean getIsStandard()
  {
    return dataRule.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    dataRule.setIsStandard(isStandard);
  }
}
