package com.cs.core.config.interactor.usecase.governancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.strategy.usecase.governancetask.IDeleteGovernanceTasksStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteGovernanceTasks
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteGovernanceTasks {
  
  @Autowired
  protected IDeleteGovernanceTasksStrategy deleteGovernanceTasksStrategy;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteGovernanceTasksStrategy.execute(dataModel);
  }
}
