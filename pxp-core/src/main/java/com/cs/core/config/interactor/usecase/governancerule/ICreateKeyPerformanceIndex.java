package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;

public interface ICreateKeyPerformanceIndex
    extends ICreateConfigInteractor<ICreateKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel> {
  
}
