package com.cs.core.rdbms.asset.dto;

import com.cs.core.rdbms.asset.idto.IAssetMiscDTO;;

public class AssetMiscDTO implements IAssetMiscDTO {
  

  private long primaryKey;
  private long assetInstanceIId;
  private long renditionInstanceIId;
  private long downloadTimeStamp;
  private long downloadCount;
  private String sharedObjectId;
  private long sharedTimeStamp;
  
  public AssetMiscDTO()
  {
    
  }
  
  @Override
  public long getPrimaryKey()
  {
    return primaryKey;
  }
  
  @Override
  public void setPrimaryKey(long primaryKey)
  {
    this.primaryKey = primaryKey;
  }
  
  @Override
  public long getAssetInstanceIId()
  {
    return assetInstanceIId;
  }
  
  @Override
  public void setAssetInstanceIId(long assetInstanceIid)
  {
    this.assetInstanceIId = assetInstanceIid;
  }
  
  @Override
  public long getRenditionInstanceIId()
  {
    return renditionInstanceIId;
  }
  
  @Override
  public void setRenditionInstanceIId(long renditionInstanceIId)
  {
    this.renditionInstanceIId = renditionInstanceIId;
  }
  
  @Override
  public long getDownloadTimeStamp()
  {
    return downloadTimeStamp;
  }
  
  @Override
  public void setDownloadTimeStamp(long downloadTimeStamp)
  {
    this.downloadTimeStamp = downloadTimeStamp;
  }
  
  @Override
  public long getDownloadCount()
  {
    return downloadCount;
  }
  
  @Override
  public void setDownloadCount(long downloadCount)
  {
    this.downloadCount = downloadCount;
  }
  
  @Override
  public String getSharedObjectId()
  {
    return sharedObjectId;
  }
  
  @Override
  public void setSharedObjectId(String sharedObjectId)
  {
    this.sharedObjectId = sharedObjectId;
  }
  
  @Override
  public long getSharedTimeStamp()
  {
    return sharedTimeStamp;
  }
  
  @Override
  public void setSharedTimeStamp(long sharedTimeStamp)
  {
    this.sharedTimeStamp = sharedTimeStamp;
  }
  
}
