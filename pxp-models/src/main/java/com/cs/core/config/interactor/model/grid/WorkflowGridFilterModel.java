package com.cs.core.config.interactor.model.grid;

import java.util.List;
public class WorkflowGridFilterModel implements IWorkflowGridFilterModel{
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    workflowType;
  protected List<String>    eventType;
  protected List<Boolean>   activation;
  protected List<String>    physicalCatalogIds;
  protected List<String>    triggeringType;
  protected List<String>    timerDefinitionType;
  protected String          timerStartExpression;
  protected List<String>    organizationIds;

  @Override
  public List<String> getWorkflowType()
  {
    return workflowType;
  }
  
  @Override
  public void setWorkflowType(List<String> workflowType)
  {
    this.workflowType = workflowType;
  }
  
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
  
  public List<Boolean> getActivation()
  {
    return activation;
  }

  
  public void setActivation(List<Boolean> activation)
  {
    this.activation = activation;
  }

  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
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
  public List<String> getTimerDefinitionType()
  {
    return timerDefinitionType;
  }
  
  @Override
  public void setTimerDefinitionType(List<String> timerDefinitionType)
  {
    this.timerDefinitionType = timerDefinitionType;
  }

  @Override
  public String getTimerStartExpression()
  {
    return timerStartExpression;
  }

  @Override
  public void setTimerStartExpression(String timerStartExpression)
  {
    this.timerStartExpression = timerStartExpression;
  }
  
  @Override
  public List<String> getOrganizationIds()
  {
    return organizationIds;
  }

  @Override
  public void setOrganizationIds(List<String> organizationIds)
  {
    this.organizationIds = organizationIds;
  }
}
