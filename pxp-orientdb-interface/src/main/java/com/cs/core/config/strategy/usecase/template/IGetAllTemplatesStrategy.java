package com.cs.core.config.strategy.usecase.template;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllTemplatesStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridTemplatesResponseModel> {
  
}
