package com.cs.core.config.interactor.usecase.user;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetUser extends IConfigInteractor<IIdParameterModel, IUserModel> {
  
}
