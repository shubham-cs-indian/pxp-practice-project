package com.cs.core.config.strategy.usecase.governancerule;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.CreateTaskInstanceConfigDetailsModel;
import com.cs.core.runtime.interactor.model.taskinstance.ICreateTaskInstanceConfigDetailsModel;
import org.springframework.stereotype.Component;

@Component
public class GetConfigDetailsByGovernanceTaskTypeStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsByGovernanceTaskTypeStrategy {
  
  @Override
  public ICreateTaskInstanceConfigDetailsModel execute(IIdParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_CONFIG_DETAILS_BY_GOVERNANCE_TASK_TYPE, model,
        CreateTaskInstanceConfigDetailsModel.class);
  }
}
