package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

public class GetAllEndpointsByTypeRequestModel extends ConfigGetAllRequestModel
    implements IGetAllEndpointsByTypeRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          endpointType;
  
  @Override
  public String getEndpointType()
  {
    return endpointType;
  }
  
  @Override
  public void setEndpointType(String endpointType)
  {
    this.endpointType = endpointType;
  }
}
