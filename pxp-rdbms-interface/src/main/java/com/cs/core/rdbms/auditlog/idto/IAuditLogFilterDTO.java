package com.cs.core.rdbms.auditlog.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.IRootDTO;

public interface IAuditLogFilterDTO extends IRootDTO {
 
  /**
   * @return Searched Text in elementName filter
   */
  public String getElementName();
  
  /**
   * @return Searched Text in code filter
   */
  public String getElementCode();
  
  /**
   * @return Searched Text in ipAddress filter
   */
  public String getIpAddress();
  
  /**
   * @return Searched Text in activityId filter
   */
  public String getActivityIID();
  
  /**
   * @return List of selected user names
   */
  public List<String> getUserName();
  
  /**
   * @return List of selected type of activity
   */
  public List<String> getActivities();
  
  /**
   * @return List of selected type of entity
   */
  public List<String> getEntityTypes();
  
  /**
   * @return List of selected type of elements
   */
  public List<String> getElements();
  
  /**
   * @return List of selected element types
   */
  public List<String> getElementTypes();
  
  /**
   * @return from value for date  
   */
  public Long getFromDate();
  
  /**
   * @return to value for date
   */
  public Long getToDate();
}
