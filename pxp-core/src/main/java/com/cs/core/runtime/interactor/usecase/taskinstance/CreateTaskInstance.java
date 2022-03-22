package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.ICreateTaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskInstance
    extends AbstractRuntimeInteractor<ITaskInstanceModel, IGetTaskInstanceModel>
    implements ICreateTaskInstance {
  
  @Autowired
  protected ICreateTaskInstanceService createTaskInstanceService;
  
  @Override
  public IGetTaskInstanceModel executeInternal(ITaskInstanceModel dataModel) throws Exception
  {
   return createTaskInstanceService.execute(dataModel);
  } 

}
