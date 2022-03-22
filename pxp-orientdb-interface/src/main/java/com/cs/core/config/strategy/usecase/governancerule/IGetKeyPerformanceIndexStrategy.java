package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKeyPerformanceIndexStrategy
    extends IConfigStrategy<IIdParameterModel, IGetKeyPerformanceIndexModel> {
  
}
