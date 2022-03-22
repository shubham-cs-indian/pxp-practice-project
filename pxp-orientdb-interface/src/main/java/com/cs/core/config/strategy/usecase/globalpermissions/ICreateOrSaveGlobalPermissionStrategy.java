package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.ICreateOrSaveGlobalPermissionResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.ISaveGlobalPermissionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateOrSaveGlobalPermissionStrategy extends
    IConfigStrategy<ISaveGlobalPermissionModel, ICreateOrSaveGlobalPermissionResponseModel> {
  
}
