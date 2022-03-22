package com.cs.core.config.interactor.entity.condition;

import java.util.List;

public class ConditionsEntitys implements IConditionsEntitys {
  
  protected String                 id;
  protected String                 entityId;
  protected String                 lastModifiedBy;
  protected Long                   versionId;
  protected Long                   versionTimestamp;
  protected List<IConditionEntity> conditions;
  
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
  public List<IConditionEntity> getConditions()
  {
    return conditions;
  }
  
  @Override
  public void setConditions(List<IConditionEntity> conditions)
  {
    this.conditions = conditions;
  }
}
