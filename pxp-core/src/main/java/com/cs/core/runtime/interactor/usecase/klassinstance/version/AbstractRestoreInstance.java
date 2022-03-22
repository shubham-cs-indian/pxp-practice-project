package com.cs.core.runtime.interactor.usecase.klassinstance.version;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public abstract class AbstractRestoreInstance<P extends IIdsListParameterModel, R extends IBulkResponseModel>
    extends AbstractRuntimeInteractor<P, R> {
}
