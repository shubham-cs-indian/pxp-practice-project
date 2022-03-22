package com.cs.ui.runtime.controller.model.response;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public class RESTSuccessResponseModel implements IResponseModel {
  
  protected String status = "SUCCESS";
  
  protected IModel response;
  
  @Override
  public IModel getSuccess()
  {
    return this.response;
  }
  
  @Override
  public void setSuccess(IModel response)
  {
    this.response = response;
  }
}
