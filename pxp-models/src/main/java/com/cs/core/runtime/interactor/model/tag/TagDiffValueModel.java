package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TagDiffValueModel implements ITagDiffValueModel {
  
  protected String id;
  protected String relevance;
  protected ITag   tag;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getRelevance()
  {
    return relevance;
  }
  
  @Override
  public void setRelevance(String relevance)
  {
    this.relevance = relevance;
  }
  
  @JsonDeserialize(as = Tag.class)
  @Override
  public ITag getTag()
  {
    return tag;
  }
  
  @Override
  public void setTag(ITag tag)
  {
    this.tag = tag;
  }
}
