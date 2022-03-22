package com.cs.core.runtime.interactor.entity.timerange;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IInstanceTimeRange extends IEntity {
  
  public static final String FROM = "from";
  public static final String TO   = "to";
  
  public void setFrom(Long from);
  
  public Long getFrom();
  
  public void setTo(Long to);
  
  public Long getTo();
}
