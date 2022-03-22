package com.cs.core.config.sso;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.sso.IGetIDPConfigurationResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetIDPConfigurationService
    extends IGetConfigService<IModel, IGetIDPConfigurationResponseModel> {
  
}
