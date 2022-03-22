package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAssetVariantInstancesInTableView extends
    IRuntimeInteractor<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
