package com.cs.core.config.template;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;

public interface ISaveTemplatePermissionService extends ISaveConfigService<ISaveTemplatePermissionModel, IGetPermissionModel> {
  
}
