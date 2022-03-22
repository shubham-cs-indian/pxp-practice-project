package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.task.GetTaskModel;
import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateTaskStrategy extends OrientDBBaseStrategy implements ICreateTaskStrategy {
  
  @Override
  public IGetTaskModel execute(ITaskModel model) throws Exception
  {
    return execute(CREATE_TASK, model, GetTaskModel.class);
  }
}
