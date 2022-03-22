package com.cs.core.runtime.interactor.entity.tag;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface ITagInstanceValue extends IEntity {
  
  public static final String TAG_ID    = "tagId";
  public static final String CODE      = "code";
  public static final String RELEVANCE = "relevance";
  public static final String TIMESTAMP = "timestamp";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public Integer getRelevance();
  
  public void setRelevance(Integer relevance);
  
  public String getTimestamp();
  
  public void setTimestamp(String timestamp);
  
  public String getCode();
  
  public void setCode(String tagCode);
}
