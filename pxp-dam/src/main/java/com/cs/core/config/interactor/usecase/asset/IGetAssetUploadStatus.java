package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusResultModel;

public interface IGetAssetUploadStatus
    extends IGetConfigInteractor<IAssetUploadStatusCheckModel, IAssetUploadStatusResultModel> {
  
}
