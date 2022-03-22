package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;


public interface IBulkAssetDownloadResponseModel extends IBulkResponseModel {
  
  public void setSuccess(IAssetDownloadModel success);  
}
