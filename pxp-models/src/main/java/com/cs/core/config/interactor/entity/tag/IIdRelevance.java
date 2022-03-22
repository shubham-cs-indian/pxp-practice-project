package com.cs.core.config.interactor.entity.tag;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IIdRelevance extends IEntity {
  
  public static final String TAGID     = "tagId";
  public static final String CODE      = "code";
  public static final String RELEVANCE = "relevance";
  public static final String IID       = "iID";
  
  public Integer getRelevance();
  
  public void setRelevance(Integer relevance);
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public long getiID();
  
  public void setiID(long iID);
  
  public String getCode();
  
  public void setCode(String tagCode);
}
