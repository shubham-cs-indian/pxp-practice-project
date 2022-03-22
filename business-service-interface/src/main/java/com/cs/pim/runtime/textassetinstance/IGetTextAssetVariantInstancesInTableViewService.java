package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;

public interface IGetTextAssetVariantInstancesInTableViewService extends
    IRuntimeService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
