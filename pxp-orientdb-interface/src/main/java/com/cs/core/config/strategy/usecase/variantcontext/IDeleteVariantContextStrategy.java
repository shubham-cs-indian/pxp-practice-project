package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteVariantContextStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteVariantContextReturnModel> {
  
}
