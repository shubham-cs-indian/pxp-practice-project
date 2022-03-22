package com.cs.di.runtime.transfer;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.runtime.interactor.model.transfer.ITransferEntityRequestModel;

public interface IManualTransferService extends IRuntimeService<ITransferEntityRequestModel, IModel> {
  
}
