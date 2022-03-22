package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IGetInstancesDetailForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesForComparisonStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstancesDetailForComparisonStrategy extends
    IRuntimeStrategy<IGetInstancesDetailForComparisonModel, IKlassInstancesForComparisonStrategyModel> {
  
}
