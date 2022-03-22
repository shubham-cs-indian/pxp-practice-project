package com.cs.core.runtime.strategy.usecase.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetHashSearchResultModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetRedundancyCheckModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IAssetInstanceRedundancyCheckStrategy
    extends IRuntimeStrategy<IAssetRedundancyCheckModel, IAssetHashSearchResultModel> {
  
}
