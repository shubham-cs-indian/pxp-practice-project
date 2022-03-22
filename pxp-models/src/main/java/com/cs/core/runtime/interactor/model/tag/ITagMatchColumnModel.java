package com.cs.core.runtime.interactor.model.tag;

import java.util.List;

public interface ITagMatchColumnModel {
  
  public static final String TAG_ID     = "tagId";
  public static final String TAG_VALUES = "tagValues";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<ITagMatchValueModel> getTagValues();
  
  public void setTagValues(List<ITagMatchValueModel> tagValues);
}
