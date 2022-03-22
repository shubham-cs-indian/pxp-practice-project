package com.cs.core.config.interactor.model.system;

public class CreateSystemModel extends SystemModel implements ICreateSystemModel {
  
  private static final long serialVersionUID = 1L;
  protected String          endpointId;
  
  public CreateSystemModel()
  {
    super();
  }
  
  public CreateSystemModel(String endpointId)
  {
    super();
    this.endpointId = endpointId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndPointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
}
