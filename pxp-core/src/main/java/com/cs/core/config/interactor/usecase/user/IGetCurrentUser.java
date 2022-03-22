package com.cs.core.config.interactor.usecase.user;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetCurrentUser
    extends IConfigInteractor<IIdParameterModel, IUserInformationModel> {
  
}
