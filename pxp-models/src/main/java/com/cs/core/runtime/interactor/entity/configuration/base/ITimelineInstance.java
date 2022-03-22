package com.cs.core.runtime.interactor.entity.configuration.base;

import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import java.util.List;
import java.util.TimeZone;

/**
 * This interface is extended by IPromotionInstance,
 * IReferencedEntity, IAssetInstance
 *
 * @author Kshitij
 */
public interface ITimelineInstance extends IRuntimeEntity {
  
  public static final String CALENDAR_DATES = "calendarDates";
  public static final String COLOR          = "color";
  public static final String EVENT_SCHEDULE = "eventSchedule";
  public static final String EXCLUSIONS     = "exclusions";
  public static final String IMAGE          = "image";
  public static final String INCLUSIONS     = "inclusions";
  public static final String LOCATION       = "location";
  public static final String NOTIFICATIONS  = "notifications";
  public static final String TIMEZONE       = "timezone";
  
  public void setCalendarDates(List<ITimeRange> dates);
  
  public List<ITimeRange> getCalendarDates();
  
  public void setColor(String color);
  
  public String getColor();
  
  public void setEventSchedule(IEventInstanceSchedule schedule);
  
  public IEventInstanceSchedule getEventSchedule();
  
  public void setExclusions(List<IEventInstanceSchedule> exclusions);
  
  public List<IEventInstanceSchedule> getExclusions();
  
  public String getImage();
  
  public void setImage(String image);
  
  public void setInclusions(List<IEventInstanceSchedule> inclusions);
  
  public List<IEventInstanceSchedule> getInclusions();
  
  public void setLocation(IEventInstanceLocation location);
  
  public IEventInstanceLocation getLocation();
  
  public void setNotifications(List<IEventInstanceNotification> notifications);
  
  public List<IEventInstanceNotification> getNotifications();
  
  public void setTimezone(TimeZone timezone);
  
  public TimeZone getTimezone();
}
