package com.cs.core.config.permission;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetFunctionPermissionForRoleService extends IGetConfigService<IIdParameterModel, IFunctionPermissionModel> {
  
}
