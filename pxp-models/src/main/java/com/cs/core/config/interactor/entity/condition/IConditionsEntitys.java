package com.cs.core.config.interactor.entity.condition;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IConditionsEntitys extends IEntity {
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public List<IConditionEntity> getConditions();
  
  public void setConditions(List<IConditionEntity> Conditions);
}
