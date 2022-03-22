package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class ConfigDetailsForGridEndpointsModel implements IConfigDetailsForGridEndpointsModel {
  
  private static final long                              serialVersionUID = 1L;
  protected List<IGetEndpointModel>                      endpointsList;
  protected Map<String, IConfigEntityInformationModel>   referencedMappings;
  protected Map<String, IConfigEntityInformationModel>   referencedProcesses;
  protected Map<String, IConfigEntityInformationModel>   referencedJmsProcesses;
  protected Map<String, IConfigEntityInformationModel>   referencedSystems;
  protected Map<String, IConfigEntityInformationModel>   referencedDashboardTabs;
  protected Map<String, IConfigEntityInformationModel>   referencedAuthorizationMappings;
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedMappings()
  {
    return referencedMappings;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedMappings(Map<String, IConfigEntityInformationModel> referencedMappings)
  {
    this.referencedMappings = referencedMappings;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedProcesses()
  {
    return referencedProcesses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedProcesses(Map<String, IConfigEntityInformationModel> referencedProcesses)
  {
    this.referencedProcesses = referencedProcesses;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedJmsProcesses()
  {
    return referencedJmsProcesses;
  }

  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedJmsProcesses(Map<String, IConfigEntityInformationModel> referencedJmsProcesses)
  {
    this.referencedJmsProcesses = referencedJmsProcesses;
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
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedDashboardTabs()
  {
    return referencedDashboardTabs;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedDashboardTabs(
      Map<String, IConfigEntityInformationModel> referencedDashboardTabs)
  {
    this.referencedDashboardTabs = referencedDashboardTabs;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedAuthorizationMappings()
  {
    return referencedAuthorizationMappings;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedAuthorizationMappings(
      Map<String, IConfigEntityInformationModel> referencedAuthorizationMappings)
  {
    this.referencedAuthorizationMappings = referencedAuthorizationMappings;
  }
}
