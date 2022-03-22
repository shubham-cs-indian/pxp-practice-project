package com.cs.dam.runtime.assetinstance;

import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceRequestModel;
import com.cs.core.config.interactor.model.asset.IBulkCreateAssetInstanceResponseModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;


public interface IBulkCreateAssetInstanceService
    extends IRuntimeService<IBulkCreateAssetInstanceRequestModel, IBulkCreateAssetInstanceResponseModel> {
  
}
