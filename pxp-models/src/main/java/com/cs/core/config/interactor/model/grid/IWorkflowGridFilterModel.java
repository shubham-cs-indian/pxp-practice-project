package com.cs.core.config.interactor.model.grid;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

  
public interface IWorkflowGridFilterModel extends IModel{
  
  public static final String WORKFLOW_TYPE         = "workflowType";
  public static final String EVENT_TYPE            = "eventType";
  public static final String ACTIVATION            = "activation";         // activation
  public static final String PHYSICAL_CATALOG_IDS  = "physicalCatalogIds";
  public static final String TRIGGERINGTYPE        = "triggeringType";     //
  public static final String TIMER_DEFINITION_TYPE = "timerDefinitionType";
  public static final String TIMER_START_EXPRESSION = "timerStartExpression";
  public static final String ORGANIZATION_IDS       = "organizationIds";
  
  public List<String> getWorkflowType();
  public void setWorkflowType(List<String> workflowType);
 
  public List<String> getEventType();
  public void setEventType(List<String> eventType);

  public List<Boolean> getActivation();
  public void setActivation(List<Boolean> activation);
 
  public List<String> getPhysicalCatalogIds();
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
 
  public List<String> getTriggeringType();
  public void setTriggeringType(List<String> triggeringType);
  
  public List<String> getTimerDefinitionType();
  public void setTimerDefinitionType(List<String> timerDefinitionType);
  
  public String getTimerStartExpression();
  public void setTimerStartExpression(String timerStartExpression);
 
  public List<String> getOrganizationIds();
  public void setOrganizationIds(List<String> organizationIds);
}
