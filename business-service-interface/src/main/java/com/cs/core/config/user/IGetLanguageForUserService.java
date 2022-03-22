package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.user.IUserLanguageModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetLanguageForUserService extends IConfigService<IIdParameterModel, IUserLanguageModel> {
  
}
