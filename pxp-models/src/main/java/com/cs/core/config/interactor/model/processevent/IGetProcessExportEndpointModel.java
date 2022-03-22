package com.cs.core.config.interactor.model.processevent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetProcessExportEndpointModel extends IModel {
  
  public static final String ENDPOINTS        = "endpoints";
  public static final String ORGANIZATION     = "organization";
  public static final String SYSTEM           = "system";
  public static final String ENDPOINT_DETAILS = "endpointDetails";
  
  public List<String> getEndpoints();
  
  public void setEndpoints(List<String> endpoints);
  
  public String getSystem();
  
  public void setSystem(String system);
  
  public String getOrganization();
  
  public void setOrganization(String organization);
  
  public Map<String, Object> getEndpointDetails();
  
  public void setEndpointDetails(Map<String, Object> endpointDetails);
}
