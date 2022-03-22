package com.cs.core.runtime.variant.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;

public interface ICreateAssetInstanceVariantForLimitedObjectService extends
    IRuntimeService<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
