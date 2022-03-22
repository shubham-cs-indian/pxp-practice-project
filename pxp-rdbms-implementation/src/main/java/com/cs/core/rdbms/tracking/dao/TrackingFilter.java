package com.cs.core.rdbms.tracking.dao;

/**
 * TrackingFilter Singleton class to access the tracking filter information.
 *
 * <p>
 * Use {getTrackingFilterInstance()} to get singleton object of this class.
 *
 * @author Pankaj Gajjar
 * @company Contentserv Inc.
 */
public class TrackingFilter /*implements IObjectTrackingDAO*/ {
  
  /**
   * Keep final static for singleton- object initialise of {TrackingFilter}
   */
  private static final TrackingFilter trackingFilter = new TrackingFilter();
  
  /**
   * Keep private for singleton
   */
  private TrackingFilter()
  {
    // TODO Auto-generated constructor stub
  }
  
  /**
   * Get SingleTon Instance of {TrackingFilter} class
   *
   * @return {TrackingFilter}
   */
  public static TrackingFilter getTrackingFilterInstance()
  {
    return trackingFilter;
  }
}
