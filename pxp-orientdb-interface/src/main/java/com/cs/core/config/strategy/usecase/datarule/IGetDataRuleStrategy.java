package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDataRuleStrategy extends IConfigStrategy<IIdParameterModel, IDataRuleModel> {
  
}
