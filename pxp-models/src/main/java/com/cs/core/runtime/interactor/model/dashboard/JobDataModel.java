package com.cs.core.runtime.interactor.model.dashboard;

public class JobDataModel implements IJobDataModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            jobId;
  protected Integer         progress;
  protected String          status;
  protected Long            createdBy;
  protected Long            created;
  protected String 			service;
  
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
  public Integer getProgress()
  {
    return progress;
  }
  
  @Override
  public void setProgress(Integer progress)
  {
    this.progress = progress;
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
  public Long getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(Long createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public Long getCreated()
  {
    return created;
  }
  
  @Override
  public void setCreated(Long created)
  {
    this.created = created;
  }
	
  @Override
  public String getService() {
	return service;
  }
	
  @Override
  public void setService(String service) {
	  this.service = service;
  }
  
}

/*
{
  "jobId": "job_676",
  "progress": 30,
  "status": "RUNNING",
  "cretedBy": "user_Id",
  "created": 1234344
}*/