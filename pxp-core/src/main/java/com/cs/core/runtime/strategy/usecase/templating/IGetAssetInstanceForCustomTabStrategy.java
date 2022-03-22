package com.cs.core.runtime.strategy.usecase.templating;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetAssetInstanceForCustomTabStrategy extends
    IRuntimeStrategy<IGetInstanceRequestStrategyModelForCustomTab, IGetKlassInstanceCustomTabModel> {
  
}
