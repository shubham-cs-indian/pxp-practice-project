package com.cs.core.runtime.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDAMConfigurationDetailsStrategy
    extends IConfigStrategy<IIdParameterModel, IDAMConfigurationDetailsResponseModel> {
  
}
