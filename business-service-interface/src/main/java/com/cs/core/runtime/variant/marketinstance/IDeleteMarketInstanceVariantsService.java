package com.cs.core.runtime.variant.marketinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;

public interface IDeleteMarketInstanceVariantsService extends IRuntimeService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> {
  
}
