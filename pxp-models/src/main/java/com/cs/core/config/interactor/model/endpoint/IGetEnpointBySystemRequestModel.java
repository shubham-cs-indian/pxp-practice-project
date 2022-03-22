package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetEnpointBySystemRequestModel extends IConfigGetAllRequestModel {
  
  public static final String SYSTEM_ID = "systemId";
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
}
