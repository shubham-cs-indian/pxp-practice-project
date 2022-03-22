package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateAssetInstanceVariantForLimitedObject extends
    IRuntimeInteractor<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
