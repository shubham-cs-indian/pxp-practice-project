package com.cs.core.config.strategy.usecase.entityconfiguration;

import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;


public interface IGetEntityConfigurationStrategy extends IConfigStrategy<IGetEntityConfigurationRequestModel, IGetEntityConfigurationResponseModel> {

}
