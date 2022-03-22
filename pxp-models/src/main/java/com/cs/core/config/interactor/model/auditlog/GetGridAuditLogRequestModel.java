package com.cs.core.config.interactor.model.auditlog;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.instance.IdPaginationModel;

public class GetGridAuditLogRequestModel extends IdPaginationModel implements IGetGridAuditLogRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            from;
  protected Long            size;
  protected String          sortBy;
  protected String          sortOrder;
  
  protected String          activityId;
  protected String          elementName;
  protected String          elementCode;
  protected String          ipAddress;
  protected List<String>    userName;
  protected List<String>    activities;
  protected List<String>    entityTypes;
  protected List<String>    elements;
  protected List<String>    elementTypes;
  protected Long            toDate;
  protected Long            fromDate;
  
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public String getSortBy()
  {
    return sortBy;
  }
  
  @Override
  public void setSortBy(String sortBy)
  {
    this.sortBy = sortBy;
  }
  
  @Override
  public String getSortOrder()
  {
    return sortOrder;
  }
  
  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
  @Override
  public String getactivityId()
  {
    return activityId;
  }
  
  @Override
  public void setactivityId(String activityId)
  {
    this.activityId = activityId;
  }
  
  @Override
  public String getElementName()
  {
    return elementName;
  }
  
  @Override
  public void setElementName(String elementName)
  {
    this.elementName = elementName;
  }
  
  @Override
  public String getElementCode()
  {
    return elementCode;
  }
  
  @Override
  public void setElementCode(String elementCode)
  {
    this.elementCode = elementCode;
  }
  
  @Override
  public String getIpAddress()
  {
    return ipAddress;
  }
  
  @Override
  public void setIpAddress(String ipAddress)
  {
    this.ipAddress = ipAddress;
  }
  
  @Override
  public List<String> getUserName()
  {
    if (userName == null) {
      userName = new ArrayList<>();
    }
    return userName;
  }
  
  @Override
  public void setUserName(List<String> userName)
  {
    this.userName = userName;
  }
  
  @Override
  public List<String> getActivities()
  {
    if (activities == null) {
      activities = new ArrayList<>();
    }
    return activities;
  }
  
  @Override
  public void setActivities(List<String> activities)
  {
    this.activities = activities;
  }
  
  @Override
  public List<String> getEntityTypes()
  {
    if (entityTypes == null) {
      entityTypes = new ArrayList<>();
    }
    return entityTypes;
  }
  
  @Override
  public void setEntityTypes(List<String> entityTypes)
  {
    this.entityTypes = entityTypes;
  }
  
  @Override
  public List<String> getElements()
  {
    if (elements == null) {
      elements = new ArrayList<>();
    }
    return elements;
  }
  
  @Override
  public void setElements(List<String> elements)
  {
    this.elements = elements;
  }
  
  @Override
  public List<String> getElementTypes()
  {
    if (elementTypes == null) {
      elementTypes = new ArrayList<>();
    }
    return elementTypes;
  }
  
  @Override
  public void setElementTypes(List<String> elementTypes)
  {
    this.elementTypes = elementTypes;
  }
  
  @Override
  public Long getToDate()
  {
    return toDate;
  }
  
  @Override
  public void setToDate(Long toDate)
  {
    this.toDate = toDate;
  }
  
  @Override
  public Long getFromDate()
  {
    return fromDate;
  }
  
  @Override
  public void setFromDate(Long fromDate)
  {
    this.fromDate = fromDate;
  }
  
}
