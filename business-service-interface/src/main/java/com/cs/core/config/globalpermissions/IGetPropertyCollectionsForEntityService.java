package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionsForRoleModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;

public interface IGetPropertyCollectionsForEntityService extends
    IGetConfigService<IGetPropertyCollectionsForEntityModel, IGetGlobalPermissionsForRoleModel> {
  
}
