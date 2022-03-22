package com.cs.core.config.interactor.usecase.sso;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.sso.IGetIDPConfigurationResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetIDPConfiguration
    extends IGetConfigInteractor<IModel, IGetIDPConfigurationResponseModel> {
  
}
