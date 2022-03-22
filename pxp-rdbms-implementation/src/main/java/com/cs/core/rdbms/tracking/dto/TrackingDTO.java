package com.cs.core.rdbms.tracking.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import java.sql.SQLException;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * DTO for tracking event
 *
 * @author vallee
 */
public class TrackingDTO extends RDBMSRootDTO implements ITrackingDTO {
  
  private static final String USER  = PXONTag.user.toReadOnlyCSETag();
  private static final String EVENT = PXONTag.event.toReadOnlyTag();
  private static final String WHEN  = PXONTag.when.toReadOnlyTag();
  
  private long                when  = 0;
  private TrackingEvent       event = TrackingEvent.UNDEFINED;
  private UserDTO             user  = new UserDTO();
  
  /**
   * Enabled default constructor
   */
  public TrackingDTO()
  {
  }
  
  /**
   * Value constructor with current time
   *
   * @param user
   * @param event
   */
  public TrackingDTO(UserDTO user, TrackingEvent event)
  {
    this.event = event;
    this.when = System.currentTimeMillis();
    this.user = user;
  }
  
  /**
   * Value constructor
   *
   * @param user
   * @param when
   * @param event
   */
  public TrackingDTO(UserDTO user, long when, TrackingEvent event)
  {
    this.event = event;
    this.when = when;
    this.user = user;
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public TrackingDTO(TrackingDTO source)
  {
    this.event = source.event;
    this.when = source.when;
    this.user = source.user;
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Tracking);
    cse.setIID(getIID());
    return cse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject ocse = (CSEObject) cse;
    setIID(ocse.getIID());
  }
  
  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException
  {
    super.fromPXON(parser);
    user.fromCSExpressID(parser.getCSEElement(USER));
    when = parser.getLong(WHEN);
    event = parser.getEnum(TrackingEvent.class, EVENT);
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
        JSONBuilder.newJSONField(USER, user.toCSExpressID()), JSONBuilder.newJSONField(WHEN, when),
        JSONBuilder.newJSONField(EVENT, event));
  }
  
  /**
   * Constructor from a query result
   *
   * @param rsParser
   *          parser of
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void set(IResultSetParser rsParser) throws SQLException, CSFormatException
  {
    setIID(rsParser.getLong("trackIID"));
    user = new UserDTO(rsParser);
    when = rsParser.getLong("posted");
    event = TrackingEvent.valueOf(rsParser.getInt("event"));
  }
  
  @Override
  public TrackingEvent getEvent()
  {
    return event;
  }
  
  /**
   * @param event
   *          overwritten event
   */
  public void setEvent(ITrackingDTO.TrackingEvent event)
  {
    this.event = event;
  }
  
  @Override
  public long getWhen()
  {
    return when;
  }
  
  /**
   * @param time
   *          overwritten when time
   */
  public void setWhen(long time)
  {
    this.when = time;
  }
  
  @Override
  public String getUserName()
  {
    return user.getUserName();
  }
  
  @Override
  public long getUserIID()
  {
    return user.getIID();
  }
  
  @Override
  public int compareTo(Object t)
  {
    int comparison = (when > ((TrackingDTO) t).when ? 1 : -1);
    if (comparison != 0) {
      return comparison;
    }
    return new CompareToBuilder().append(user.getUserName(),
        ((TrackingDTO) t).user.getUserName()).toComparison();
  }
}
