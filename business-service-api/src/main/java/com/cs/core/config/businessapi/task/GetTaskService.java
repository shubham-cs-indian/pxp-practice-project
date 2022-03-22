package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.IGetTaskStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTaskService extends AbstractGetConfigService<IIdParameterModel, ITaskModel>implements IGetTaskService {
  
  @Autowired
  IGetTaskStrategy getTaskStrategy;
  
  @Override
  public ITaskModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getTaskStrategy.execute(model);
  }
}
