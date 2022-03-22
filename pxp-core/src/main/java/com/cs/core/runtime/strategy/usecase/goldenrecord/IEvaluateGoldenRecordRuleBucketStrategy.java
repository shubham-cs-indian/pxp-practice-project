package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IEvaluateGoldenRecordRuleBucketRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IEvaluateGoldenRecordRuleBucketStrategy
    extends IRuntimeStrategy<IEvaluateGoldenRecordRuleBucketRequestModel, IIdParameterModel> {
  
}
