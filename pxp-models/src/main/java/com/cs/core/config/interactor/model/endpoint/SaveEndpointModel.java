package com.cs.core.config.interactor.model.endpoint;

import java.util.ArrayList;
import java.util.List;

public class SaveEndpointModel extends EndpointModel implements ISaveEndpointModel {
  
  private static final long serialVersionUID             = 1L;
  protected List<String>    addedProcesses;
  protected List<String>    deletedProcesses;
  protected List<String>    addedJmsProcesses            = new ArrayList<>();
  protected List<String>    deletedJmsProcesses          = new ArrayList<>();;
  protected String          addedSystemId;
  protected String          deletedSystemId;
  protected String          addedDashboardTabId;
  protected String          deletedDashboardTabId;
  protected List<String>    addedMappings                = new ArrayList<>();
  protected List<String>    deletedMappings              = new ArrayList<>();
  protected List<String>    addedAuthorizationMappings   = new ArrayList<>();
  protected List<String>    deletedAuthorizationMappings = new ArrayList<>();
  
  @Override
  public List<String> getAddedProcesses()
  {
    return addedProcesses;
  }
  
  @Override
  public void setAddedProcesses(List<String> addedProcesses)
  {
    this.addedProcesses = addedProcesses;
  }
  
  @Override
  public List<String> getDeletedProcesses()
  {
    return deletedProcesses;
  }
  
  
  @Override
  public List<String> getAddedJmsProcesses()
  {
    return addedJmsProcesses;
  }

  @Override
  public void setAddedJmsProcesses(List<String> addedJmsProcesses)
  {
    this.addedJmsProcesses = addedJmsProcesses;
  }

  @Override
  public List<String> getDeletedJmsProcesses()
  {
    return deletedJmsProcesses;
  }

  @Override
  public void setDeletedJmsProcesses(List<String> deletedJmsProcesses)
  {
    this.deletedJmsProcesses = deletedJmsProcesses;
  }

  @Override
  public void setDeletedProcesses(List<String> deletedProcesses)
  {
    this.deletedProcesses = deletedProcesses;
  }
  
  @Override
  public String getAddedSystemId()
  {
    return addedSystemId;
  }
  
  @Override
  public void setAddedSystemId(String addedSystemId)
  {
    this.addedSystemId = addedSystemId;
  }
  
  @Override
  public String getDeletedSystemId()
  {
    return deletedSystemId;
  }
  
  @Override
  public void setDeletedSystemId(String deletedSystemId)
  {
    this.deletedSystemId = deletedSystemId;
  }
  
  @Override
  public String getAddedDashboardTabId()
  {
    return addedDashboardTabId;
  }
  
  @Override
  public void setAddedDashboardTabId(String addedDashboardTabId)
  {
    this.addedDashboardTabId = addedDashboardTabId;
  }
  
  @Override
  public String getDeletedDashboardTabId()
  {
    return deletedDashboardTabId;
  }
  
  @Override
  public void setDeletedDashboardTabId(String deletedDashboardTabId)
  {
    this.deletedDashboardTabId = deletedDashboardTabId;
  }
 
  @Override
  public List<String> getDeletedMappings()
  {
    return deletedMappings;
  }

  @Override
  public void setDeletedMappings(List<String> deletedMappings)
  {
    this.deletedMappings = deletedMappings;
  }
  
  @Override
  public List<String> getAddedMappings()
  {
    return addedMappings;
  }

  @Override
  public void setAddedMappings(List<String> addedMappings)
  {
    this.addedMappings = addedMappings;
  }
  
  @Override
  public List<String> getAddedAuthorizationMappings()
  {
    return addedAuthorizationMappings;
  }
  
  @Override
  public void setAddedAuthorizationMappings(List<String> addedAuthorizationMappings)
  {
    this.addedAuthorizationMappings = addedAuthorizationMappings;
  }
  
  @Override
  public List<String> getDeletedAuthorizationMappings()
  {
    return deletedAuthorizationMappings;
  }
  
  @Override
  public void setDeletedAuthorizationMappings(List<String> deletedAuthorizationMappings)
  {
    this.deletedAuthorizationMappings = deletedAuthorizationMappings;
  }
}
