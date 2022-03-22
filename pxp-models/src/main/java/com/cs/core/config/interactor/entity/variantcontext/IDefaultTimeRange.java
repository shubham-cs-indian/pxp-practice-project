package com.cs.core.config.interactor.entity.variantcontext;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IDefaultTimeRange extends IConfigEntity {
  
  public static final String FROM            = "from";
  public static final String TO              = "to";
  public static final String IS_CURRENT_TIME = "isCurrentTime";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getTo();
  
  public void setTo(Long to);
  
  public Boolean getIsCurrentTime();
  
  public void setIsCurrentTime(Boolean isCurrentTime);
}
