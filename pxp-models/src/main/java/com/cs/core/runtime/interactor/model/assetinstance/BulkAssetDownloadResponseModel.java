package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class BulkAssetDownloadResponseModel implements IBulkAssetDownloadResponseModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected IAssetDownloadModel success;
  protected IExceptionModel     failure;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  @JsonDeserialize(as = ExceptionModel.class)
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public IAssetDownloadModel getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = AssetDownloadModel.class)
  public void setSuccess(IAssetDownloadModel success)
  {
    this.success = success;
  }

}
