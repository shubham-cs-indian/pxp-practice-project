package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.goldenrecord.IGetGoldenRecordRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGoldenRecordRule
    extends IGetConfigInteractor<IIdParameterModel, IGetGoldenRecordRuleModel> {
  
}
