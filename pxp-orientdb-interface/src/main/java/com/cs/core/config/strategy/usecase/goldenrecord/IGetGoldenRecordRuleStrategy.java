package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGoldenRecordRuleStrategy
    extends IConfigStrategy<IIdParameterModel, IGetGoldenRecordRuleModel> {
  
}
