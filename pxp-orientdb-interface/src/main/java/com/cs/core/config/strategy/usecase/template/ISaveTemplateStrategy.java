package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveTemplateStrategy
    extends IConfigStrategy<ISaveCustomTemplateModel, IGetCustomTemplateModel> {
  
}
