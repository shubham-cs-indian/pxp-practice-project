package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ISaveMarketInstanceForOverviewTabService
    extends IRuntimeService<IMarketInstanceSaveModel, IGetKlassInstanceModel> {
  
}
