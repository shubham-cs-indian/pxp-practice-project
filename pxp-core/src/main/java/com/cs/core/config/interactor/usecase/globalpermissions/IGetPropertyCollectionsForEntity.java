package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;

public interface IGetPropertyCollectionsForEntity extends
    IGetConfigInteractor<IGetPropertyCollectionsForEntityModel, IGetGlobalPermissionsForRoleModel> {
  
}
