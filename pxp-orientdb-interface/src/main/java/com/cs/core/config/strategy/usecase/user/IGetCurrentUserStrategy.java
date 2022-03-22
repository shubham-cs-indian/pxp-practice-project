package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.user.IGetCurrentUserModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetCurrentUserStrategy
    extends IConfigStrategy<IIdParameterModel, IGetCurrentUserModel> {
  
}
