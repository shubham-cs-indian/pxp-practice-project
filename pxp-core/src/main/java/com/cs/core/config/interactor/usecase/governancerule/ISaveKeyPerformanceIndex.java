package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;

public interface ISaveKeyPerformanceIndex
    extends ISaveConfigInteractor<ISaveKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel> {
  
}
