package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;

public interface IGetTemplatePermission
    extends IGetConfigInteractor<IPermissionRequestModel, IGetTemplatePermissionModel> {
  
}
