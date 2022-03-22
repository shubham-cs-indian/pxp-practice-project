package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetFunctionPermissionForRoleStrategy extends IConfigStrategy<IIdParameterModel, IFunctionPermissionModel>{
  
}
