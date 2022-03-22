package com.cs.core.config.interactor.model.auditlog;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetGridAuditLogRequestModel extends IModel {
  
  public static final String FROM          = "from";
  public static final String SIZE          = "size";
  public static final String SORT_BY       = "sortBy";
  public static final String SORT_ORDER    = "sortOrder";
  
  public static final String ACTIVITY_ID   = "activityId";
  public static final String ELEMENT_NAME  = "elementName";
  public static final String ELEMENT_CODE  = "elementCode";
  public static final String IP_ADDRESS    = "ipAddress";
  
  public static final String USER_NAME     = "userName";
  public static final String ACTIVITIES    = "activities";
  public static final String ENTITY_TYPES  = "entityTypes";
  public static final String ELEMENTS      = "elements";
  public static final String ELEMENT_TYPES = "elementTypes";
  
  public static final String TO_DATE       = "toDate";
  public static final String FROM_DATE     = "fromDate";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public String getactivityId();
  
  public void setactivityId(String activityId);
  
  public String getElementName();
  
  public void setElementName(String elementName);
  
  public String getElementCode();
  
  public void setElementCode(String elementCode);
  
  public String getIpAddress();
  
  public void setIpAddress(String ipAddress);
  
  public List<String> getUserName();
  
  public void setUserName(List<String> userName);
  
  public List<String> getActivities();
  
  public void setActivities(List<String> activities);
  
  public List<String> getEntityTypes();
  
  public void setEntityTypes(List<String> entityTypes);
  
  public List<String> getElements();
  
  public void setElements(List<String> elements);
  
  public List<String> getElementTypes();
  
  public void setElementTypes(List<String> elementTypes);
  
  public Long getToDate();
  
  public void setToDate(Long toDate);
  
  public Long getFromDate();
  
  public void setFromDate(Long fromDate);
  
}
