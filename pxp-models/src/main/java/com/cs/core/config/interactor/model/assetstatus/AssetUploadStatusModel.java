package com.cs.core.config.interactor.model.assetstatus;

import com.cs.core.config.interactor.entity.assetstatus.AssetUploadStatus;
import com.cs.core.config.interactor.entity.assetstatus.IAssetUploadStatus;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public class AssetUploadStatusModel implements IAssetUploadStatusModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected IAssetUploadStatus entity;
  
  public AssetUploadStatusModel()
  {
    this.entity = new AssetUploadStatus();
  }
  
  public AssetUploadStatusModel(IAssetUploadStatus entity)
  {
    this.entity = entity;
  }
  
  @Override
  public String getStatus()
  {
    return entity.getStatus();
  }
  
  @Override
  public void setStatus(String status)
  {
    entity.setStatus(status);
  }
  
  @Override
  public String getProgress()
  {
    return entity.getProgress();
  }
  
  @Override
  public void setProgress(String progress)
  {
    entity.setProgress(progress);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String toString()
  {
    return entity.toString();
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
}
