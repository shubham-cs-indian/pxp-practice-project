package com.cs.core.runtime.strategy.usecase.assetstatus;

import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IElasticGetAssetUploadStatusStrategy
    extends IConfigStrategy<IAssetUploadStatusCheckModel, IAssetUploadStatusModel> {
  
}
