package com.cs.core.config.interactor.model.auditlog;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAuditLogExportEntryModel extends IModel{
  
  public static final String FILE_NAME  = "fileName";
  public static final String USERNAME   = "userName";
  public static final String ASSET_ID   = "assetId";
  public static final String START_TIME = "startTime";
  public static final String END_TIME   = "endTime";
  public static final String STATUS     = "status";
  
  public String getFileName();
  public void setFileName(String fileName);
  
  public String getUserName();
  public void setUserName(String userName);
  
  public Long getStartTime();
  public void setStartTime(Long startTime);
  
  public Long getEndTime();
  public void setEndTime(Long endTime);
  
  public Integer getStatus();
  public void setStatus(Integer status);
  
  public String getAssetId();
  public void setAssetId(String assetId);
}
