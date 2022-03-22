package com.cs.core.runtime.interactor.model.tag;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TagMatchColumnModel implements ITagMatchColumnModel {
  
  protected String                    tagId;
  protected List<ITagMatchValueModel> tagValues;
  
  @Override
  public String getTagId()
  {
    return tagId;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @Override
  public List<ITagMatchValueModel> getTagValues()
  {
    if (tagValues == null) {
      tagValues = new ArrayList<ITagMatchValueModel>();
    }
    return tagValues;
  }
  
  @JsonDeserialize(contentAs = TagMatchValueModel.class)
  @Override
  public void setTagValues(List<ITagMatchValueModel> tagValues)
  {
    this.tagValues = tagValues;
  }
}
