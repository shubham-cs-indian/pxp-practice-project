package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IBulkDeleteTaskInstanceReturnModel;
import com.cs.core.runtime.interactor.model.taskinstance.IDeleteTaskInstancesRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.IDeleteTaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unchecked")
@Service
public class DeleteTaskInstance extends
    AbstractRuntimeInteractor<IDeleteTaskInstancesRequestModel, IBulkDeleteTaskInstanceReturnModel>
    implements IDeleteTaskInstance {
  
  @Autowired
  protected IDeleteTaskInstanceService deleteTaskInstanceService;

  @Override
  public IBulkDeleteTaskInstanceReturnModel executeInternal(IDeleteTaskInstancesRequestModel dataModel) throws Exception
  {

    return deleteTaskInstanceService.execute(dataModel);
  }

}
