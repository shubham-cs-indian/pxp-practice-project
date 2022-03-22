package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;

public interface ICreateCustomTemplate
    extends ICreateConfigInteractor<ICustomTemplateModel, IGetCustomTemplateModel> {
  
}
