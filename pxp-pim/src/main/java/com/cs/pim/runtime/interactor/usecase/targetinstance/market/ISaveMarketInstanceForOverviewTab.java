package com.cs.pim.runtime.interactor.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveMarketInstanceForOverviewTab
    extends IRuntimeInteractor<IMarketInstanceSaveModel, IGetKlassInstanceModel> {
  
}
