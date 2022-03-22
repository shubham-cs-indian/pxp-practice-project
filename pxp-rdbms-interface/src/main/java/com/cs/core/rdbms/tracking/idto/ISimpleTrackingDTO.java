package com.cs.core.rdbms.tracking.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * A DTO representing simple tracking information - the user name and when
 *
 * @author vallee
 */
public interface ISimpleTrackingDTO extends ISimpleDTO, Comparable {

  /**
   * @return the when time
   */
  public long getWhen();

  /**
   * @return who
   */
  public String getWho();

  public enum TrackingAttributes {
    lastmodifiedattribute, lastmodifiedbyattribute, createdbyattribute, createdonattribute;
  }

}
