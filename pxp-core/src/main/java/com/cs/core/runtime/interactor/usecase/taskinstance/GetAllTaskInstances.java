package com.cs.core.runtime.interactor.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceForDashboardModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.IGetAllTaskInstancesService;

@Service
public class GetAllTaskInstances extends AbstractRuntimeInteractor<IIdParameterModel, IGetTaskInstanceForDashboardModel>
    implements IGetAllTaskInstances {
  
  @Autowired
  protected IGetAllTaskInstancesService getAllTaskInstancesService;
  
  @Override
  public IGetTaskInstanceForDashboardModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllTaskInstancesService.execute(dataModel);
  }
}
