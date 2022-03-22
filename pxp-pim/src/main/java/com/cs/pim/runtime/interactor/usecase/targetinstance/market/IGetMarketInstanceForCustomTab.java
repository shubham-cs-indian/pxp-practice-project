package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetMarketInstanceForCustomTab
    extends IRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel> {
  
}
