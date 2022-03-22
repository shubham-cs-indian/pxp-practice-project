package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

public class GetConfigDataWorkflowPaginationModel extends GetConfigDataEntityPaginationModel
implements IGetConfigDataWorkflowPaginationModel{

  private static final long serialVersionUID = 1L;
  protected List<String>        eventType;
  protected List<String>        triggeringType;
  protected String              physicalCatalogId;
  protected Boolean             isExecutable;
  protected Boolean             isTemplate;
  protected List<String>        workflowTypes;
  protected String              organizationId;

  @Override
  public List<String> getEventType()
  {
    return eventType;
  }

  @Override
  public void setEventType(List<String> eventType)
  {
    this.eventType = eventType;
  }

  @Override
  public List<String> getTriggeringType()
  {
    return triggeringType;
  }

  @Override
  public void setTriggeringType(List<String> triggeringType)
  {
    this.triggeringType = triggeringType;
  }

  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }

  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }

  @Override
  public Boolean getIsExecutable()
  {
    return isExecutable;
  }

  @Override
  public void setIsExecutable(Boolean isExecutable)
  {
    this.isExecutable = isExecutable;
  }
  
  @Override
  public Boolean getIsTemplate()
  {
    return isTemplate;
  }
  
  @Override
  public void setIsTemplate(Boolean isTemplate)
  {
    this.isTemplate = isTemplate;
  }

  @Override
  public List<String> getWorkflowTypes()
  {
    return workflowTypes;
  }

  @Override
  public void setWorkflowTypes(List<String> workflowTypes)
  {
    this.workflowTypes = workflowTypes;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }

  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
}
