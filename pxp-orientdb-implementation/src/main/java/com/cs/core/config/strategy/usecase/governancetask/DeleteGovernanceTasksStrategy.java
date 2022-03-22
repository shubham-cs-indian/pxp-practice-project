package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteGovernanceTasksStrategy extends OrientDBBaseStrategy
    implements IDeleteGovernanceTasksStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(OrientDBBaseStrategy.DELETE_GOVERNANCE_TASKS, model,
        BulkDeleteReturnModel.class);
  }
}
