package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IEvaluateIdentifierAttributeTask
    extends IRuntimeInteractor<IEvaluateIdentifierAttributesInstanceModel, IVoidModel> {
}
