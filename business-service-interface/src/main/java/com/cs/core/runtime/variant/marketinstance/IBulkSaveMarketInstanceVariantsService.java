package com.cs.core.runtime.variant.marketinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;

public interface IBulkSaveMarketInstanceVariantsService
    extends IRuntimeService<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel> {
  
}
