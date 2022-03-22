package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;

public interface IGetGlobalPermissionWithAllowedTemplates extends
    IGetConfigInteractor<IGetGlobalPermissionWithAllowedTemplatesRequestModel, IGetGlobalPermissionWithAllowedTemplatesModel> {
  
}
