package com.cs.core.exception;

import com.cs.core.runtime.interactor.model.pluginexception.DevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import java.util.ArrayList;
import java.util.List;

public class PluginException extends Exception {
  
  private static final long                serialVersionUID = 1L;
  
  protected List<IExceptionDetailModel>    exceptionDetails;
  protected List<IDevExceptionDetailModel> devExceptionDetails;
  
  public PluginException()
  {
    createDefaultExceptionDetailsAndDevExceptionDetailsModel(this);
  }
  
  public PluginException(String message)
  {
    super(message);
    createDefaultExceptionDetailsAndDevExceptionDetailsModel(this);
  }
  
  private void createDefaultExceptionDetailsAndDevExceptionDetailsModel(Throwable e)
  {
    IExceptionDetailModel exceptionDetail = new ExceptionDetailModel();
    exceptionDetail.setKey(this.getClass()
        .getSimpleName());
    this.getExceptionDetails()
        .add(exceptionDetail);
    
    IDevExceptionDetailModel devExceptionDetail = new DevExceptionDetailModel();
    devExceptionDetail.setKey(this.getClass()
        .getSimpleName());
    devExceptionDetail.setDetailMessage(this.getMessage());
    devExceptionDetail.setStack(this.getStackTrace());
    devExceptionDetail.setExceptionClass(this.getClass()
        .getName());
    this.getDevExceptionDetails()
        .add(devExceptionDetail);
  }
  
  public PluginException(IExceptionModel exceptionModel)
  {
    exceptionDetails = exceptionModel.getExceptionDetails();
    devExceptionDetails = exceptionModel.getDevExceptionDetails();
  }
  
  public PluginException(Exception e)
  {
    super(e);
    createDefaultExceptionDetailsAndDevExceptionDetailsModel(e);
    this.getDevExceptionDetails()
        .get(0)
        .setStack(e.getStackTrace());
  }
  
  public PluginException(Throwable e)
  {
    super(e);
    createDefaultExceptionDetailsAndDevExceptionDetailsModel(e);
    this.getDevExceptionDetails()
        .get(0)
        .setStack(e.getStackTrace());
  }
  
  public PluginException(List<IExceptionDetailModel> exceptionDetails,
      List<IDevExceptionDetailModel> devExceptionDetails)
  {
    this.exceptionDetails = exceptionDetails;
    this.devExceptionDetails = devExceptionDetails;
  }
  
  public List<IExceptionDetailModel> getExceptionDetails()
  {
    if (exceptionDetails == null) {
      exceptionDetails = new ArrayList<IExceptionDetailModel>();
    }
    return exceptionDetails;
  }
  
  public void setExceptionDetails(List<IExceptionDetailModel> exceptionDetails)
  {
    this.exceptionDetails = exceptionDetails;
  }
  
  public List<IDevExceptionDetailModel> getDevExceptionDetails()
  {
    if (devExceptionDetails == null) {
      devExceptionDetails = new ArrayList<IDevExceptionDetailModel>();
    }
    return devExceptionDetails;
  }
  
  public void setDevExceptionDetails(List<IDevExceptionDetailModel> devExceptionDetails)
  {
    this.devExceptionDetails = devExceptionDetails;
  }
}
