package com.cs.core.config.interactor.usecase.governancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.task.ICreateGovernanceTaskResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.governancetask.ICreateGovernanceTaskStrategy;

@Service
public class CreateGovernanceTask
    extends AbstractCreateConfigInteractor<ITaskModel, ICreateGovernanceTaskResponseModel>
    implements ICreateGovernanceTask {
  
  @Autowired
  protected ICreateGovernanceTaskStrategy createGovernanceTaskStrategy;
  
  @Override
  public ICreateGovernanceTaskResponseModel executeInternal(ITaskModel model) throws Exception
  {
    return createGovernanceTaskStrategy.execute(model);
  }
}
