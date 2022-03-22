package com.cs.core.config.interactor.usecase.task;

import com.cs.core.config.businessapi.task.ICreateTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

@Service
public class CreateTask extends AbstractCreateConfigInteractor<ITaskModel, IGetTaskModel>
    implements ICreateTask {
  
  @Autowired ICreateTaskService createTaskAPI;
  
  @Override
  public IGetTaskModel executeInternal(ITaskModel model) throws Exception
  {
   return createTaskAPI.execute(model);
  }
}
