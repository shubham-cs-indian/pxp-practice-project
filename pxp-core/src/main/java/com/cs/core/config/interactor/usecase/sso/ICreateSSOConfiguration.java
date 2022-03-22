package com.cs.core.config.interactor.usecase.sso;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationResponseModel;

public interface ICreateSSOConfiguration
    extends ICreateConfigInteractor<ICreateSSOConfigurationModel, ICreateSSOConfigurationResponseModel> {
  
}
