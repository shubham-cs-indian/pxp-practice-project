package com.cs.core.runtime.interactor.usecase.variant.targetinstance.market;

import com.cs.core.runtime.interactor.model.targetinstance.IMarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveMarketInstanceForLanguageComparison
    extends IRuntimeInteractor<IMarketInstanceSaveModel, IGetKlassInstanceModel> {
  
}
