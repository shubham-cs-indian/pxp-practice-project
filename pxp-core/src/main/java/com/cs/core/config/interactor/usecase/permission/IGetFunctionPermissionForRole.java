package com.cs.core.config.interactor.usecase.permission;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetFunctionPermissionForRole extends IConfigInteractor<IIdParameterModel, IFunctionPermissionModel> {
  
}
