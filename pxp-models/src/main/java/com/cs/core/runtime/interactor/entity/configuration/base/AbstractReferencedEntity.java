package com.cs.core.runtime.interactor.entity.configuration.base;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.cs.core.runtime.interactor.entity.propertyinstance.IReferencedEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/** @author Kshitij */
public abstract class AbstractReferencedEntity implements IReferencedEntity {
  
  private static final long                  serialVersionUID = 1L;
  
  protected String                           id;
  protected String                           label;
  protected String                           type;
  protected String                           image;
  protected String                           color;
  protected IEventInstanceLocation           location;
  protected TimeZone                         timezone;
  protected List<IEventInstanceNotification> notifications;
  protected IEventInstanceSchedule           eventSchedule;
  protected List<ITimeRange>                 calendarDates;
  protected List<IEventInstanceSchedule>     exclusions       = new ArrayList<>();
  protected List<IEventInstanceSchedule>     inclusions       = new ArrayList<>();
  
  protected String                           createdBy;
  protected Long                             createdOn;
  protected String                           jobId;
  protected Long                             versionId;
  protected Long                             versionTimestamp;
  protected String                           lastModifiedBy;
  protected List<ILinkedEntities>            linkedEntities;
  
  @Override
  public String getJobId()
  {
    return jobId;
  }
  
  @Override
  public void setJobId(String jobId)
  {
    this.jobId = jobId;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public IEventInstanceLocation getLocation()
  {
    return location;
  }
  
  @JsonDeserialize(as = EventInstanceLocation.class)
  @Override
  public void setLocation(IEventInstanceLocation location)
  {
    this.location = location;
  }
  
  @Override
  public TimeZone getTimezone()
  {
    return timezone;
  }
  
  @Override
  public void setTimezone(TimeZone timezone)
  {
    this.timezone = timezone;
  }
  
  @Override
  public List<IEventInstanceNotification> getNotifications()
  {
    return notifications;
  }
  
  @JsonDeserialize(contentAs = EventInstanceNotification.class)
  @Override
  public void setNotifications(List<IEventInstanceNotification> notifications)
  {
    this.notifications = notifications;
  }
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    return eventSchedule;
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    this.eventSchedule = schedule;
  }
  
  @Override
  public List<ITimeRange> getCalendarDates()
  {
    return calendarDates;
  }
  
  @JsonDeserialize(contentAs = TimeRange.class)
  @Override
  public void setCalendarDates(List<ITimeRange> flatDates)
  {
    this.calendarDates = flatDates;
  }
  
  @Override
  public List<IEventInstanceSchedule> getExclusions()
  {
    return exclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setExclusions(List<IEventInstanceSchedule> exclusions)
  {
    this.exclusions = exclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getInclusions()
  {
    return inclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setInclusions(List<IEventInstanceSchedule> inclusions)
  {
    this.inclusions = inclusions;
  }
  
  @Override
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
  }
  
  @Override
  public String getImage()
  {
    return image;
  }
  
  @Override
  public void setImage(String image)
  {
    this.image = image;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public List<ILinkedEntities> getLinkedEntities()
  {
    return linkedEntities;
  }
  
  @JsonDeserialize(contentAs = LinkedEntities.class)
  @Override
  public void setLinkedEntities(List<ILinkedEntities> linkedEntities)
  {
    this.linkedEntities = linkedEntities;
  }
}
