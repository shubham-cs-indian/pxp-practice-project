package com.cs.core.runtime.interactor.usecase.variant.marketinstance;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteMarketInstanceVariants extends
    IRuntimeInteractor<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> {
  
}
