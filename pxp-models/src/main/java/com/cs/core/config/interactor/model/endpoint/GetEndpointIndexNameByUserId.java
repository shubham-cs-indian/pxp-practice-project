package com.cs.core.config.interactor.model.endpoint;

public class GetEndpointIndexNameByUserId implements IGetEndpointIndexNameByUserIdModel {
  
  private static final long serialVersionUID = 1L;
  protected String          endpointIndexName;
  
  @Override
  public String getEndpointIndexName()
  {
    return this.endpointIndexName;
  }
  
  @Override
  public void setEndpointIndexName(String endpointIndexName)
  {
    this.endpointIndexName = endpointIndexName;
  }
}
