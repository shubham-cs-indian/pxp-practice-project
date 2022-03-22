package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;

public interface IGetGlobalPermissionWithAllowedTemplatesService extends
    IGetConfigService<IGetGlobalPermissionWithAllowedTemplatesRequestModel, IGetGlobalPermissionWithAllowedTemplatesModel> {
  
}
