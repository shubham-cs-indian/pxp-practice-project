package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

public class GetEndpointBySystemRequestModel extends ConfigGetAllRequestModel
    implements IGetEnpointBySystemRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          systemId;
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
}
