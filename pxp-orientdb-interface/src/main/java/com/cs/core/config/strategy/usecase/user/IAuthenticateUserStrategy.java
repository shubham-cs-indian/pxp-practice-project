package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IAuthenticateUserStrategy extends IConfigStrategy<IUserModel, IUserModel> {
  
}
