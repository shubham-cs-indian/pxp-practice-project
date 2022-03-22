package com.cs.core.config.assetserver;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;

public interface IUploadMultipleAssetsToServerService extends ISaveConfigService<IUploadAssetModel, IBulkUploadResponseAssetModel> {
  
}
