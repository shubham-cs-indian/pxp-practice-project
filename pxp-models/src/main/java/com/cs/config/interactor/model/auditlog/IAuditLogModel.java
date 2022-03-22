package com.cs.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAuditLogModel extends IModel {
  
  public static final String ACTIVITY_ID = "activityId";
  public static final String DATE         = "date";
  public static final String USER_NAME    = "userName";
  public static final String IP_ADDRESS   = "ipAddress";
  public static final String ACTIVITY     = "activity";
  public static final String ENTITY_TYPE  = "entityType";
  public static final String ELEMENT      = "element";
  public static final String ELEMENT_TYPE = "elementType";
  public static final String ELEMENT_CODE = "elementCode";
  public static final String ELEMENT_NAME = "elementName";
  
  public String getActivityId();
  
  public void setActivityId(String activityId);
  
  public Long getDate();
  
  public void setDate(Long date);
  
  public String getUserName();
  
  public void setUserName(String userName);
  
  public String getIpAddress();
  
  public void setIpAddress(String ipAddress);
  
  public String getActivity();
  
  public void setActivity(String activity);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public String getElement();
  
  public void setElement(String element);
  
  public String getElementType();
  
  public void setElementType(String elementType);
  
  public String getElementCode();
  
  public void setElementCode(String elementCode);
  
  public String getElementName();
  
  public void setElementName(String elementName);
  
}
