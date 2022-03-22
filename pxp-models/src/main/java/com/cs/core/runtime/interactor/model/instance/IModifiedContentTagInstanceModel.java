package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

import java.util.List;

public interface IModifiedContentTagInstanceModel extends IContentTagInstance, IRuntimeModel {
  
  public static final String ADDED_TAG_VALUES    = "addedTagValues";
  public static final String DELETED_TAG_VALUES  = "deletedTagValues";
  public static final String MODIFIED_TAG_VALUES = "modifiedTagValues";
  public static final String ADDED_TAGS          = "addedTags";
  public static final String DELETED_TAGS        = "deletedTags";
  public static final String MODIFIED_TAGS       = "modifiedTags";
  
  public List<ITagInstanceValue> getAddedTagValues();
  
  public void setAddedTagValues(List<ITagInstanceValue> addedTagValues);
  
  public List<String> getDeletedTagValues();
  
  public void setDeletedTagValues(List<String> deletedTagValues);
  
  public List<ITagInstanceValue> getModifiedTagValues();
  
  public void setModifiedTagValues(List<ITagInstanceValue> modifiedTagValues);
  
  public List<IContentTagInstance> getAddedTags();
  
  public void setAddedTags(List<IContentTagInstance> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IModifiedContentTagInstanceModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags);
}
