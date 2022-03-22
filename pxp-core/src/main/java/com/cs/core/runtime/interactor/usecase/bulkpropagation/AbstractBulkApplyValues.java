package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IBulkApplyValueRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public abstract class AbstractBulkApplyValues<P extends IBulkApplyValueRequestModel, R extends IIdsListParameterModel>
    extends AbstractRuntimeInteractor<P, R> {
}
