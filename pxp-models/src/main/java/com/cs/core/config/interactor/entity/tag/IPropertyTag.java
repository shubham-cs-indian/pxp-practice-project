package com.cs.core.config.interactor.entity.tag;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IPropertyTag extends IEntity {
  
  public static final String TAG_ID     = "tagId";
  public static final String LABEL      = "label";
  public static final String TAG_VALUES = "tagValues";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IPropertyTagValues> getTagValues();
  
  public void setTagValues(List<IPropertyTagValues> tagValues);
}
