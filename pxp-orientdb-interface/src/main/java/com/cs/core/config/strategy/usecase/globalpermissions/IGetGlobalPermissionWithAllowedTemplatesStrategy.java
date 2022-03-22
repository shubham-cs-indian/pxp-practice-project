package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGlobalPermissionWithAllowedTemplatesStrategy extends
    IConfigStrategy<IGetGlobalPermissionWithAllowedTemplatesRequestModel, IGetGlobalPermissionWithAllowedTemplatesModel> {
  
}
