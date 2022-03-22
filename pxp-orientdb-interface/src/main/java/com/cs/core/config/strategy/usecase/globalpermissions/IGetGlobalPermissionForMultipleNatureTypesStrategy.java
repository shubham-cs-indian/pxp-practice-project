package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGlobalPermissionForMultipleNatureTypesStrategy extends
    IConfigStrategy<IGetGlobalPermissionForMultipleNatureTypesRequestModel, IGetGlobalPermissionForMultipleNatureTypesResponseModel> {
  
}
