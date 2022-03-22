package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForGoldenRecordRuleResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailsForGoldenRecordBucketsStrategy
extends IRuntimeStrategy<IIdsListParameterModel, IGetConfigDetailsForGoldenRecordRuleResponseModel> {
  
}
