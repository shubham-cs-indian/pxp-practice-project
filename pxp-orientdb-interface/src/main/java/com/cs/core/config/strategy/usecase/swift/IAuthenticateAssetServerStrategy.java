package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAuthenticateAssetServerStrategy
    extends IConfigStrategy<IModel, IAssetServerDetailsModel> {
  
}
