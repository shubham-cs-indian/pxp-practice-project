package com.cs.core.config.interactor.model.system;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ICreateSystemModel extends IConfigModel, ISystemModel {
  
  public static final String ENDPOINT_ID = "endpointId";
  
  public String getEndpointId();
  
  public void setEndPointId(String endpointId);
}
