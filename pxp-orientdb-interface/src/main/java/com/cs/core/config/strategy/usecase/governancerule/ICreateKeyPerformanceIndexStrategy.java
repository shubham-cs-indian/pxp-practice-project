package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateKeyPerformanceIndexStrategy
    extends IConfigStrategy<ICreateKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel> {
  
}
