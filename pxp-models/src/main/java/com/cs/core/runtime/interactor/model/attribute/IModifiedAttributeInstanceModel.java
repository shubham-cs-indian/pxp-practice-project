package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.context.IModifiedContextInstanceModel;

public interface IModifiedAttributeInstanceModel
    extends IModifiedContentAttributeInstanceModel, IAttributeInstance, IRuntimeModel {
  
  public static final String SOURCE               = "source";
  public static final String IS_CONFLICT_RESOLVED = "isConflictResolved";
  public static final String MODIFIED_CONTEXT     = "modifiedContext";
  
  public IConflictingValueSource getSource();
  
  public void setSource(IConflictingValueSource source);
  
  public Boolean getIsConflictResolved();
  
  public void setIsConflictResolved(Boolean isConflictResolved);
  
  public IModifiedContextInstanceModel getModifiedContext();
  
  public void setModifiedContext(IModifiedContextInstanceModel modifiedContext);
}
