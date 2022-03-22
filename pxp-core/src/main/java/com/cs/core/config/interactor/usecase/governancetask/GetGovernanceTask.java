package com.cs.core.config.interactor.usecase.governancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.governancetask.IGetGovernanceTaskStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetGovernanceTask extends AbstractGetConfigInteractor<IIdParameterModel, ITaskModel>
    implements IGetGovernanceTask {
  
  @Autowired
  protected IGetGovernanceTaskStrategy getGovernanceTaskStrategy;
  
  @Override
  public ITaskModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getGovernanceTaskStrategy.execute(model);
  }
}
