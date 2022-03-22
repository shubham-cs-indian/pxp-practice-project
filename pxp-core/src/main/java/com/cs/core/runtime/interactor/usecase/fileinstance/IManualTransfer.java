package com.cs.core.runtime.interactor.usecase.fileinstance;

import com.cs.core.config.interactor.model.transfer.ITransferDataRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IManualTransfer extends IRuntimeInteractor<ITransferDataRequestModel, IModel> {
}
