package com.cs.core.config.strategy.usecase.state;

import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetStateStrategy extends IConfigStrategy<IIdParameterModel, IStateModel> {
  
}
