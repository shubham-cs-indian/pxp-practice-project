package com.cs.core.config.interactor.usecase.sso;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationRequestModel;
import com.cs.core.config.interactor.model.sso.IGetGridSSOConfigurationResponseModel;

public interface IGetGridSSOConfiguration extends
    IGetConfigInteractor<IGetGridSSOConfigurationRequestModel, IGetGridSSOConfigurationResponseModel> {
  
}
