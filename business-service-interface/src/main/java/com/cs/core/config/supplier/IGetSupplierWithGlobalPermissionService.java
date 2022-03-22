package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplierWithGlobalPermissionService
    extends IGetConfigService<IIdParameterModel, IGetKlassWithGlobalPermissionModel> {
  
}
