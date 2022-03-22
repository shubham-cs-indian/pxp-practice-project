package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;

public interface IGetPropertiesOfPropertyCollection extends
    IGetConfigInteractor<IGetPropertiesOfPropertyCollectionModel, IGetGlobalPermissionsForRoleModel> {
  
}
