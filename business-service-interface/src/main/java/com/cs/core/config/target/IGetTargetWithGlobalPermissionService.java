package com.cs.core.config.target;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTargetWithGlobalPermissionService
    extends IGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel> {
  
}
