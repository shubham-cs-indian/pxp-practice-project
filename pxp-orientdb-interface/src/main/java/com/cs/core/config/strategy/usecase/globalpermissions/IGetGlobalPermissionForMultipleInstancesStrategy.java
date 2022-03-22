package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleEntityResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleInstancesRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGlobalPermissionForMultipleInstancesStrategy extends
    IConfigStrategy<IGetGlobalPermissionForMultipleInstancesRequestModel, IGetGlobalPermissionForMultipleEntityResponseModel> {
  
}
