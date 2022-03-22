package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetTaskStrategy extends OrientDBBaseStrategy implements IGetTaskStrategy {
  
  @Override
  public ITaskModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_TASK, model, TaskModel.class);
  }
}
