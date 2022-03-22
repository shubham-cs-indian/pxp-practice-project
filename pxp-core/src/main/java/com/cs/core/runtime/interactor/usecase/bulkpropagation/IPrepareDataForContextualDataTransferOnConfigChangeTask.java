package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContetxtualDataTransferOnConfigChangeInputModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IPrepareDataForContextualDataTransferOnConfigChangeTask extends
    IRuntimeInteractor<IContetxtualDataTransferOnConfigChangeInputModel, IContentsPropertyDiffModel> {
}
