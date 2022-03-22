package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGlobalPermissionsForRoleService
    extends IGetConfigService<IIdParameterModel, IGetGlobalPermissionsForRoleModel> {
  
}
