package com.cs.core.config.interactor.usecase.assetserver;

import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;


public interface IUploadMultipleAssetsInForeground
    extends IRuntimeInteractor<IUploadAssetModel, IBulkUploadResponseAssetModel> {
  
}
