package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IUpdateIdentifierAttributesTask extends
    IRuntimeInteractor<IPropertyInstanceUniquenessEvaluationForPropagationModel, IIdAndTypeModel> {
}
