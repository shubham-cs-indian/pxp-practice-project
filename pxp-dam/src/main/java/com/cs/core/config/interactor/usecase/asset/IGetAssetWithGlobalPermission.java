package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAssetWithGlobalPermission
    extends ICreateConfigInteractor<IIdParameterModel, IGetKlassWithGlobalPermissionModel> {
  
}
