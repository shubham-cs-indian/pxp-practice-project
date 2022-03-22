package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IEvaluateGovernanceRulesTask
    extends IRuntimeInteractor<ITypeInfoWithContentIdentifiersModel, IIdParameterModel> {
}
