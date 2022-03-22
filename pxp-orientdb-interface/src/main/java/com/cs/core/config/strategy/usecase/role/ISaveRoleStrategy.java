package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveRoleStrategy extends IConfigStrategy<IRoleSaveModel, IGetRoleStrategyModel> {
  
}
