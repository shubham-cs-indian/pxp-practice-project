package com.cs.core.config.interactor.model.configdetails;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadSuccessResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public class BulkUploadResponseAssetModel extends ConfigResponseWithAuditLogModel
    implements IBulkUploadResponseAssetModel {
  
  /**
   * 
   */
  private static final long                  serialVersionUID = 1L;
  protected IAssetUploadSuccessResponseModel success;
  protected IExceptionModel                  failure;
  protected boolean                          isAssetUploadSubmitedToBGP;
  
  @Override
  public IAssetUploadSuccessResponseModel getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(IAssetUploadSuccessResponseModel success)
  {
    this.success = success;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  } 
  
  @Override
  public boolean getIsAssetUploadSubmitedToBGP()
  {
    return isAssetUploadSubmitedToBGP;
  }
  
  @Override
  public void setIsAssetUploadSubmitedToBGP(boolean isAssetUploadSubmitedToBGP)
  {
    this.isAssetUploadSubmitedToBGP = isAssetUploadSubmitedToBGP;
  }
}
