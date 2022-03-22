package com.cs.core.runtime.interactor.entity.configuration.base;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractTimelineInstance extends AbstractRuntimeEntity
    implements ITimelineInstance {
  
  private static final long                  serialVersionUID = 1L;
  protected String                           name;
  protected String                           shortDescription;
  protected String                           longDescription;
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
    if(this.calendarDates == null) {
      this.calendarDates = new ArrayList<>();
    }
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
  public String getImage()
  {
    return image;
  }
  
  @Override
  public void setImage(String image)
  {
    this.image = image;
  }
}
