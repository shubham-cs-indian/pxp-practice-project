package com.cs.core.runtime.interactor.usecase.login;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;

public interface IAuthenticateUser extends IGetConfigInteractor<IUserModel, IUserModel> {
}
