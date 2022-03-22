package com.cs.core.config.template;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTemplateService extends IGetConfigService<IIdParameterModel, IGetCustomTemplateModel> {
  
}
