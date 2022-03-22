package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllVariantContextStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllVariantContextsResponseModel> {
  
}
