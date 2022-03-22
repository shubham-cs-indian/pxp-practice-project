package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;

public interface ICreateOrSaveGlobalPermissionService extends
    ICreateConfigService<ISaveGlobalPermissionModel, ICreateOrSaveGlobalPermissionResponseModel> {
  
}
