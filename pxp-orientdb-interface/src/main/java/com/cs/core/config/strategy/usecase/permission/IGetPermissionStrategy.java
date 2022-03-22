package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPermissionStrategy
    extends IConfigStrategy<IGetPermissionRequestModel, IGetPermissionModel> {
  
}
