package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractKlassInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EventInstance extends AbstractKlassInstance implements IEventInstance {
  
  private static final long                  serialVersionUID = 1L;
  
  /*Event specific fields*/
  protected List<ILinkedEntities>            linkedEntities;
  
  /* Fields from AbstractTimelineInstance   */
  protected String                           shortDescription;
  protected String                           longDescription;
  protected String                           image;
  protected String                           color;
  protected IEventInstanceLocation           location;
  protected TimeZone                         timezone;
  protected List<IEventInstanceNotification> notifications;
  protected IEventInstanceSchedule           eventSchedule;
  protected List<ITimeRange>                 calendarDates;
  protected List<IEventInstanceSchedule>     exclusions       = new ArrayList<>();
  protected List<IEventInstanceSchedule>     inclusions       = new ArrayList<>();
  
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
  public String getShortDescription()
  {
    return shortDescription;
  }
  
  @Override
  public void setShortDescription(String shortDescription)
  {
    this.shortDescription = shortDescription;
  }
  
  @Override
  public String getLongDescription()
  {
    return longDescription;
  }
  
  @Override
  public void setLongDescription(String longDescription)
  {
    this.longDescription = longDescription;
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
