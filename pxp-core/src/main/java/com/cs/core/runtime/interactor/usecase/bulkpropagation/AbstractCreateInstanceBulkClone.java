package com.cs.core.runtime.interactor.usecase.bulkpropagation;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractCreateInstanceBulkClone<P extends ICreateKlassInstanceBulkCloneModel, R extends IBulkCreateKlassInstanceCloneResponseModel>
    extends AbstractRuntimeInteractor<P, R> {
}
