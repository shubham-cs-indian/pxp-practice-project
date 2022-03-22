package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetGovernanceTaskStrategy extends OrientDBBaseStrategy
    implements IGetGovernanceTaskStrategy {
  
  @Override
  public ITaskModel execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_GOVERNANCE_TASK, model, TaskModel.class);
  }
}
