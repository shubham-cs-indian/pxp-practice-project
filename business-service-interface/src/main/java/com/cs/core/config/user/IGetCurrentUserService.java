package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetCurrentUserService
    extends IConfigService<IIdParameterModel, IUserInformationModel> {
  
}
