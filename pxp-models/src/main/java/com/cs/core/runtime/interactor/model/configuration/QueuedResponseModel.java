package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.model.pluginexception.IPluginExceptionModel;

public class QueuedResponseModel implements IModel {
  
  /**
   *
   */
  private static final long       serialVersionUID = 1L;
  
  protected String                jobId;
  
  protected IModel                model;
  
  protected String                status;
  
  protected IPluginExceptionModel exceptionModel;
  
  public QueuedResponseModel(String jobId, IModel model)
  {
    super();
    this.jobId = jobId;
    this.model = model;
    this.status = "success";
  }
  
  public QueuedResponseModel(String jobId, IModel model, String status)
  {
    super();
    this.jobId = jobId;
    this.model = model;
    this.status = status;
  }
  
  public QueuedResponseModel(String jobId, IPluginExceptionModel model, String status)
  {
    super();
    this.jobId = jobId;
    this.exceptionModel = model;
    this.status = status;
  }
  
  public IPluginExceptionModel getExceptionModel()
  {
    return exceptionModel;
  }
  
  public void setExceptionModel(IPluginExceptionModel exceptionModel)
  {
    this.exceptionModel = exceptionModel;
  }
  
  public String getStatus()
  {
    return status;
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  public String getJobId()
  {
    return jobId;
  }
  
  public void setJobId(String jobId)
  {
    this.jobId = jobId;
  }
  
  public IModel getModel()
  {
    return model;
  }
  
  public void setModel(IModel model)
  {
    this.model = model;
  }
}
