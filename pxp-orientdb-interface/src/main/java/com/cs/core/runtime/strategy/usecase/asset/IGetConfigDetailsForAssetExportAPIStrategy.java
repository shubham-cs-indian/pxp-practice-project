package com.cs.core.runtime.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IAssetExportAPIRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;


public interface IGetConfigDetailsForAssetExportAPIStrategy extends IConfigStrategy<IAssetExportAPIRequestModel, IAssetExportAPIResponseModel> {
  
}
