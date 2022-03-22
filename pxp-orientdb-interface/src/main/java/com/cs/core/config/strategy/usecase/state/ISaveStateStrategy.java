package com.cs.core.config.strategy.usecase.state;

import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveStateStrategy extends IConfigStrategy<IStateModel, IGetStateResponseModel> {
  
}
