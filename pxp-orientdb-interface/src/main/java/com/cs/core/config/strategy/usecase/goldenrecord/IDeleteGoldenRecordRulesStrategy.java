package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleStrategyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteGoldenRecordRulesStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteGoldenRecordRuleStrategyModel> {
  
}
