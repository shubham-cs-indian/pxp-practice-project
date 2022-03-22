package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.GetGridTasksResponseModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGridTasksStrategy extends OrientDBBaseStrategy implements IGetGridTasksStrategy {
  
  @Override
  public IGetGridTasksResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_GRID_TASKS, model, GetGridTasksResponseModel.class);
  }
}
