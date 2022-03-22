package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface ISaveTemplatePermissionStrategy
    extends IConfigStrategy<ISaveTemplatePermissionModel, IIdParameterModel> {
  
}
