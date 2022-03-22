package com.cs.core.config.interactor.usecase.assetserver;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;

public interface IUploadMultipleAssetsToServer
    extends ISaveConfigInteractor<IUploadAssetModel, IBulkUploadResponseAssetModel> {
  
}
