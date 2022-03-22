package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.configuration.base.ITimelineInstance;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;

import java.util.List;

public interface ITimelineInstanceSaveModel extends ITimelineInstance, IContentInstanceSaveModel {
  
  public static final String MODIFIED_EVENT_SCHEDULE = "modifiedEventSchedule";
  
  public static final String ADDED_INCLUSIONS        = "addedInclusions";
  public static final String DELETED_INCLUSIONS      = "deletedInclusions";
  public static final String MODIFIED_INCLUSIONS     = "modifiedInclusions";
  
  public static final String ADDED_EXCLUSIONS        = "addedExclusions";
  public static final String DELETED_EXCLUSIONS      = "deletedExclusions";
  public static final String MODIFIED_EXCLUSIONS     = "modifiedExclusions";
  
  public IEventInstanceSchedule getModifiedEventSchedule();
  
  public void setModifiedEventSchedule(IEventInstanceSchedule modifiedEventSchedule);
  
  public List<IEventInstanceSchedule> getAddedInclusions();
  
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions);
  
  public List<IEventInstanceSchedule> getDeletedInclusions();
  
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions);
  
  public List<IEventInstanceSchedule> getModifiedInclusions();
  
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions);
  
  public List<IEventInstanceSchedule> getAddedExclusions();
  
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions);
  
  public List<IEventInstanceSchedule> getDeletedExclusions();
  
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions);
  
  public List<IEventInstanceSchedule> getModifiedExclusions();
  
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions);
}
