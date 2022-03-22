package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetConfigDetailsForTasksDashboardModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetConfigDetailsForTasksDashboardModel;
import org.springframework.stereotype.Component;

@Component
public class GetAllTasksForDashboardStrategy extends OrientDBBaseStrategy
    implements IGetAllTasksForDashboardStrategy {
  
  @Override
  public IGetConfigDetailsForTasksDashboardModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_FOR_TASK_DASHBOARD, model,
        GetConfigDetailsForTasksDashboardModel.class);
  }
}
