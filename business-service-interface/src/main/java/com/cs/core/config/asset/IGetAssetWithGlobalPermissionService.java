package com.cs.core.config.asset;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAssetWithGlobalPermissionService
    extends ICreateConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel> {
  
}
