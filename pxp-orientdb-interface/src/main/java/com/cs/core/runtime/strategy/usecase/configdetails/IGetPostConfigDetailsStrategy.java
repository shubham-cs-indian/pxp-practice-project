package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPostConfigDetailsStrategy
    extends IConfigStrategy<IGetPostConfigDetailsRequestModel, IGetPostConfigDetailsResponseModel> {
  
}
