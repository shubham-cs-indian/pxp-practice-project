package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateRoleStrategy
    extends IConfigStrategy<ICreateRoleModel, IGetRoleStrategyModel> {
  
}
