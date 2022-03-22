package com.cs.core.config.asset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusResultModel;

public interface IGetAssetUploadStatusService
    extends IGetConfigService<IAssetUploadStatusCheckModel, IAssetUploadStatusResultModel> {
  
}
