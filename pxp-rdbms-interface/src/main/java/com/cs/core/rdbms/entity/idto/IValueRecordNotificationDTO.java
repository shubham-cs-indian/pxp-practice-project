package com.cs.core.rdbms.entity.idto;


public interface IValueRecordNotificationDTO {
  
  /**
   * @return the property RDBMS IID
   */
  public default long getIID() {
    return getPropertyIID();
  }

  /**
   * @return the property RDBMS IID
   */
  public long getPropertyIID();
  
  /**
   * @return the property RDBMS IID
   */
  public void setPropertyIID(long propertyIID);
  
  
  /**
   * @return the locale ID
   */
  public String getLocaleID();

  /**
   * @param localeID overwritten locale ID
   */
  public void setLocaleID(String localeID);
  
  /**
   * @return the value
   */
  public String getValue();

  /**
   * @param value overwritten value
   */
  
  public void setValue(String value);
  
  /**
   * @return the value as html
   */
  public String getAsHTML();

  /**
   * @param html overwritten as HTML
   */
  public void setAsHTML(String html);
  
}
