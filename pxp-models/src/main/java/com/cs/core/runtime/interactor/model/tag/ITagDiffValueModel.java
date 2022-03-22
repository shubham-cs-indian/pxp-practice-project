package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITagDiffValueModel extends IModel {
  
  public static final String ID        = "id";
  public static final String RELEVANCE = "relevance";
  public static final String TAG       = "tag";
  
  public String getId();
  
  public void setId(String id);
  
  public String getRelevance();
  
  public void setRelevance(String relevance);
  
  public ITag getTag();
  
  public void setTag(ITag tag);
}
