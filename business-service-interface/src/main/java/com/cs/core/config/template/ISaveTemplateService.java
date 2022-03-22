package com.cs.core.config.template;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;

public interface ISaveTemplateService extends ISaveConfigService<ISaveCustomTemplateModel, IGetCustomTemplateModel> {
  
}
