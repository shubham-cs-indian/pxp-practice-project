package com.cs.core.config.interactor.entity.assetstatus;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IAssetUploadStatus extends IEntity {
  
  public static final String STATUS   = "status";
  public static final String PROGRESS = "progress";
  
  public String getStatus();
  
  public void setStatus(String status);
  
  public String getProgress();
  
  public void setProgress(String progress);
}
