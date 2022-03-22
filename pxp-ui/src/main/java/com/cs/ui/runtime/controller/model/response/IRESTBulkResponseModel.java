package com.cs.ui.runtime.controller.model.response;

import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IRESTBulkResponseModel extends IRESTModel {
  
  public Object getSuccess();
  
  // TODO: review from Rohith data type
  public void setSuccess(Object success);
  
  public IExceptionModel getFailure();
  
  public void setFailure(IExceptionModel exceptionModel);
}
