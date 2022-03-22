package com.cs.core.config.interactor.entity.condition;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IConditionTagValue extends IEntity {
  
  public Integer getFrom();
  
  public void setFrom(Integer value);
  
  public Integer getTo();
  
  public void setTo(Integer value);
}
