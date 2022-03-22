package com.cs.ui.runtime.controller.model.response;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.*;

import java.util.ArrayList;
import java.util.List;

public class RESTFailedObjectResponseModel implements IRESTModel {
  
  protected IExceptionModel failure;
  protected String          status = "FAILURE";
  
  public RESTFailedObjectResponseModel(IExceptionModel response)
  {
    this.failure = response;
  }
  
  public RESTFailedObjectResponseModel(Throwable e)
  {
    IExceptionDetailModel exceptionDetail = new ExceptionDetailModel();
    exceptionDetail.setKey(e.getClass()
        .getSimpleName());
    List<IExceptionDetailModel> exceptionDetails = new ArrayList<>();
    exceptionDetails.add(exceptionDetail);
    
    IDevExceptionDetailModel devExceptionDetail = new DevExceptionDetailModel();
    devExceptionDetail.setDetailMessage(e.getMessage());
    devExceptionDetail.setKey(e.getClass()
        .getSimpleName());
    devExceptionDetail.setStack(e.getStackTrace());
    devExceptionDetail.setExceptionClass(e.getClass()
        .getName());
    List<IDevExceptionDetailModel> devExceptionDetails = new ArrayList<>();
    devExceptionDetails.add(devExceptionDetail);
    
    ExceptionModel response = new ExceptionModel();
    response.setExceptionDetails(exceptionDetails);
    response.setDevExceptionDetails(devExceptionDetails);
    
    this.failure = response;
  }
  
  public RESTFailedObjectResponseModel(PluginException e)
  {
    ExceptionModel interactorExceptionModel = new ExceptionModel();
    interactorExceptionModel.setDevExceptionDetails(e.getDevExceptionDetails());
    interactorExceptionModel.setExceptionDetails(e.getExceptionDetails());
    this.failure = interactorExceptionModel;
  }
  
  public IExceptionModel getFailure()
  {
    return this.failure;
  }
  
  public void setFailure(IExceptionModel response)
  {
    this.failure = response;
  }
}
