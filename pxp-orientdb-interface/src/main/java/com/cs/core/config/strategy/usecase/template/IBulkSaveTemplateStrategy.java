package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IBulkSaveTemplateStrategy
    extends IConfigStrategy<IListModel<ISaveCustomTemplateModel>, IBulkSaveTemplatesResponseModel> {
  
}
