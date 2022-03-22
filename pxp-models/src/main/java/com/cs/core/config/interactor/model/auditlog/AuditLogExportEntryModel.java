package com.cs.core.config.interactor.model.auditlog;

public class AuditLogExportEntryModel implements IAuditLogExportEntryModel {
  
  private static final long serialVersionUID = 1L;
  protected String          fileName;
  protected String          userName;
  protected String          assetId;
  protected Long            startTime;
  protected Long            endTime;
  protected Integer         status;
  
  @Override
  public String getAssetId()
  {
    return assetId;
  }
  
  @Override
  public void setAssetId(String assetId)
  {
    this.assetId = assetId;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public Long getStartTime()
  {
    return startTime;
  }
  
  @Override
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  @Override
  public Long getEndTime()
  {
    return endTime;
  }
  
  @Override
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }
  
  @Override
  public Integer getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(Integer status)
  {
    this.status = status;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
    
  }
}
