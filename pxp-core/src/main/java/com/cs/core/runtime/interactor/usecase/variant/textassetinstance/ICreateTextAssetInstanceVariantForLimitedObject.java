package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTextAssetInstanceVariantForLimitedObject extends
    IRuntimeInteractor<ICreateVariantForLimitedObjectRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
