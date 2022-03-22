package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

public interface IModifiedTagInstanceModel
    extends IModifiedContentTagInstanceModel, ITagInstance, IRuntimeModel {
  
  public static final String SOURCE               = "source";
  public static final String IS_CONFLICT_RESOLVED = "isConflictResolved";
  
  public IConflictingValueSource getSource();
  
  public void setSource(IConflictingValueSource source);
  
  public Boolean getIsConflictResolved();
  
  public void setIsConflictResolved(Boolean isConflictResolved);
}
