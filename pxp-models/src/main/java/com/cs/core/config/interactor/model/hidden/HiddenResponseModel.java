package com.cs.core.config.interactor.model.hidden;

public class HiddenResponseModel implements IHiddenResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected String          response;
  
  @Override
  public String getResponse()
  {
    return response;
  }
  
  @Override
  public void setResponse(String response)
  {
    this.response = response;
  }
}
