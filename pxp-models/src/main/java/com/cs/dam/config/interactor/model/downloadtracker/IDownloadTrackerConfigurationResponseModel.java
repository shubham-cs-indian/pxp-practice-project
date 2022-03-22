package com.cs.dam.config.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IDownloadTrackerConfigurationResponseModel extends IModel {
  
  public static final String KLASS_ID                    = "klassId";
  public static final String KLASS_CODE                  = "klassCode";
  public static final String IS_DOWNLOAD_TRACKER_ENABLED = "isDownloadTrackerEnabled";
  
  public String getKlassId();
  public void setKlassId(String klassId);
  
  public String getKlassCode();
  public void setKlassCode(String klassCode);
  
  public Boolean getIsDownloadTrackerEnabled();
  public void setIsDownloadTrackerEnabled(Boolean isDownloadTrackerEnabled);
  
}
