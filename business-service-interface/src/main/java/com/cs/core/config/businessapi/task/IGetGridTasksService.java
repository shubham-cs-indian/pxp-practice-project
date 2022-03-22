package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;

public interface IGetGridTasksService extends IGetConfigService<IConfigGetAllRequestModel, IGetGridTasksResponseModel>{

}
