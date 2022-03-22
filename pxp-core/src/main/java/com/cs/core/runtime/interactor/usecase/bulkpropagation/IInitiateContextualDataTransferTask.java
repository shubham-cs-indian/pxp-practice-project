package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.context.IContextualDataTransferInputModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IInitiateContextualDataTransferTask
    extends IRuntimeInteractor<IContextualDataTransferInputModel, IModel> {
}
