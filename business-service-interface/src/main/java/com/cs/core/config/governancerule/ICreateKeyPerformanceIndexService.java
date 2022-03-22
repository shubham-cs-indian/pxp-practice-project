package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;

public interface ICreateKeyPerformanceIndexService
    extends ICreateConfigService<ICreateKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel> {
  
}
