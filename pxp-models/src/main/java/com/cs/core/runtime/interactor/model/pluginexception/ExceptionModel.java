package com.cs.core.runtime.interactor.model.pluginexception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ExceptionModel implements IExceptionModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<IExceptionDetailModel>    exceptionDetails;
  protected List<IDevExceptionDetailModel> devExceptionDetails;
  
  @Override
  public List<IExceptionDetailModel> getExceptionDetails()
  {
    if (exceptionDetails == null) {
      exceptionDetails = new ArrayList<IExceptionDetailModel>();
    }
    return exceptionDetails;
  }
  
  @JsonDeserialize(contentAs = ExceptionDetailModel.class)
  @Override
  public void setExceptionDetails(List<IExceptionDetailModel> exceptionDetails)
  {
    this.exceptionDetails = exceptionDetails;
  }
  
  @Override
  public List<IDevExceptionDetailModel> getDevExceptionDetails()
  {
    if (devExceptionDetails == null) {
      devExceptionDetails = new ArrayList<IDevExceptionDetailModel>();
    }
    return devExceptionDetails;
  }
  
  @JsonDeserialize(contentAs = DevExceptionDetailModel.class)
  @Override
  public void setDevExceptionDetails(List<IDevExceptionDetailModel> devExceptionDetails)
  {
    this.devExceptionDetails = devExceptionDetails;
  }
}
