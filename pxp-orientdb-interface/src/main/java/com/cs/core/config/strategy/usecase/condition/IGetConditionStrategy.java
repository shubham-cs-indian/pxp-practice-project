package com.cs.core.config.strategy.usecase.condition;

import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetConditionStrategy extends IConfigStrategy<IIdParameterModel, IConditionModel> {
  
}
