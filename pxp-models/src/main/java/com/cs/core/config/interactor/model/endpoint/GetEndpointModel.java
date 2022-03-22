package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetEndpointModel implements IGetEndpointModel {
  
  private static final long                            serialVersionUID = 1L;
  protected IEndpointModel                             endpoint;
  protected Map<String, IConfigEntityInformationModel> referencedDashboardTabs;
  protected Map<String, IConfigEntityInformationModel> referencedSystems;
  
  @Override
  public IEndpointModel getEndpoint()
  {
    return endpoint;
  }
  
  @Override
  @JsonDeserialize(as = EndpointModel.class)
  public void setEndpoint(IEndpointModel endpoint)
  {
    this.endpoint = endpoint;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedDashboardTabs()
  {
    return referencedDashboardTabs;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedDashboardTabs(
      Map<String, IConfigEntityInformationModel> referencedDashboardTabs)
  {
    this.referencedDashboardTabs = referencedDashboardTabs;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedSystems()
  {
    return referencedSystems;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedSystems(Map<String, IConfigEntityInformationModel> referencedSystems)
  {
    this.referencedSystems = referencedSystems;
  }
}
