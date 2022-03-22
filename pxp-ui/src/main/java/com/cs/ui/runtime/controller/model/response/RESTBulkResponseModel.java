package com.cs.ui.runtime.controller.model.response;

import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public class RESTBulkResponseModel implements IRESTBulkResponseModel {
  
  protected String          status = "SUCCESS";
  protected Object          success;
  protected IExceptionModel failure;
  
  @Override
  public Object getSuccess()
  {
    return this.success;
  }
  
  @Override
  public void setSuccess(Object success)
  {
    this.success = success;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
}
