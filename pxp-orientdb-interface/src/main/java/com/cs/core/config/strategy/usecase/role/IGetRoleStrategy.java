package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRoleStrategy
    extends IConfigStrategy<IIdParameterModel, IGetRoleStrategyModel> {
  
}
