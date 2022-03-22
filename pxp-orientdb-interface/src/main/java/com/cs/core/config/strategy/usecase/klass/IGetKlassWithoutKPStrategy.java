package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassWithoutKPStrategy
    extends IConfigStrategy<IIdParameterModel, IGetKlassEntityWithoutKPModel> {
  
}
