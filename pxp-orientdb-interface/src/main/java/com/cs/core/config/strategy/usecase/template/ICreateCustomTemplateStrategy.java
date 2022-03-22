package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateCustomTemplateStrategy
    extends IConfigStrategy<ICustomTemplateModel, IGetCustomTemplateModel> {
  
}
