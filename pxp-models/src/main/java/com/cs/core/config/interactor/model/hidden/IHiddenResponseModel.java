package com.cs.core.config.interactor.model.hidden;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IHiddenResponseModel extends IModel {
  
  public static final String RESPONSE = "response";
  
  public String getResponse();
  
  public void setResponse(String response);
}
