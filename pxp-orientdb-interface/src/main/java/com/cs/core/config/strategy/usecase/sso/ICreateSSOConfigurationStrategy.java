package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateSSOConfigurationStrategy
    extends IConfigStrategy<ICreateSSOConfigurationModel, ICreateSSOConfigurationResponseModel> {
  
}
