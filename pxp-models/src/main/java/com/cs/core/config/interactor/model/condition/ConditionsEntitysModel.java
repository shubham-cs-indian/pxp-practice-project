package com.cs.core.config.interactor.model.condition;

import com.cs.core.config.interactor.entity.condition.AbstractConditionEntity;
import com.cs.core.config.interactor.entity.condition.IConditionEntity;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ConditionsEntitysModel implements IConditionsEntitysModel {
  
  private static final long        serialVersionUID = 1L;
  protected String                 id;
  protected Long                   versionId;
  protected Long                   versionTimestamp;
  protected String                 lastModifiedBy;
  protected String                 entityId;
  protected List<IConditionEntity> conditions;
  protected IEntity                entity;
  protected String                 code;
  
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
  public List<IConditionEntity> getConditions()
  {
    return conditions;
  }
  
  @JsonDeserialize(contentAs = AbstractConditionEntity.class)
  @Override
  public void setConditions(List<IConditionEntity> conditions)
  {
    this.conditions = conditions;
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
}
