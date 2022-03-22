package com.cs.core.config.interactor.usecase.causeeffectrule;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetCauseEffectRule
    extends IGetConfigInteractor<IIdParameterModel, ICauseEffectRulesModel> {
  
}
