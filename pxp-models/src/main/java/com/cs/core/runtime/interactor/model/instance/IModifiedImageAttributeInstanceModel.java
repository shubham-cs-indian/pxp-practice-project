package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

public interface IModifiedImageAttributeInstanceModel
    extends IModifiedContentAttributeInstanceModel, IImageAttributeInstance, IRuntimeModel {
  
  public static final String SOURCE_ID            = "sourceId";
  public static final String IS_CONFLICT_RESOLVED = "isConflictResolved";
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public Boolean getIsConflictResolved();
  
  public void setIsConflictResolved(Boolean isConflictResolved);
}
