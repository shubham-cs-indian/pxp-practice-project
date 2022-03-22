package com.cs.core.config.interactor.model.endpoint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetGridEndpointsResponseModel implements IGetGridEndpointsResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<IGetEndpointModel>             endpointsList;
  protected Long                                count;
  protected IConfigDetailsForGridEndpointsModel configDetails;
  
  @Override
  public List<IGetEndpointModel> getEndpointsList()
  {
    if (endpointsList == null) {
      endpointsList = new ArrayList<>();
    }
    return endpointsList;
  }
  
  @JsonDeserialize(contentAs = GetEndpointModel.class)
  @Override
  public void setEndpointsList(List<IGetEndpointModel> taskList)
  {
    this.endpointsList = taskList;
  }
  
  @Override
  public Long getCount()
  {
    if (count == null) {
      count = new Long(0);
    }
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public IConfigDetailsForGridEndpointsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGridEndpointsModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGridEndpointsModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
