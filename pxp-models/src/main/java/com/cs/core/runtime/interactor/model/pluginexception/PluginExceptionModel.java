package com.cs.core.runtime.interactor.model.pluginexception;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PluginExceptionModel implements IPluginExceptionModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<IExceptionDetailModel>    exceptionDetails;
  protected List<IDevExceptionDetailModel> devExceptionDetails;
  protected String                         exceptionClass;
  
  public List<IExceptionDetailModel> getExceptionDetails()
  {
    if (exceptionDetails == null) {
      exceptionDetails = new ArrayList<>();
    }
    return exceptionDetails;
  }
  
  @JsonDeserialize(contentAs = ExceptionDetailModel.class)
  public void setExceptionDetails(List<IExceptionDetailModel> exceptionDetails)
  {
    this.exceptionDetails = exceptionDetails;
  }
  
  public List<IDevExceptionDetailModel> getDevExceptionDetails()
  {
    if (devExceptionDetails == null) {
      devExceptionDetails = new ArrayList<>();
    }
    return devExceptionDetails;
  }
  
  @JsonDeserialize(contentAs = DevExceptionDetailModel.class)
  public void setDevExceptionDetails(List<IDevExceptionDetailModel> devExceptionDetails)
  {
    this.devExceptionDetails = devExceptionDetails;
  }
  
  public String getExceptionClass()
  {
    return exceptionClass;
  }
  
  public void setExceptionClass(String exceptionClass)
  {
    this.exceptionClass = exceptionClass;
  }
}
