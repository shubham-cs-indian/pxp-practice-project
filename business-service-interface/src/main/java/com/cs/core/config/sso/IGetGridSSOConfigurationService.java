package com.cs.core.config.sso;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;

public interface IGetGridSSOConfigurationService extends
    IGetConfigService<IGetGridSSOConfigurationRequestModel, IGetGridSSOConfigurationResponseModel> {
  
}
