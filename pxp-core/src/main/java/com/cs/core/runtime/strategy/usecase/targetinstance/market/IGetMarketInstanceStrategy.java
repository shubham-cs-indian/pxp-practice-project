package com.cs.core.runtime.strategy.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetMarketInstanceStrategy
    extends IRuntimeStrategy<IGetKlassInstanceTreeStrategyModel, IGetKlassInstanceTreeModel>{
  
}
