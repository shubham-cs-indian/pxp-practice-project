package com.cs.core.config.strategy.usecase.causeeffectrule;

import com.cs.core.config.interactor.model.causeeffectrule.ICauseEffectRulesModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetCauseEffectRuleStrategy
    extends IConfigStrategy<IIdParameterModel, ICauseEffectRulesModel> {
  
}
