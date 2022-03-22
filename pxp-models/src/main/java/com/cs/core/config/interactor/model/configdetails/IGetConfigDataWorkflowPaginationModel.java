package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

public interface IGetConfigDataWorkflowPaginationModel extends IGetConfigDataEntityPaginationModel {
  
  public static final String EVENT_TYPE          = "eventType";
  public static final String TRIGGERING_TYPE     = "triggeringType";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String IS_EXECUTABLE       = "isExecutable";
  public static final String IS_TEMPLATE         = "isTemplate";
  public static final String WORKFLOW_TYPES      = "workflowTypes";
  public static final String ORGANIZATION_ID     = "organizationId";
  
  public List<String> getEventType();
  
  public void setEventType(List<String> eventType);
  
  public List<String> getTriggeringType();
  
  public void setTriggeringType(List<String> triggeringType);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public Boolean getIsExecutable();
  
  public void setIsExecutable(Boolean isExecutable);
  
  public Boolean getIsTemplate();
  
  public void setIsTemplate(Boolean isTemplate);
  
  public List<String> getWorkflowTypes();
  
  public void setWorkflowTypes(List<String> workflowTypes);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
}
