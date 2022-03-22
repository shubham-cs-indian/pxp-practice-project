package com.cs.core.runtime.interactor.model.pluginexception;

import java.io.Serializable;
import java.util.List;

public interface IExceptionModel extends Serializable {
  
  public static final String EXCEPTION_DETAILS     = "exceptionDetails";
  public static final String DEV_EXCEPTION_DETAILS = "devExceptionDetails";
  
  public List<IExceptionDetailModel> getExceptionDetails();
  
  public void setExceptionDetails(List<IExceptionDetailModel> exceptionDetails);
  
  public List<IDevExceptionDetailModel> getDevExceptionDetails();
  
  public void setDevExceptionDetails(List<IDevExceptionDetailModel> exceptionDetails);
}
