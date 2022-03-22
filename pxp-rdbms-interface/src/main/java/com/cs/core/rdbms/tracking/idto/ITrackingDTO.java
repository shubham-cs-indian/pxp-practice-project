package com.cs.core.rdbms.tracking.idto;

import com.cs.core.technical.rdbms.idto.IUserDTO;

/**
 * A DTO representing tracking information - the user ID - when - event (i.e. create, delete, modify)
 *
 * @author vallee
 */
public interface ITrackingDTO extends IUserDTO, Comparable {

  /**
   * @return the event
   */
  public TrackingEvent getEvent();

  ;
  
  /**
   * @return the when time
   */
  public long getWhen();

  /**
   * @return the timestamp of event creation (same as getWhen)
   */
  public default long getPostedTime() {
    return getWhen();
  }

  public enum TrackingEvent {

    UNDEFINED, CREATE, MODIFY, DELETE, CUSTOM;

    private static final TrackingEvent[] values = values();

    public static TrackingEvent valueOf(int ordinal) {
      return values[ordinal];
    }
  }
}
