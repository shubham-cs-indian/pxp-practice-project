package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAddedVariantContextTagsModel extends IModel {
  
  public static final String TAG_ID        = "tagId";
  public static final String TAG_VALUE_IDS = "tagValueIds";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<String> getTagValueIds();
  
  public void setTagValueIds(List<String> tagValueIds);
}
