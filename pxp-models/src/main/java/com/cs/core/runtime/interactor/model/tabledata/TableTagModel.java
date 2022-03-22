package com.cs.core.runtime.interactor.model.tabledata;

import com.cs.core.runtime.interactor.model.tag.ITagMatchValueModel;
import com.cs.core.runtime.interactor.model.tag.TagMatchValueModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableTagModel implements ITableTagModel {
  
  protected String                           id;
  protected String                           tagId;
  protected Map<String, Map<String, Object>> klassInstanceValues;
  protected List<ITagMatchValueModel>        tagValues;
  
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
  public Map<String, Map<String, Object>> getKlassInstanceValues()
  {
    return klassInstanceValues;
  }
  
  @Override
  public void setKlassInstanceValues(Map<String, Map<String, Object>> klassInstanceValues)
  {
    this.klassInstanceValues = klassInstanceValues;
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
  public void setTagValues(List<ITagMatchValueModel> attributeMatchValueModels)
  {
    this.tagValues = attributeMatchValueModels;
  }
}
