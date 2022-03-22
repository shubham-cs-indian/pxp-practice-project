package com.cs.core.config.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetDownloadLogsModel extends IModel {
  
  public static final String USER_ID                       = "userId";
  public static final String COMMENT                       = "comment";
  public static final String USER_NAME                     = "userName";
  public static final String TIMESTAMP                     = "timestamp";
  public static final String PRIMARY_ID                    = "primaryId";
  public static final String DOWNLOADED_BY                 = "downloadedBy";
  public static final String ASSET_FILE_NAME               = "assetFileName";
  public static final String ASSET_INSTANCE_IID             = "assetInstanceIId";
  public static final String RENDITION_FILE_NAME           = "renditionFileName";
  public static final String ASSET_INSTANCE_NAME           = "assetInstanceName";
  public static final String RENDITION_INSTANCE_IID         = "renditionInstanceIId";
  public static final String ASSET_INSTANCE_CLASS_ID       = "assetInstanceClassId";
  public static final String RENDITION_INSTANCE_NAME       = "renditionInstanceName";
  public static final String ASSET_INSTANCE_CLASS_CODE     = "assetInstanceClassCode";
  public static final String ASSET_INSTANCE_CLASS_NAME     = "assetInstanceClassName";
  public static final String RENDITION_INSTANCE_CLASS_ID   = "renditionInstanceClassId";
  public static final String RENDITION_INSTANCE_CLASS_CODE = "renditionInstanceClassCode";
  public static final String RENDITION_INSTANCE_CLASS_NAME = "renditionInstanceClassName";
  
  public String getUserId();
  public void setUserId(String userId);
  
  public String getComment();
  public void setComment(String comment);

  public String getUserName();
  public void setUserName(String userName);

  public Long getTimestamp();
  public void setTimestamp(Long timestamp);
  
  public Long getPrimaryId();
  public void setPrimaryId(Long primaryId);
  
  public long getAssetInstanceIId();
  public void setAssetInstanceIId(long assetId);
  
  public String getDownloadedBy();
  public void setDownloadedBy(String downloadedBy);
  
  public String getAssetFileName();
  public void setAssetFileName(String assetFileName);
  
  public String getAssetInstanceName();
  public void setAssetInstanceName(String assetName);
  
  public String getRenditionInstanceIId();
  public void setRenditionInstanceIId(String renditionId);
  
  public String getAssetInstanceClassId();
  public void setAssetInstanceClassId(String assetClassId);

  public String getRenditionFileName();
  public void setRenditionFileName(String renditionFileName);
  
  public String getRenditionInstanceName();
  public void setRenditionInstanceName(String renditionName);
  
  public String getAssetInstanceClassCode();
  public void setAssetInstanceClassCode(String assetClassCode);
  
  public String getAssetInstanceClassName();
  public void setAssetInstanceClassName(String assetClassName);

  public String getRenditionInstanceClassId();
  public void setRenditionInstanceClassId(String renditionClassId);
  
  public String getRenditionInstanceClassCode();
  public void setRenditionInstanceClassCode(String renditionClassCode);
  
  public String getRenditionInstanceClassName();
  public void setRenditionInstanceClassName(String renditionClassName);
 
}
