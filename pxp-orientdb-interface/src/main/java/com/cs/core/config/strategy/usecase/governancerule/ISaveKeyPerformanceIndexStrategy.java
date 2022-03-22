package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.ISaveKPIResponseModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveKeyPerformanceIndexStrategy
    extends IConfigStrategy<ISaveKeyPerformanceIndexModel, ISaveKPIResponseModel> {
  
}
