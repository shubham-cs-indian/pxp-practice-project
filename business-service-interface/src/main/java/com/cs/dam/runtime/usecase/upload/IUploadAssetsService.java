package com.cs.dam.runtime.usecase.upload;

import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.runtime.interactor.model.assetupload.IUploadAssetResponseModel;

public interface IUploadAssetsService {
  
  public IUploadAssetResponseModel execute(IUploadAssetModel requestModel) throws Exception;
}
