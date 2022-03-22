package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstanceForVersionTabStrategy extends
    IRuntimeStrategy<IGetInstanceRequestStrategyModelForCustomTab, IGetKlassInstanceVersionsForTimeLineModel> {
  
}
