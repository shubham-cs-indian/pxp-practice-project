package com.cs.core.rdbms.tracking.dto;

import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author vallee
 */
public class SimpleTrackingDTO extends SimpleDTO implements ISimpleTrackingDTO {
  
  private static final String WHEN = PXONTag.when.toReadOnlyTag();
  private static final String WHO  = PXONTag.user.toReadOnlyTag();
  
  private long                when = System.currentTimeMillis();
  private String              who  = "";
  
  /**
   * Enabled default constructor
   */
  public SimpleTrackingDTO()
  {
  }
  
  /**
   * Value constructor with current time
   *
   * @param user
   *          the user name
   */
  public SimpleTrackingDTO(String user)
  {
    this.who = user;
    this.when = System.currentTimeMillis();
  }
  
  /**
   * Value constructor
   *
   * @param user
   * @param when
   */
  public SimpleTrackingDTO(String user, long when)
  {
    this.who = user;
    this.when = when;
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public SimpleTrackingDTO(SimpleTrackingDTO source)
  {
    this.who = source.who;
    this.when = source.when;
  }
  
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    when = parser.getLong(WHEN);
    who = parser.getString(WHO);
  }
  
  @Override
  public StringBuffer toJSONBuffer()
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(WHO, who, true),
        JSONBuilder.newJSONField(WHEN, when));
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
  public String getWho()
  {
    return who;
  }
  
  /**
   * @param userName
   *          overwritten user name
   */
  public void setWho(String userName)
  {
    this.who = userName;
  }
  
  /**
   * @return true when this DTO is not initialized
   */
  public boolean isNull()
  {
    return who.isEmpty();
  }
  
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(5, 11).append(when)
        .append(who)
        .build();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SimpleTrackingDTO other = (SimpleTrackingDTO) obj;
    if (this.when != other.when) {
      return false;
    }
    if (!Objects.equals(this.who, other.who)) {
      return false;
    }
    return true;
  }
  
  @Override
  public int compareTo(Object t)
  {
    SimpleTrackingDTO that = (SimpleTrackingDTO) t;
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    int compare = compareToBuilder.append(this.who, that.who).toComparison();
    return (compare == 0 ? compareToBuilder.append(this.when,that.when).toComparison() : compare);
  }
}
