package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public abstract class AbstractTimelineInstanceSaveModel extends AbstractContentInstanceSaveModel
    implements ITimelineInstanceSaveModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected IEventInstanceSchedule       modifiedEventSchedule;
  
  protected List<IEventInstanceSchedule> addedInclusions;
  protected List<IEventInstanceSchedule> deletedInclusions;
  protected List<IEventInstanceSchedule> modifiedInclusions;
  
  protected List<IEventInstanceSchedule> addedExclusions;
  protected List<IEventInstanceSchedule> deletedExclusions;
  protected List<IEventInstanceSchedule> modifiedExclusions;
  
  @Override
  public IEventInstanceSchedule getModifiedEventSchedule()
  {
    return modifiedEventSchedule;
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setModifiedEventSchedule(IEventInstanceSchedule modifiedEventSchedule)
  {
    this.modifiedEventSchedule = modifiedEventSchedule;
  }
  
  @Override
  public List<IEventInstanceSchedule> getAddedInclusions()
  {
    return addedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions)
  {
    this.addedInclusions = addedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getDeletedInclusions()
  {
    return deletedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions)
  {
    this.deletedInclusions = deletedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getModifiedInclusions()
  {
    return modifiedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions)
  {
    this.modifiedInclusions = modifiedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getAddedExclusions()
  {
    return addedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions)
  {
    this.addedExclusions = addedExclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getDeletedExclusions()
  {
    return deletedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions)
  {
    this.deletedExclusions = deletedExclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getModifiedExclusions()
  {
    return modifiedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions)
  {
    this.modifiedExclusions = modifiedExclusions;
  }
}
