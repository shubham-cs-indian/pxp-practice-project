package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllEndpointsByTypeRequestModel extends IConfigGetAllRequestModel {
  
  public static final String ENDPOINT_TYPE = "endpointType";
  
  public String getEndpointType();
  
  public void setEndpointType(String endpointType);
}
