package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IVariantContextModifiedTagsModel extends IModel {
  
  public static final String TAG_ID                = "tagId";
  public static final String ADDED_TAG_VALUE_IDS   = "addedTagValueIds";
  public static final String DELETED_TAG_VALUE_IDS = "deletedTagValueIds";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<String> getAddedTagValueIds();
  
  public void setAddedTagValueIds(List<String> addedTagValues);
  
  public List<String> getDeletedTagValueIds();
  
  public void setDeletedTagValueIds(List<String> deletedTagValues);
}
