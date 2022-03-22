package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;

import java.util.List;

public interface IModifiedContentAttributeInstanceModel
    extends IContentAttributeInstance, IRuntimeModel {
  
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
