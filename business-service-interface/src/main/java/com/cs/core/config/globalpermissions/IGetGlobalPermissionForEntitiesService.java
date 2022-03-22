package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForEntitiesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGlobalPermissionForEntitiesService
    extends IGetConfigService<IIdParameterModel, IGetGlobalPermissionForEntitiesModel> {
  
}
