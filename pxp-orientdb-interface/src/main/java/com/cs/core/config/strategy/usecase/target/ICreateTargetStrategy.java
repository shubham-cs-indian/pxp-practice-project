package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateTargetStrategy
    extends IConfigStrategy<ITargetModel, IGetKlassEntityWithoutKPModel> {
  
}
