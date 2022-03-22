package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;

public interface IBulkSaveTemplate extends
    ISaveConfigInteractor<IListModel<ISaveCustomTemplateModel>, IBulkSaveTemplatesResponseModel> {
  
}
