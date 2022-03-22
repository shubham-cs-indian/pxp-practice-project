package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.config.interactor.model.asset.IAssetServerUploadResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IUploadAssetToServerStrategy
    extends IConfigStrategy<IAssetUploadDataModel, IAssetServerUploadResponseModel> {
  
}
