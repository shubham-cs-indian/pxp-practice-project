package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAttributesAndTagsModel extends IModel {
  
  public static final String TAG_IDS       = "tagIds";
  public static final String ATTRIBUTE_IDS = "attributeIds";
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
}
