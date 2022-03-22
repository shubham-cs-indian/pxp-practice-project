package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetUserService extends IConfigService<IIdParameterModel, IUserModel> {
  
}
