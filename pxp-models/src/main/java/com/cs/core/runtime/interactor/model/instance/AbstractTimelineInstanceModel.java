package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.configuration.base.ITimelineInstance;
import com.cs.core.runtime.interactor.entity.eventinstance.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.TimeZone;

public abstract class AbstractTimelineInstanceModel extends AbstractContentInstanceModel
    implements ITimelineInstance {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public String getColor()
  {
    return ((ITimelineInstance) entity).getColor();
  }
  
  @Override
  public void setColor(String color)
  {
    ((ITimelineInstance) entity).setColor(color);
  }
  
  @Override
  public IEventInstanceLocation getLocation()
  {
    return ((ITimelineInstance) entity).getLocation();
  }
  
  @JsonDeserialize(as = EventInstanceLocation.class)
  @Override
  public void setLocation(IEventInstanceLocation location)
  {
    ((ITimelineInstance) entity).setLocation(location);
  }
  
  @Override
  public TimeZone getTimezone()
  {
    return ((ITimelineInstance) entity).getTimezone();
  }
  
  @Override
  public void setTimezone(TimeZone timezone)
  {
    ((ITimelineInstance) entity).setTimezone(timezone);
    ;
  }
  
  @Override
  public List<IEventInstanceNotification> getNotifications()
  {
    return ((ITimelineInstance) entity).getNotifications();
  }
  
  @JsonDeserialize(contentAs = EventInstanceNotification.class)
  @Override
  public void setNotifications(List<IEventInstanceNotification> notifications)
  {
    ((ITimelineInstance) entity).setNotifications(notifications);
  }
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    return ((ITimelineInstance) entity).getEventSchedule();
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    ((ITimelineInstance) entity).setEventSchedule(schedule);
  }
  
  @Override
  public List<ITimeRange> getCalendarDates()
  {
    return ((ITimelineInstance) entity).getCalendarDates();
  }
  
  @JsonDeserialize(contentAs = TimeRange.class)
  @Override
  public void setCalendarDates(List<ITimeRange> flatDates)
  {
    ((ITimelineInstance) entity).setCalendarDates(flatDates);
  }
  
  @Override
  public List<IEventInstanceSchedule> getExclusions()
  {
    return ((ITimelineInstance) entity).getExclusions();
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setExclusions(List<IEventInstanceSchedule> exclusions)
  {
    ((ITimelineInstance) entity).setExclusions(exclusions);
  }
  
  @Override
  public List<IEventInstanceSchedule> getInclusions()
  {
    return ((ITimelineInstance) entity).getInclusions();
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setInclusions(List<IEventInstanceSchedule> inclusions)
  {
    ((ITimelineInstance) entity).setInclusions(inclusions);
  }
  
  /*
  @Override
  public String getShortDescription()
  {
    return ((ITimelineInstance) entity).getShortDescription();
  }
  
  @Override
  public void setShortDescription(String shortDescription)
  {
    ((ITimelineInstance) entity).setShortDescription(shortDescription);
  }
  
  @Override
  public String getLongDescription()
  {
    return ((ITimelineInstance) entity).getLongDescription();
  }
  
  @Override
  public void setLongDescription(String longDescription)
  {
    ((ITimelineInstance) entity).setLongDescription(longDescription);
  }
  */
  @Override
  public String getImage()
  {
    return ((ITimelineInstance) entity).getImage();
  }
  
  @Override
  public void setImage(String image)
  {
    ((ITimelineInstance) entity).setImage(image);
  }
}
