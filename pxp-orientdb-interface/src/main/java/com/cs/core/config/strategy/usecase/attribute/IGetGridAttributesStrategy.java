package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridAttributesStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridAttributesResponseModel> {
  
}
