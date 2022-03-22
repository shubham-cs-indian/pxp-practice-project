package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertiesOfPropertyCollectionModel;

public interface IGetPropertiesOfPropertyCollectionService extends
    IGetConfigService<IGetPropertiesOfPropertyCollectionModel, IGetGlobalPermissionsForRoleModel> {
  
}
