package com.cs.core.runtime.interactor.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.IGetTaskInstanceService;

@Service
public class GetTaskInstance extends AbstractRuntimeInteractor<IIdParameterModel, IGetTaskInstanceModel> implements IGetTaskInstance {
  
  @Autowired
  protected IGetTaskInstanceService getTaskInstanceService;
  
  @Override
  public IGetTaskInstanceModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getTaskInstanceService.execute(dataModel);
  }
  
}
