package com.cs.core.config.interactor.usecase.user;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetUsersByRole
    extends IConfigInteractor<IIdParameterModel, IGetGridUsersResponseModel> {
  
}
