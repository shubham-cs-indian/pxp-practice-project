package com.cs.core.config.interactor.model.processdetails;

public interface IProcessStatusDetailsModel extends IProcessStatusModel {
  
  public static final String TOTAL_COUNT      = "totalCount";
  public static final String FAILED_COUNT     = "failedCount";
  public static final String SUCCESS_COUNT    = "successCount";
  public static final String INPROGRESS_COUNT = "inprogressCount";
  public static final String START_TIME       = "startTime";
  public static final String END_TIME         = "endTime";
  
  public Integer getTotalCount();
  
  public void setTotalCount(Integer totalCount);
  
  public Integer getFailedCount();
  
  public void setFailedCount(Integer failedCount);
  
  public Integer getSuccessCount();
  
  public void setSuccessCount(Integer passCount);
  
  public Integer getInprogressCount();
  
  public void setInprogressCount(Integer inprogressCount);
  
  public String getStartTime();
  
  public void setStartTime(String startTime);
  
  public String getEndTime();
  
  public void setEndTime(String endTime);
}
