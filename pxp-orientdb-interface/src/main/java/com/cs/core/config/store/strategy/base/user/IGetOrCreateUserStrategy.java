package com.cs.core.config.store.strategy.base.user;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateUserStrategy extends IConfigStrategy<IUserModel, IUserModel> {
  
}
