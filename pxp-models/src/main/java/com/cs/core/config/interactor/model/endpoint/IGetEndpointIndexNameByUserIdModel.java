package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetEndpointIndexNameByUserIdModel extends IModel {
  
  public static final String ENDPOINT_INDEX_NAME = "endpointIndexName";
  
  public String getEndpointIndexName();
  
  public void setEndpointIndexName(String endpointIndexName);
}
