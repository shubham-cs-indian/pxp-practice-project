package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateRoleCloneStrategy
    extends IConfigStrategy<ICreateRoleCloneModel, IGetRoleStrategyModel> {
  
}
