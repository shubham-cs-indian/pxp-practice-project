package com.cs.core.config.template;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;

public interface IBulkSaveTemplateService
    extends ISaveConfigService<IListModel<ISaveCustomTemplateModel>, IBulkSaveTemplatesResponseModel> {
  
}
