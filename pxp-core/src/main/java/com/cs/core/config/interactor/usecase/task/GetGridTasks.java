package com.cs.core.config.interactor.usecase.task;

import com.cs.core.config.businessapi.task.IGetGridTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;

@Service
public class GetGridTasks
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTasksResponseModel>
    implements IGetGridTasks {
  
  @Autowired
  protected IGetGridTasksService getGridTasksAPI;
  
  @Override
  public IGetGridTasksResponseModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getGridTasksAPI.execute(model);
  }
}
