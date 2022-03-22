package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAttributeToExportModel extends IModel {
  
  public static String TAG_ID                       = "tagId";
  public static String LAST_EXPORTED_TIME_STAMP_KEY = "lastExportedTimeStampKey";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public String getLastExportedTimeStampKey();
  
  public void setLastExportedTimeStampKey(String lastExportedTimeStampKey);
}
