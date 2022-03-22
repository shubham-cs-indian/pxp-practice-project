package com.cs.core.runtime.interactor.entity.tag;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;

public interface ISearchableTagValue extends IRuntimeEntity {
  
  public static final String TAG_ID    = "tagId";
  public static final String RELEVANCE = "relevance";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public Integer getRelevance();
  
  public void setRelevance(Integer relevance);
}
