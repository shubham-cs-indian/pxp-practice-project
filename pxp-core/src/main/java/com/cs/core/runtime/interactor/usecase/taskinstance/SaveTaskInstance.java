package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ISaveTaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.ISaveTaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SaveTaskInstance
    extends AbstractRuntimeInteractor<ISaveTaskInstanceModel, IGetTaskInstanceModel>
    implements ISaveTaskInstance {
  
  @Autowired
  protected ISaveTaskInstanceService saveTaskInstanceService;

  
  @Override
  public IGetTaskInstanceModel executeInternal(ISaveTaskInstanceModel dataModel) throws Exception
  {
    return saveTaskInstanceService.execute(dataModel);
  }

}
