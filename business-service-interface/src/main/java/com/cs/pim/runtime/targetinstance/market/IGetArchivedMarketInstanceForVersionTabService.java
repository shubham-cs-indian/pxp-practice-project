package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;

public interface IGetArchivedMarketInstanceForVersionTabService extends
    IRuntimeService<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel> {
  
}
