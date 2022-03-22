package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.config.interactor.model.goldenrecord.ITypesInfoWithRuleIdModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IEvaluateGoldenRecordRuleBucketsForConfigChangeTask
    extends IRuntimeInteractor<ITypesInfoWithRuleIdModel, IIdParameterModel> {
}
