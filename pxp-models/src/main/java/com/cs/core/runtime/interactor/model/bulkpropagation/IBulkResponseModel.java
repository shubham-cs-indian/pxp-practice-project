package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IBulkResponseModel extends IModel {
  
  public static final String FAILURE = "failure";
  public static final String SUCCESS = "success";
  
  public IExceptionModel getFailure();
  
  public void setFailure(IExceptionModel failure);
  
  // TODO: review from Rohith data type
  // also see the implementing classes
  public Object getSuccess();
}
