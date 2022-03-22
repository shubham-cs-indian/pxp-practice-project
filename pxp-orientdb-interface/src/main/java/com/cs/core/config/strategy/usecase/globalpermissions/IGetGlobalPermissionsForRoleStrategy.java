package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGlobalPermissionsForRoleStrategy
    extends IConfigStrategy<IIdParameterModel, IGetGlobalPermissionsForRoleModel> {
  
}
