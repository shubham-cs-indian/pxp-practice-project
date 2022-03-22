package com.cs.core.config.interactor.model.downloadtracker;


public class GetDownloadLogsModel implements IGetDownloadLogsModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            primaryId;
  protected long            assetInstanceIId;
  protected String          assetInstanceName;
  protected String          assetInstanceClassId;
  protected String          assetInstanceClassCode;
  protected String          assetInstanceClassName;
  protected String          renditionInstanceIId;
  protected String          renditionInstanceName;
  protected String          renditionInstanceClassId;
  protected String          renditionInstanceClassCode;
  protected String          renditionInstanceClassName;
  protected String          userId;
  protected String          downloadedBy;
  protected Long            timestamp;
  protected String          comment;
  protected String          assetFileName;
  protected String          renditionFileName;
  protected String          userName;
  
  @Override
  public Long getPrimaryId()
  {
    return primaryId;
  }

  @Override
  public void setPrimaryId(Long primaryId)
  {
    this.primaryId = primaryId;
  }

  @Override
  public long getAssetInstanceIId()
  {
    return assetInstanceIId;
  }

  @Override
  public void setAssetInstanceIId(long assetInstanceId)
  {
    this.assetInstanceIId = assetInstanceId;
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
  public String getAssetInstanceClassName()
  {
    return assetInstanceClassName;
  }

  @Override
  public void setAssetInstanceClassName(String assetInstanceClassName)
  {
    this.assetInstanceClassName = assetInstanceClassName;
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
  public String getRenditionInstanceIId()
  {
    return renditionInstanceIId;
  }

  @Override
  public void setRenditionInstanceIId(String renditionInstanceId)
  {
    this.renditionInstanceIId = renditionInstanceId;
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
  public String getRenditionInstanceClassName()
  {
    return renditionInstanceClassName;
  }

  @Override
  public void setRenditionInstanceClassName(String renditionInstanceClassName)
  {
    this.renditionInstanceClassName = renditionInstanceClassName;
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
  public String getDownloadedBy()
  {
    return downloadedBy;
  }

  @Override
  public void setDownloadedBy(String downloadedBy)
  {
    this.downloadedBy = downloadedBy;
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
  public String getUserName()
  {
    return this.userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
}
