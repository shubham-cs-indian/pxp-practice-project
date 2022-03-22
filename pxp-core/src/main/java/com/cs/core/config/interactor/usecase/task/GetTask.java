package com.cs.core.config.interactor.usecase.task;

import com.cs.core.config.businessapi.task.IGetTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTask extends AbstractGetConfigInteractor<IIdParameterModel, ITaskModel>
    implements IGetTask {
  
  @Autowired
  IGetTaskService getTaskAPI;
  
  @Override
  public ITaskModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getTaskAPI.execute(model);
  }
}
