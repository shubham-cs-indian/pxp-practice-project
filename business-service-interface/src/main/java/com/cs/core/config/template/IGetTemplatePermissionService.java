package com.cs.core.config.template;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;

public interface IGetTemplatePermissionService
    extends IGetConfigService<IPermissionRequestModel, IGetTemplatePermissionModel> {
  
}
