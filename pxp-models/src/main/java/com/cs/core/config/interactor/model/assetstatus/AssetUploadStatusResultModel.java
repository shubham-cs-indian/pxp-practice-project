package com.cs.core.config.interactor.model.assetstatus;

public class AssetUploadStatusResultModel implements IAssetUploadStatusResultModel {
  
  protected Integer status;
  
  @Override
  public Integer getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(Integer status)
  {
    this.status = status;
  }
}
