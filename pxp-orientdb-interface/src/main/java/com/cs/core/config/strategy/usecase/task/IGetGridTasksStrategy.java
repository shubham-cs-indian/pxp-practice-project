package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridTasksStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridTasksResponseModel> {
  
}
