package com.cs.core.runtime.strategy.usecase.assetstatus;

import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ISetAssetUploadStatusStrategy
    extends IRuntimeStrategy<IAssetUploadStatusModel, IAssetUploadStatusModel> {
  
}
