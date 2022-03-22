package com.cs.core.config.interactor.model.processdetails;

public class ProcessStatusDetailsModel extends ProcessStatusModel
    implements IProcessStatusDetailsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         totalCount;
  protected Integer         failedCount;
  protected Integer         successCount;
  protected Integer         inprogressCount;
  protected String          startTime;
  protected String          endTime;
  
  public Integer getTotalCount()
  {
    return totalCount;
  }
  
  public void setTotalCount(Integer totalCount)
  {
    this.totalCount = totalCount;
  }
  
  public Integer getFailedCount()
  {
    return failedCount;
  }
  
  public void setFailedCount(Integer failedCount)
  {
    this.failedCount = failedCount;
  }
  
  public Integer getSuccessCount()
  {
    return successCount;
  }
  
  public void setSuccessCount(Integer successCount)
  {
    this.successCount = successCount;
  }
  
  public Integer getInprogressCount()
  {
    return inprogressCount;
  }
  
  public void setInprogressCount(Integer inprogressCount)
  {
    this.inprogressCount = inprogressCount;
  }
  
  public String getStartTime()
  {
    return startTime;
  }
  
  public void setStartTime(String startTime)
  {
    this.startTime = startTime;
  }
  
  public String getEndTime()
  {
    return endTime;
  }
  
  public void setEndTime(String endTime)
  {
    this.endTime = endTime;
  }
}
