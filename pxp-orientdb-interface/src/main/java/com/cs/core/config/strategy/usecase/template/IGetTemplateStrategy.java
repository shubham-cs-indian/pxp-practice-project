package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTemplateStrategy
    extends IConfigStrategy<IIdParameterModel, IGetCustomTemplateModel> {
  
}
