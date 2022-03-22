package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.config.strategy.model.governancerule.IBulkDeleteKeyPerformanceIndexStrategyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteKeyPerformanceIndexStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteKeyPerformanceIndexStrategyModel> {
  
}
