package com.cs.core.rdbms.tracking.idto;

import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * DTO representation of object change tracking
 *
 * @author vallee
 */
public interface IObjectTrackingDTO extends ITrackingDTO {

  public static final String PXON_PAYLOAD = "pxon";

  /**
   * @return the RDBMS internal ID of this event (0 when not registered)
   */
  public long getTrackIID();

  /**
   * @return the object IID at the origin of this tracking information
   */
  public long getObjectIID();

  /**
   * @return the parent catalog information concerned by this tracking information
   */
  public ICatalogDTO getCatalog();

  /**
   * @return the parent object ID concerned by this tracking information
   */
  public String getObjectID();

  /**
   * @return the raw JSON information attached to this event
   */
  public IJSONContent getJSONTimelineData();

  /**
   * @return the information attached to this event
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public ITimelineDTO getTimelineData() throws CSFormatException;

  /**
   * @return the JSON extract of content change
   */
  public IJSONContent getJSONExtract();
}
