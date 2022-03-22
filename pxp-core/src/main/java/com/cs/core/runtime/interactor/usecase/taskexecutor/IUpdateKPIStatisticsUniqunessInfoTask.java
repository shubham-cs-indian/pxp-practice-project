package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IUpdateKPIStatisticsUniqunessInfoTask extends
    IRuntimeInteractor<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel, IIdAndTypeModel> {
}
