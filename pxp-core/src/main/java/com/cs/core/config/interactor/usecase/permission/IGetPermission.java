package com.cs.core.config.interactor.usecase.permission;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;

public interface IGetPermission
    extends IGetConfigInteractor<IGetPermissionRequestModel, IGetPermissionModel> {
  
}
