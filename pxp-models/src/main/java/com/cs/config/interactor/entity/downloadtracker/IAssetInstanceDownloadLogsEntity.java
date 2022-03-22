package com.cs.config.interactor.entity.downloadtracker;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;


public interface IAssetInstanceDownloadLogsEntity extends IEntity {
  
  public static final String PRIMARY_KEY                   = "primaryKey";
  public static final String ASSET_INSTANCE_ID             = "assetInstanceId";
  public static final String ASSET_INSTANCE_NAME           = "assetInstanceName";
  public static final String ASSET_INSTANCE_CLASS_ID       = "assetInstanceClassId";
  public static final String ASSET_INSTANCE_CLASS_CODE     = "assetInstanceClassCode";
  public static final String RENDITION_INSTANCE_ID         = "renditionInstanceId";
  public static final String RENDITION_INSTANCE_NAME       = "renditionInstanceName";
  public static final String RENDITION_INSTANCE_CLASS_ID   = "renditionInstanceClassId";
  public static final String RENDITION_INSTANCE_CLASS_CODE = "renditionInstanceClassCode";
  public static final String USER_ID                       = "userId";
  public static final String TIMESTAMP                     = "timestamp";
  public static final String COMMENT                       = "comment";
  public static final String DOWNLOAD_ID                   = "downloadId";
  public static final String ASSET_FILE_NAME               = "assetFileName";
  public static final String RENDITION_FILE_NAME           = "renditionFileName";
  public static final String USER_NAME                     = "userName";
  public static final String IS_RESET                      = "isReset";
  
  public Long getPrimaryKey();
  public void setPrimaryKey(Long primaryKey);
  
  public String getAssetInstanceId();
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getAssetInstanceName();
  public void setAssetInstanceName(String assetInstanceName);
  
  public String getAssetInstanceClassId();
  public void setAssetInstanceClassId(String assetInstanceClassId);
  
  public String getAssetInstanceClassCode();
  public void setAssetInstanceClassCode(String assetInstanceClassCode);
  
  public String getRenditionInstanceId();
  public void setRenditionInstanceId(String renditionInstanceId);
  
  public String getRenditionInstanceName();
  public void setRenditionInstanceName(String renditionInstanceName);
  
  public String getRenditionInstanceClassId();
  public void setRenditionInstanceClassId(String renditionInstanceClassId);
  
  public String getRenditionInstanceClassCode();
  public void setRenditionInstanceClassCode(String renditionInstanceClassCode);
  
  public String getUserId();
  public void setUserId(String userId);
  
  public Long getTimestamp();
  public void setTimestamp(Long timestamp);
  
  public String getComment();
  public void setComment(String comment);
  
  public String getDownloadId();
  public void setDownloadId(String downloadId);
  
  public String getAssetFileName();
  public void setAssetFileName(String assetFileName);
  
  public String getRenditionFileName();
  public void setRenditionFileName(String renditionFileName);
  
  public String getUserName();
  public void setUserName(String userName);
  
  public Boolean getIsReset();
  public void setIsReset(Boolean isReset);
  
}
