package com.cs.core.rdbms.asset.idto;

public interface IAssetMiscDTO {
  
  public static final String PRIMARY_KEY           = "primaryKey";
  public static final String ASSET_INSTANCE_IID     = "assetInstanceIId";
  public static final String RENDITION_INSTANCE_IID = "renditionInstanceIId";
  public static final String DOWNLOAD_TIME_STAMP   = "downloadTimeStamp";
  public static final String DOWNLOAD_COUNT        = "downloadCount";
  public static final String SHARED_OBJECT_ID      = "sharedObjectId";
  public static final String SHARED_TIME_STAMP     = "sharedTimeStamp";
  
  public long getPrimaryKey();
  
  public void setPrimaryKey(long primaryKey);
  
  public long getAssetInstanceIId();
  
  public void setAssetInstanceIId(long assetInstanceIId);
  
  public long getRenditionInstanceIId();
  
  public void setRenditionInstanceIId(long renditionInstanceIId);
  
  public long getDownloadTimeStamp();
  
  public void setDownloadTimeStamp(long downloadTimeStamp);
  
  public long getDownloadCount();
  
  public void setDownloadCount(long downloadCount);
  
  public String getSharedObjectId();
  
  public void setSharedObjectId(String sharedObjectId);
  
  public long getSharedTimeStamp();
  
  public void setSharedTimeStamp(long sharedTimeStamp);
}
