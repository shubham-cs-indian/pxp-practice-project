package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.task.GetGridTasksResponseModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetGridGovernanceTasksStrategy extends OrientDBBaseStrategy
    implements IGetGridGovernanceTasksStrategy {
  
  @Override
  public IGetGridTasksResponseModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GRID_GOVERNANCE_TASKS, model,
        GetGridTasksResponseModel.class);
  }
}
