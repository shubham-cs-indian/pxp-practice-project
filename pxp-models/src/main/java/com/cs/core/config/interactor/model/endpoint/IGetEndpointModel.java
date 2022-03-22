package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetEndpointModel extends IModel {
  
  public static final String ENDPOINT                  = "endpoint";
  public static final String REFERENCED_DASHBOARD_TABS = "referencedDashboardTabs";
  public static final String REFERENCED_SYSTEMS        = "referencedSystems";
  
  public IEndpointModel getEndpoint();
  
  public void setEndpoint(IEndpointModel endpoint);
  
  public Map<String, IConfigEntityInformationModel> getReferencedDashboardTabs();
  
  public void setReferencedDashboardTabs(
      Map<String, IConfigEntityInformationModel> referencedDashboardTabs);
  
  public Map<String, IConfigEntityInformationModel> getReferencedSystems();
  
  public void setReferencedSystems(Map<String, IConfigEntityInformationModel> referencedSystems);
}
