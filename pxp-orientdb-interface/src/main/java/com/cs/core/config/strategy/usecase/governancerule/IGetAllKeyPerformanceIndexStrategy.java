package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.interactor.model.governancerule.IGetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllKeyPerformanceIndexStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllKeyPerformanceIndexModel> {
  
}
