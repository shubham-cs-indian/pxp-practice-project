package com.cs.core.config.sso;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationResponseModel;

public interface ICreateSSOConfigurationService
    extends ICreateConfigService<ICreateSSOConfigurationModel, ICreateSSOConfigurationResponseModel> {
  
}
