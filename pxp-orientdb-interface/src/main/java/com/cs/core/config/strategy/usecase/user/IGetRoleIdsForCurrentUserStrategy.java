package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetRoleIdsForCurrentUserStrategy
    extends IConfigStrategy<IIdParameterModel, IIdsListParameterModel> {
  
}
