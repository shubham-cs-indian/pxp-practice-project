package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTargetStrategy extends IConfigStrategy<IIdParameterModel, ITargetModel> {
  
}
