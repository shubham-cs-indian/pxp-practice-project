package com.cs.core.runtime.interactor.model.process;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBGPProcessModel extends IModel{
  
  public Long getJobId();
  public void setJobId(Long jobId);
  
  public String getStatus();
  public void setStatus(String status);
  
  public String getLogData();
  public void setLogData(String logData);
  
  public String getService();
  public void setService(String service);
  
  public String getProcessDefination();
  public void setProcessDefination(String processDeifintion);
  
}
