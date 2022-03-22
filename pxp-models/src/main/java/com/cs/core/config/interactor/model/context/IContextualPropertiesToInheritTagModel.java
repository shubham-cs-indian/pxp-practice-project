package com.cs.core.config.interactor.model.context;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;

import java.util.List;

public interface IContextualPropertiesToInheritTagModel
    extends IContextualPropertiesToInheritPropertyModel {
  
  public static final String TAG_VALUES = "tagValues";
  public static final String TAG_ID     = "tagId";
  
  public List<IIdRelevance> getTagValues();
  
  public void setTagValues(List<IIdRelevance> tagValues);
  
  public String getTagId();
  
  public void setTagId(String tagId);
}
