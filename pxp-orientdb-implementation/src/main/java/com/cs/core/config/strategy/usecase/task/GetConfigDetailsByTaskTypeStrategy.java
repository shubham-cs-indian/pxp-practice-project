package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.CreateTaskInstanceConfigDetailsModel;
import com.cs.core.runtime.interactor.model.taskinstance.ICreateTaskInstanceConfigDetailsModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsByTaskTypeStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsByTaskTypeStrategy {
  
  @Override
  public ICreateTaskInstanceConfigDetailsModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_CONFIG_DETAILS_BY_TASK_TYPE, model,
        CreateTaskInstanceConfigDetailsModel.class);
  }
}
