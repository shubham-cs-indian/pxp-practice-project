package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTemplate
    extends IGetConfigInteractor<IIdParameterModel, IGetCustomTemplateModel> {
  
}
