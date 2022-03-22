package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;

public interface IGetAssetFromServer
    extends IGetConfigInteractor<IGetAssetDetailsRequestModel, IGetAssetDetailsResponseModel> {
  
}
