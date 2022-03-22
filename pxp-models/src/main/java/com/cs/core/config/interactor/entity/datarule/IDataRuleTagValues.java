package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IDataRuleTagValues extends IEntity {
  
  static final String INNER_TAG_ID = "innerTagId";
  static final String TO           = "to";
  static final String FROM         = "from";
  
  public String getInnerTagId();
  
  public void setInnerTagId(String innerTagId);
  
  public Long getTo();
  
  public void setTo(Long to);
  
  public Long getFrom();
  
  public void setFrom(Long from);
}
