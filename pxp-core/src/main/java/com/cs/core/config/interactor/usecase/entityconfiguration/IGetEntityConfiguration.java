package com.cs.core.config.interactor.usecase.entityconfiguration;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;

public interface IGetEntityConfiguration extends
    IGetConfigInteractor<IGetEntityConfigurationRequestModel, IGetEntityConfigurationResponseModel> {
  
}
