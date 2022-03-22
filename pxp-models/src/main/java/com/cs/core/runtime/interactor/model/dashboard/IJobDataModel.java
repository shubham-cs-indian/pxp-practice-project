package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IJobDataModel extends IModel{
	
  public Long getJobId();
  public void setJobId(Long jobId);
  
  public Integer getProgress();
  public void setProgress(Integer progress);
  
  public String getStatus();
  public void setStatus(String status);
  
  public Long getCreatedBy();
  public void setCreatedBy(Long cretedBy);
  
  public Long getCreated();
  public void setCreated(Long created);
  
  public String getService();
  public void setService(String service);
}
