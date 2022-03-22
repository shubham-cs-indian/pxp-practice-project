package com.cs.core.config.interactor.usecase.template;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;

public interface IGetAllTemplates
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTemplatesResponseModel> {
  
}
