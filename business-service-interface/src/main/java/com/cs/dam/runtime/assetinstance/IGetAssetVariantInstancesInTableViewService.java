package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;

public interface IGetAssetVariantInstancesInTableViewService extends
    IRuntimeService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
