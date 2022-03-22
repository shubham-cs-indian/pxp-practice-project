package com.cs.core.config.assetserver;

import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;

public interface IUploadMultipleAssetsInForegroundService extends IRuntimeService<IUploadAssetModel, IBulkUploadResponseAssetModel> {
  
}
