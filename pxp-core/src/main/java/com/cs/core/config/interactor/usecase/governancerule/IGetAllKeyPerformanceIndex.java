package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.governancerule.IGetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllKeyPerformanceIndex
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllKeyPerformanceIndexModel> {
  
}
