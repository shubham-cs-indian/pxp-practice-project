package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IPrepareDataForContextualDataTransferTask;

public class PrepareDataForContextualDataTransferTask extends
    AbstractRuntimeInteractor<IModel, IModel> implements IPrepareDataForContextualDataTransferTask {
  
  @Override
  protected IModel executeInternal(IModel model) throws Exception
  {
    // TODO Auto-generated method stub
    return null;
  }
}
