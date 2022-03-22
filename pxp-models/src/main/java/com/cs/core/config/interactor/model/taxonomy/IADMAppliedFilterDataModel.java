package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.taxonomy.IAppliedFilterData;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

import java.util.List;

public interface IADMAppliedFilterDataModel extends IAppliedFilterData, IConfigModel {
  
  public static final String ADDED_TAGS         = "addedTags";
  public static final String DELETED_TAGS       = "deletedTags";
  public static final String ADDED_ATTRIBUTES   = "addedAttributes";
  public static final String DELETED_ATTRIBUTES = "deletedAttributes";
  
  public List<String> getAddedTags();
  
  public void setAddedTags(List<String> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<String> getAddedAttributes();
  
  public void setAddedAttributes(List<String> addedAttributes);
  
  public List<String> getDeletedAttributes();
  
  public void setDeletedAttributes(List<String> deletedAttributes);
}
