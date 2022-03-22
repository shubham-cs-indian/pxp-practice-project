package com.cs.core.config.interactor.entity.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IDefaultFilters extends IEntity {
  
  public static final String TAG_VALUES = "tagValues";
  public static final String TAG_ID     = "tagId";
  
  public List<IFilterTagValue> getTagValues();
  
  public void setTagValues(List<IFilterTagValue> tagValues);
  
  public String getTagId();
  
  public void setTagId(String tagId);
}
