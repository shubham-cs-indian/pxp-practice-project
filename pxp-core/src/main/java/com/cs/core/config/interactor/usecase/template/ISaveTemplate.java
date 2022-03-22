package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;

public interface ISaveTemplate
    extends ISaveConfigInteractor<ISaveCustomTemplateModel, IGetCustomTemplateModel> {
  
}
