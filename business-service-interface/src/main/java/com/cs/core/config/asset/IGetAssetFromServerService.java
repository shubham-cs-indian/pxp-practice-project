package com.cs.core.config.asset;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;

public interface IGetAssetFromServerService
    extends IGetConfigService<IGetAssetDetailsRequestModel, IGetAssetDetailsResponseModel> {
  
}
