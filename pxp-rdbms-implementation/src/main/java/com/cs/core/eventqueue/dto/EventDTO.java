package com.cs.core.eventqueue.dto;

import java.sql.SQLException;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.dto.TrackingDTO;
import com.cs.core.technical.exception.CSFormatException;

/**
 * DTO for PXP event information
 *
 * @author vallee
 */
public class EventDTO extends ObjectTrackingDTO implements IEventDTO {

  private static final long  serialVersionUID = 1L;
  public static final String EVENT_TYPE   = PXONTag.event.toReadOnlyTag();
  public static final String FLAGGED      = PXONTag.flag.toPrivateTag();
  public static final String CONSUMED     = PXONTag.read.toPrivateTag();
  private EventType          eventType    = EventType.UNDEFINED;
  private long               flaggedTime  = 0;
  private long               consumedTime = 0;
  private String             localeID     = IStandardConfig.GLOBAL_LOCALE;
  private String transactionId;
  /**
   * Enabled default constructor
   */
  public EventDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param tracking
   *          posted tracking information
   * @param catalog
   * @param objectID
   * @param type
   * @param objectIID
   */
  public EventDTO(TrackingDTO tracking, ICatalogDTO catalog, String objectID, long objectIID,
      EventType type)
  {
    super(tracking, catalog, objectID, objectIID);
    this.eventType = type;
    this.flaggedTime = 0;
    consumedTime = 0;
    localeID = IStandardConfig.GLOBAL_LOCALE;
  }
  
  @Override
  public void set(IResultSetParser rsParser) throws SQLException, CSFormatException
  {
    super.set(rsParser);
    eventType = EventType.valueOf(rsParser.getInt("eventType"));
    flaggedTime = rsParser.getLong("flagged");
    consumedTime = rsParser.getLong("consumed");
    localeID = rsParser.getString("localeid");
    transactionId = rsParser.getString("transactionId");
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    eventType = parser.getEnum(EventType.class, EVENT_TYPE);
    flaggedTime = parser.getLong(FLAGGED);
    consumedTime = parser.getLong(CONSUMED);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONField(EVENT_TYPE, eventType),
        JSONBuilder.newJSONField(FLAGGED, flaggedTime),
        JSONBuilder.newJSONField(CONSUMED, consumedTime));
  }
  
  @Override
  public EventType getType()
  {
    return eventType;
  }
  
  @Override
  public long getFlaggedTime()
  {
    return flaggedTime;
  }
  
  @Override
  public long getConsumedTime()
  {
    return consumedTime;
  }
  
  @Override
  public String getLocaleID()
  {
    return localeID;
  }

  @Override
  public String getTransactionId()
  {
    return transactionId;
  }

  /**
   * @param time
   *          overwritten consumed time
   */
  public void setConsumedTime(long time)
  {
    consumedTime = time;
  }
  
  @Override
  public int compareTo(Object t)
  {
    return (flaggedTime > ((EventDTO) t).flaggedTime ? 1
        : flaggedTime < ((EventDTO) t).flaggedTime ? -1 : 0);
  }
  
  @Override
  public String toString()
  {
    return String.format("[%s track:%d obj:%d]", this.eventType.toString(), getTrackIID(),
        getObjectIID());
  }
}
