package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridGovernanceTasksStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridTasksResponseModel> {
  
}
