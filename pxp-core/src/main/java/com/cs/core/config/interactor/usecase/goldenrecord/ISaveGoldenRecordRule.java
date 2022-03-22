package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;

public interface ISaveGoldenRecordRule
    extends ISaveConfigInteractor<ISaveGoldenRecordRuleModel, IGetGoldenRecordRuleModel> {
  
}
