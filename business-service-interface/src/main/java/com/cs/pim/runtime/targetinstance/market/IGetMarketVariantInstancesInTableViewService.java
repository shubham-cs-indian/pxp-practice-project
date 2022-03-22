package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;

public interface IGetMarketVariantInstancesInTableViewService extends
    IRuntimeService<IGetVariantInstanceInTableViewRequestModel, IGetVariantInstancesInTableViewModel> {
  
}
