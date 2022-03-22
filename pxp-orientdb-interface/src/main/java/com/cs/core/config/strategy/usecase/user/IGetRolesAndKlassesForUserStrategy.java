package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.role.IGetRolesAndKlassesForUserModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetRolesAndKlassesForUserStrategy
    extends IConfigStrategy<IIdParameterModel, IGetRolesAndKlassesForUserModel> {
  
}
