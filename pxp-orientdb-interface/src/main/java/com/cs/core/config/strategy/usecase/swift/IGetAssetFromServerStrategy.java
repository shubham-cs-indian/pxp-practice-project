package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsStrategyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAssetFromServerStrategy
    extends IConfigStrategy<IGetAssetDetailsRequestModel, IGetAssetDetailsStrategyModel> {
  
}
