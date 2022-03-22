package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IAssetAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public interface IModifiedAssetAttributeInstanceModel
    extends IAssetAttributeInstance, IRuntimeModel {
  
  public static final String ADDED_TAGS    = "addedTags";
  public static final String DELETED_TAGS  = "deletedTags";
  public static final String MODIFIED_TAGS = "modifiedTags";
  
  public List<ITagInstance> getAddedTags();
  
  public void setAddedTags(List<ITagInstance> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IModifiedContentTagInstanceModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags);
}
