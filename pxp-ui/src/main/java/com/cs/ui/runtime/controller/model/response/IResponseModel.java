package com.cs.ui.runtime.controller.model.response;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IResponseModel extends IRESTModel {
  
  public IModel getSuccess();
  
  public void setSuccess(IModel response);
}
