package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;

public interface ICreateGoldenRecordRule
    extends ICreateConfigInteractor<IGoldenRecordRuleModel, IGetGoldenRecordRuleModel> {
  
}
