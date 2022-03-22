package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

import java.util.List;

public interface IEndpointModel extends IConfigModel, IEndpoint {
  
  public static final String PROCESSES             = "processes";
  public static final String JMS_PROCESSES         = "jmsProcesses";
  public static final String MAPPINGS              = "mappings";
  public static final String AUTHORIZATION_MAPPING = "authorizationMapping";
  
  public List<String> getProcesses();
  public void setProcesses(List<String> processes);
  
  public List<String> getJmsProcesses();
  public void setJmsProcesses(List<String> jmsProcesses);
  
  public List<String> getMappings();
  public void setMappings(List<String> mappings);
  
  public List<String> getAuthorizationMapping();
  public void setAuthorizationMapping(List<String> authorizationMapping);
}
