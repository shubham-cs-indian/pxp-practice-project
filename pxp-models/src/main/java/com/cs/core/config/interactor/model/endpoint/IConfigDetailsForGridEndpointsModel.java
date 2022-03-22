package com.cs.core.config.interactor.model.endpoint;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForGridEndpointsModel extends IModel {
  
  public static final String REFERENCED_MAPPINGS               = "referencedMappings";
  public static final String REFERENCED_PROCESSES              = "referencedProcesses";
  public static final String REFERENCED_JMS_PROCESSES          = "referencedJmsProcesses";
  public static final String REFERENCED_SYSTEMS                = "referencedSystems";
  public static final String REFERENCED_DASHBOARD_TABS         = "referencedDashboardTabs";
  public static final String REFERENCED_AUTHORIZATION_MAPPINGS = "referencedAuthorizationMappings";
  
  public Map<String, IConfigEntityInformationModel> getReferencedSystems();
  
  public void setReferencedSystems(Map<String, IConfigEntityInformationModel> referencedSystems);
  
  public Map<String, IConfigEntityInformationModel> getReferencedMappings();
  
  public void setReferencedMappings(Map<String, IConfigEntityInformationModel> referencedMappings);
  
  public Map<String, IConfigEntityInformationModel> getReferencedProcesses();
  
  public void setReferencedProcesses(Map<String, IConfigEntityInformationModel> referencedProcesses);
  
  public Map<String, IConfigEntityInformationModel> getReferencedJmsProcesses();
  
  public void setReferencedJmsProcesses(Map<String, IConfigEntityInformationModel> referencedJmsProcesses);
  
  public Map<String, IConfigEntityInformationModel> getReferencedDashboardTabs();
  
  public void setReferencedDashboardTabs(
      Map<String, IConfigEntityInformationModel> referencedDashboardTabs);
  
  public Map<String, IConfigEntityInformationModel> getReferencedAuthorizationMappings();
  
  public void setReferencedAuthorizationMappings(
      Map<String, IConfigEntityInformationModel> referencedAuthorizationMappings);
}
