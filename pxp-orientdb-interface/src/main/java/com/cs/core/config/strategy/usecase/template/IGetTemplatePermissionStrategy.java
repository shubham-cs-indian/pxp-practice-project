package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetTemplatePermissionStrategy
    extends IConfigStrategy<IPermissionRequestModel, IGetTemplatePermissionModel> {
  
}
