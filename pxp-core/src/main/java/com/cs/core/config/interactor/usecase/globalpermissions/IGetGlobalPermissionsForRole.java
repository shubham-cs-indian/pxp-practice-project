package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGlobalPermissionsForRole
    extends IGetConfigInteractor<IIdParameterModel, IGetGlobalPermissionsForRoleModel> {
  
}
