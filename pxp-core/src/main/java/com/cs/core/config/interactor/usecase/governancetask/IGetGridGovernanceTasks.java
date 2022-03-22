package com.cs.core.config.interactor.usecase.governancetask;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;

public interface IGetGridGovernanceTasks
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTasksResponseModel> {
  
}
