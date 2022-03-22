package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetSupplierWithGlobalPermission
    extends IGetConfigInteractor<IIdParameterModel, IGetKlassWithGlobalPermissionModel> {
  
}
