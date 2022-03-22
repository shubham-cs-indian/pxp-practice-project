package com.cs.core.config.interactor.entity.assetstatus;

public class AssetUploadStatus implements IAssetUploadStatus {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          status;
  protected String          progress;
  
  @Override
  public String getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  @Override
  public String getProgress()
  {
    return progress;
  }
  
  @Override
  public void setProgress(String progress)
  {
    this.progress = progress;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
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
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
