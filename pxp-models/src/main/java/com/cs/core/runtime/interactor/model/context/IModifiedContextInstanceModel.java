package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedContextInstanceModel extends IModel {
  
  public static final String TIME_RANGE               = "timeRange";
  public static final String CONTEXT_ID               = "contextId";
  public static final String ADDED_LINKED_INSTANCES   = "addedLinkedInstances";
  public static final String DELETED_LINKED_INSTANCES = "deletedLinkedInstances";
  
  public String getId();
  
  public void setId(String id);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public List<IIdAndBaseType> getAddedLinkedInstances();
  
  public void setAddedLinkedInstances(List<IIdAndBaseType> addedLinkedInstances);
  
  public List<String> getDeletedLinkedInstances();
  
  public void setDeletedLinkedInstances(List<String> deletedLinkedInstances);
}
