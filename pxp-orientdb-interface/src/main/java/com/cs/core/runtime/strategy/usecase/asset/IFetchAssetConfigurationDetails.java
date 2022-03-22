package com.cs.core.runtime.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IFetchAssetConfigurationDetails
    extends IConfigStrategy<IIdParameterModel, IAssetConfigurationDetailsResponseModel> {
  
}
