package com.cs.core.runtime.variant.marketinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;

public interface ICreateMarketInstanceVariantForLimitedObjectService extends
    IRuntimeService<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
