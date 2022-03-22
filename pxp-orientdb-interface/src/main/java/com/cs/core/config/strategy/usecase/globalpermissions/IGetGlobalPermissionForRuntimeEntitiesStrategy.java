package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGlobalPermissionForRuntimeEntitiesStrategy extends
    IConfigStrategy<IGetGlobalPermissionModel, IGetGlobalPermissionForRuntimeEntitiesResponseModel> {
  
}
