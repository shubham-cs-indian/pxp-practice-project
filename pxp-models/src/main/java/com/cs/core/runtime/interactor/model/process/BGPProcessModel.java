package com.cs.core.runtime.interactor.model.process;

public class BGPProcessModel implements IBGPProcessModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Long            jobId;
  protected String          status;
  protected String          logData;
  protected String          service;
  protected String          processDefination;
  
  @Override
  public Long getJobId()
  {
    return jobId;
  }
  
  @Override
  public void setJobId(Long jobId)
  {
    this.jobId = jobId;
  }
  
  @Override
  public String getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  @Override
  public String getLogData()
  {
    return logData;
  }
  
  @Override
  public void setLogData(String logData)
  {
    this.logData = logData;
  }
  
  @Override
  public String getService()
  {
    return service;
  }
  
  @Override
  public void setService(String service)
  {
    this.service = service;
  }
  
  @Override
  public String getProcessDefination()
  {
    return processDefination;
  }
  
  @Override
  public void setProcessDefination(String processDefination)
  {
    this.processDefination = processDefination;
  }
}
