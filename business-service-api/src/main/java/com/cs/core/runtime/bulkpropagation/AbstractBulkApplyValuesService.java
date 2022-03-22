package com.cs.core.runtime.bulkpropagation;

import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IBulkApplyValueRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public abstract class AbstractBulkApplyValuesService<P extends IBulkApplyValueRequestModel, R extends IIdsListParameterModel>
    extends AbstractRuntimeService<P, R> {}
