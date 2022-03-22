package com.cs.dam.runtime.assetinstance;

import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceResponseModel;
import com.cs.core.config.interactor.model.asset.ICreateAssetInstanceAfterUploadRequestModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;


public interface ICreateAssetInstanceAfterUploadService extends
    IRuntimeService<ICreateAssetInstanceAfterUploadRequestModel, IBulkCreateAssetInstanceResponseModel> {
  
}
