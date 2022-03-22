package com.cs.core.config.template;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.customtemplate.ICustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;

public interface ICreateCustomTemplateService extends ICreateConfigService<ICustomTemplateModel, IGetCustomTemplateModel> {
  
}
