package com.cs.core.config.permission;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;

public interface IGetPermissionService
    extends IGetConfigService<IGetPermissionRequestModel, IGetPermissionModel> {
  
}
