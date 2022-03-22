package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkDeleteInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IDeleteInstanceVariantsStrategy extends
    IRuntimeStrategy<IDeleteVariantStrategyModel, IBulkDeleteInstanceVariantsResponseModel> {
  
}
