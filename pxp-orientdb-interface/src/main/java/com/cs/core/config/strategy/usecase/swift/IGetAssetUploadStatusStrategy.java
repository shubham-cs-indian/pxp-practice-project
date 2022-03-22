package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusResultModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAssetUploadStatusStrategy
    extends IConfigStrategy<IAssetUploadStatusCheckModel, IAssetUploadStatusResultModel> {
  
}
