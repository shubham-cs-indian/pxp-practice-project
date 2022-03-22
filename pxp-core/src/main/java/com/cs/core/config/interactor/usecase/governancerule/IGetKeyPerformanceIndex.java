package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKeyPerformanceIndex
    extends IGetConfigInteractor<IIdParameterModel, IGetKeyPerformanceIndexModel> {
  
}
