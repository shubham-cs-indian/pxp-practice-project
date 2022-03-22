package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.ISaveDAMConfigurationRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;


public interface ISaveDAMConfigurationStrategy extends
    IConfigStrategy<ISaveDAMConfigurationRequestModel, IDAMConfigurationDetailsResponseModel> {
  
}
