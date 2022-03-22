package com.cs.core.config.interactor.usecase.entityconfiguration;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;

public abstract class AbstractGetConfigEntityConfiguration extends
    AbstractGetConfigInteractor<IGetEntityConfigurationRequestModel, IGetEntityConfigurationResponseModel> {
  
  protected abstract IGetEntityConfigurationResponseModel executeInternal(
      IGetEntityConfigurationRequestModel model) throws Exception;
  
}
