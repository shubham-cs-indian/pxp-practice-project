package com.cs.core.config.interactor.usecase.assetserver;

import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkAssetDownload
    extends IRuntimeInteractor<IIdParameterModel, IGetAssetDetailsResponseModel> {
  
}
