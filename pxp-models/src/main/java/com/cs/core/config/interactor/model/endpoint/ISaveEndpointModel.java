package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveEndpointModel extends IModel, IEndpoint {
  
  public static final String ADDED_PROCESSES                = "addedProcesses";
  public static final String DELETED_PROCESSES              = "deletedProcesses";
  public static final String ADDED_JMS_PROCESSES            = "addedJmsProcesses";
  public static final String DELETED_JMS_PROCESSES          = "deletedJmsProcesses";
  public static final String ADDED_SYSTEM_ID                = "addedSystemId";
  public static final String DELETED_SYSTEM_ID              = "deletedSystemId";
  public static final String ADDED_DASHBOARD_TAB_ID         = "addedDashboardTabId";
  public static final String DELETED_DASHBOARD_TAB_ID       = "deletedDashboardTabId";
  public static final String ADDED_MAPPINGS                 = "addedMappings";
  public static final String DELETED_MAPPINGS               = "deletedMappings";
  public static final String ADDED_AUTHORIZATION_MAPPINGS   = "addedAuthorizationMappings";
  public static final String DELETED_AUTHORIZATION_MAPPINGS = "deletedAuthorizationMappings";
  
  public List<String> getAddedProcesses();
  
  public void setAddedProcesses(List<String> addedProcesses);
  
  public List<String> getDeletedProcesses();
  
  public void setDeletedProcesses(List<String> deletedProcesses);
  
  public List<String> getAddedJmsProcesses();
  
  public void setAddedJmsProcesses(List<String> addedJmsProcesses);
  
  public List<String> getDeletedJmsProcesses();
  
  public void setDeletedJmsProcesses(List<String> deletedJmsProcesses);
  
  public String getAddedSystemId();
  
  public void setAddedSystemId(String addedSystemId);
  
  public String getDeletedSystemId();
  
  public void setDeletedSystemId(String deletedSystemId);
  
  public String getAddedDashboardTabId();
  
  public void setAddedDashboardTabId(String addedDashboardTabId);
  
  public String getDeletedDashboardTabId();
  
  public void setDeletedDashboardTabId(String deletedDashboardTabId);
  
  public List<String> getAddedMappings();
  
  public void setAddedMappings(List<String> addedMappings);
  
  public List<String> getDeletedMappings();
  
  public void setDeletedMappings(List<String> deletedMappings);
  
  public List<String> getAddedAuthorizationMappings();
  
  public void setAddedAuthorizationMappings(List<String> addedAuthorizationMappings);
  
  public List<String> getDeletedAuthorizationMappings();
  
  public void setDeletedAuthorizationMappings(List<String> deletedAuthorizationMappings);
}
