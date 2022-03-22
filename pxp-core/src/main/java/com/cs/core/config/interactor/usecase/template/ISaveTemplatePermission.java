package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;

public interface ISaveTemplatePermission
    extends ISaveConfigInteractor<ISaveTemplatePermissionModel, IGetPermissionModel> {
  
}
