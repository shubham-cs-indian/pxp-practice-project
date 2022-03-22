package com.cs.core.config.interactor.model.configdetails;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadSuccessResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IBulkUploadResponseAssetModel extends IModel, IConfigResponseWithAuditLogModel {

  public IAssetUploadSuccessResponseModel getSuccess();
  
  public void setSuccess(IAssetUploadSuccessResponseModel dataModel);
  
  public IExceptionModel getFailure();
  
  public void setFailure(IExceptionModel failure);
  
  public boolean getIsAssetUploadSubmitedToBGP();
  
  public void setIsAssetUploadSubmitedToBGP(boolean isAssetUploadSubmitedToBGP);
}
