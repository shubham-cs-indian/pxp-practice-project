package com.cs.di.runtime.interactor.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.di.runtime.interactor.model.transfer.ITransferEntityRequestModel;
import com.cs.di.runtime.transfer.IManualTransferService;

@Service("manualTransferInteractor")
public class ManualTransferInteractor
    extends AbstractRuntimeInteractor<ITransferEntityRequestModel, IModel>
    implements IManualTransferInteractor {
 
  @Autowired
  protected IManualTransferService manualTransferService;

  @Override
  protected IModel executeInternal(ITransferEntityRequestModel model) throws Exception
  {
    return manualTransferService.execute(model);
  }
}
