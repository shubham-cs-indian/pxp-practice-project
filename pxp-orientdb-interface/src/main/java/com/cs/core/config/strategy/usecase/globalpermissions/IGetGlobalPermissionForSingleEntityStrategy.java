package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForSingleEntityModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGlobalPermissionForSingleEntityStrategy extends
    IConfigStrategy<IGetGlobalPermissionForSingleEntityModel, IGlobalPermissionWithAllowedModuleEntitiesModel> {
  
}
