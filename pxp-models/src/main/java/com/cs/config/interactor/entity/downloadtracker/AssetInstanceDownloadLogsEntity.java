package com.cs.config.interactor.entity.downloadtracker;


public class AssetInstanceDownloadLogsEntity implements IAssetInstanceDownloadLogsEntity {
  
  private static final long serialVersionUID = 1L;
  protected Long            primaryKey;
  protected String          assetInstanceId;
  protected String          assetInstanceName;
  protected String          assetInstanceClassId;
  protected String          assetInstanceClassCode;
  protected String          renditionInstanceId;
  protected String          renditionInstanceName;
  protected String          renditionInstanceClassId;
  protected String          renditionInstanceClassCode;
  protected String          userId;
  protected Long            timestamp;
  protected String          comment;
  protected String          downloadId;
  protected String          assetFileName;
  protected String          renditionFileName;
  protected String          userName;
  protected Boolean         isReset;

  @Override
  public Long getPrimaryKey()
  {
    return primaryKey;
  }

  @Override
  public void setPrimaryKey(Long primaryKey)
  {
    this.primaryKey = primaryKey;
  }
  
  @Override
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }

  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }

  @Override
  public String getAssetInstanceName()
  {
    return assetInstanceName;
  }

  @Override
  public void setAssetInstanceName(String assetInstanceName)
  {
    this.assetInstanceName = assetInstanceName;
  }
  
  @Override
  public String getAssetInstanceClassId()
  {
    return assetInstanceClassId;
  }

  @Override
  public void setAssetInstanceClassId(String assetInstanceClassId)
  {
    this.assetInstanceClassId = assetInstanceClassId;
  }
  
  @Override
  public String getAssetInstanceClassCode()
  {
    return assetInstanceClassCode;
  }

  @Override
  public void setAssetInstanceClassCode(String assetInstanceClassCode)
  {
    this.assetInstanceClassCode = assetInstanceClassCode;
  }
  
  @Override
  public String getRenditionInstanceId()
  {
    return renditionInstanceId;
  }
  
  @Override
  public void setRenditionInstanceId(String renditionInstanceId)
  {
    this.renditionInstanceId = renditionInstanceId;
  }

  @Override
  public String getRenditionInstanceName()
  {
    return renditionInstanceName;
  }

  @Override
  public void setRenditionInstanceName(String renditionInstanceName)
  {
    this.renditionInstanceName = renditionInstanceName;
  }

  @Override
  public String getRenditionInstanceClassId()
  {
    return renditionInstanceClassId;
  }

  @Override
  public void setRenditionInstanceClassId(String renditionInstanceClassId)
  {
    this.renditionInstanceClassId = renditionInstanceClassId;
  }
  
  @Override
  public String getRenditionInstanceClassCode()
  {
    return renditionInstanceClassCode;
  }

  @Override
  public void setRenditionInstanceClassCode(String renditionInstanceClassCode)
  {
    this.renditionInstanceClassCode = renditionInstanceClassCode;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }

  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  @Override
  public Long getTimestamp()
  {
    return timestamp;
  }

  @Override
  public void setTimestamp(Long timestamp)
  {
    this.timestamp = timestamp;
  }

  @Override
  public String getComment()
  {
    return comment;
  }

  @Override
  public void setComment(String comment)
  {
    this.comment = comment;
  }
  
  @Override
  public String getDownloadId()
  {
    return downloadId;
  }

  @Override
  public void setDownloadId(String downloadId)
  {
    this.downloadId = downloadId;
  }
  
  @Override
  public String getAssetFileName()
  {
    return this.assetFileName;
  }
  
  @Override
  public void setAssetFileName(String assetFileName)
  {
    this.assetFileName = assetFileName;
  }
  
  @Override
  public String getRenditionFileName()
  {
    return this.renditionFileName;
  }
  
  @Override
  public void setRenditionFileName(String renditionFileName)
  {
    this.renditionFileName = renditionFileName;
  }  
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUserName()
  {
    return this.userName;
  }

  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Boolean getIsReset()
  {
    return isReset;
  }

  @Override
  public void setIsReset(Boolean isReset)
  {
    this.isReset = isReset;
  }

}
