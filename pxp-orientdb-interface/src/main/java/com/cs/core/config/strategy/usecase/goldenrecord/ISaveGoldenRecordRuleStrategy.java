package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveGoldenRecordRuleStrategy
    extends IConfigStrategy<ISaveGoldenRecordRuleModel, IGetGoldenRecordRuleModel> {
  
}
