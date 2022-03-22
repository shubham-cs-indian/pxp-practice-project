package com.cs.core.config.interactor.model.processevent;

import java.util.List;
import java.util.Map;

public class GetProcessExportEndpointModel implements IGetProcessExportEndpointModel {
  
  private static final long  serialVersionUID = 1L;
  
  public List<String>        endpoints;
  public String              organization;
  public String              system;
  public Map<String, Object> endpointDetails;
  
  @Override
  public List<String> getEndpoints()
  {
    return endpoints;
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public String getOrganization()
  {
    return organization;
  }
  
  @Override
  public void setOrganization(String organization)
  {
    this.organization = organization;
  }
  
  @Override
  public String getSystem()
  {
    return system;
  }
  
  @Override
  public void setSystem(String system)
  {
    this.system = system;
  }
  
  @Override
  public Map<String, Object> getEndpointDetails()
  {
    return endpointDetails;
  }
  
  @Override
  public void setEndpointDetails(Map<String, Object> endpointDetails)
  {
    this.endpointDetails = endpointDetails;
  }
}
