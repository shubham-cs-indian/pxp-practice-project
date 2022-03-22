package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IValueRecordNotificationDTO;

public class ValueRecordNotificationDTO implements IValueRecordNotificationDTO {
  
  private long   propertyIID = 0l;
  private String localeID    = "";
  private String value       = "";
  private String asHTML      = "";
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public void setPropertyIID(long propertyIID)
  {
    this.propertyIID = propertyIID;
  }
  
  @Override
  public String getLocaleID()
  {
    return localeID;
  }
  
  @Override
  public void setLocaleID(String localeID)
  {
    this.localeID = localeID;
  }
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public String getAsHTML()
  {
    return asHTML;
  }
  
  @Override
  public void setAsHTML(String html)
  {
    this.asHTML = html ;
  }
}
