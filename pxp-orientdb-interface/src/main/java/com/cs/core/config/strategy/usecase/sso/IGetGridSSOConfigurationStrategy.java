package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridSSOConfigurationStrategy extends
    IConfigStrategy<IGetGridSSOConfigurationRequestModel, IGetGridSSOConfigurationResponseModel> {
  
}
