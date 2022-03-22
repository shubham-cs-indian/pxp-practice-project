package com.cs.core.config.interactor.entity.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IFilterTagValue extends IEntity {
  
  public static final String FROM   = "from";
  public static final String TO     = "to";
  public static final String TYPE   = "type";
  public static final String TAG_ID = "tagId";
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getTo();
  
  public void setTo(Integer to);
  
  public String getType();
  
  public void setType(String type);
  
  public String getTagId();
  
  public void setTagId(String tagId);
}
