package com.cs.dam.rdbms.downloadtracker.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;


public interface IDownloadTrackerDTO extends IRootDTO {
  
  public static final String PRIMARY_KEY                   = "primaryKey";
  public static final String ASSET_INSTANCE_IID            = "assetInstanceIId";
  public static final String ASSET_INSTANCE_NAME           = "assetInstanceName";
  public static final String ASSET_FILE_NAME               = "assetFileName";
  public static final String ASSET_INSTANCE_CLASS_ID       = "assetInstanceClassId";
  public static final String ASSET_INSTANCE_CLASS_CODE     = "assetInstanceClassCode";
  public static final String RENDITION_INSTANCE_IID        = "renditionInstanceIId";
  public static final String RENDITION_INSTANCE_NAME       = "renditionInstanceName";
  public static final String RENDITION_FILE_NAME           = "renditionFileName";
  public static final String RENDITION_INSTANCE_CLASS_ID   = "renditionInstanceClassId";
  public static final String RENDITION_INSTANCE_CLASS_CODE = "renditionInstanceClassCode";
  public static final String USER_ID                       = "userId";
  public static final String TIME_STAMP                    = "timeStamp";
  public static final String COMMENT                       = "comment";
  public static final String DOWNLOAD_ID                   = "downloadId";
  public static final String USER_NAME                     = "userName";
  public static final String IS_RESET                      = "isReset";
  
  public Long getPrimaryKey();
  public void setPrimaryKey(Long primaryKey);
  
  long getAssetInstanceIId();
  void setAssetInstanceIId(long assetInstanceIId);
  
  public String getAssetInstanceName();
  public void setAssetInstanceName(String assetInstanceName);

  String getAssetFileName();
  void setAssetFileName(String assetFileName);

  String getAssetInstanceClassId();
  void setAssetInstanceClassId(String assetInstanceClassId);

  String getAssetInstanceClassCode();
  void setAssetInstanceClassCode(String assetInstanceClassCode);

  String getRenditionInstanceIId();
  void setRenditionInstanceIid(String renditionInstanceIId);

  String getRenditionInstanceName();
  void setRenditionInstanceName(String renditionInstanceName);

  String getRenditionFileName();
  void setRenditionFileName(String renditionFileName);

  String getRenditionInstanceClassId();
  void setRenditionInstanceClassId(String renditionInstanceClassId);
  
  String getRenditionInstanceClassCode();
  void setRenditionInstanceClassCode(String renditionInstanceClassCode);
  
  String getUserId();
  void setUserId(String userId);
  
  long getTimeStamp();
  void setTimeStamp(long timeStamp);
  
  String getComment();
  void setComment(String comment);
  
  String getDownloadId();
  void setDownloadId(String downloadId);
  
  String getUserName();
  void setUserName(String userName);
  
  boolean isReset();
  void setReset(boolean isReset);
}
