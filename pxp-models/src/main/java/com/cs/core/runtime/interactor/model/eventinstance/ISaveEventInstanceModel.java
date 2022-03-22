package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;

import java.util.List;

public interface ISaveEventInstanceModel extends IEventInstanceModel {
  
  public static final String ADDED_INCLUSIONS         = "addedInclusions";
  public static final String DELETED_INCLUSIONS       = "deletedInclusions";
  public static final String ADDED_EXCLUSIONS         = "addedExclusions";
  public static final String DELETED_EXCLUSIONS       = "deletedExclusions";
  public static final String MODIFIED_INCLUSIONS      = "modifiedInclusions";
  public static final String MODIFIED_EXCLUSIONS      = "modifiedExclusions";
  public static final String ADDED_LINKED_ENTITIES    = "addedLinkedEntities";
  public static final String DELETED_LINKED_ENTITIES  = "deletedLinkedEntities";
  public static final String MODIFIED_LINKED_ENTITIES = "modifiedLinkedEntities";
  
  public List<IEventInstanceSchedule> getAddedInclusions();
  
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions);
  
  public List<IEventInstanceSchedule> getDeletedInclusions();
  
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions);
  
  public List<IEventInstanceSchedule> getAddedExclusions();
  
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions);
  
  public List<IEventInstanceSchedule> getDeletedExclusions();
  
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions);
  
  public List<IEventInstanceSchedule> getModifiedInclusions();
  
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions);
  
  public List<IEventInstanceSchedule> getModifiedExclusions();
  
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions);
  
  public List<ILinkedEntities> getAddedLinkedEntities();
  
  public void setAddedLinkedEntities(List<ILinkedEntities> addedLinkedEntities);
  
  public List<String> getDeletedLinkedEntities();
  
  public void setDeletedLinkedEntities(List<String> deletedLinkedEntities);
  
  public List<ILinkedEntities> getModifiedLinkedEntities();
  
  public void setModifiedLinkedEntities(List<ILinkedEntities> modifiedLinkedEntities);
}
