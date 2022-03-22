package com.cs.core.config.strategy.usecase.governancetask;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.task.CreateGovernanceTaskResponseModel;
import com.cs.core.config.interactor.model.task.ICreateGovernanceTaskResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class CreateGovernanceTaskStrategy extends OrientDBBaseStrategy
    implements ICreateGovernanceTaskStrategy {
  
  @Override
  public ICreateGovernanceTaskResponseModel execute(ITaskModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.CREATE_GOVERNANCE_TASK, model, CreateGovernanceTaskResponseModel.class);
  }
}
