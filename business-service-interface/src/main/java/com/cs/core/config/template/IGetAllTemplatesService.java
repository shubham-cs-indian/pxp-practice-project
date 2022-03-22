package com.cs.core.config.template;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;

public interface IGetAllTemplatesService extends IGetConfigService<IConfigGetAllRequestModel, IGetGridTemplatesResponseModel> {
  
}
