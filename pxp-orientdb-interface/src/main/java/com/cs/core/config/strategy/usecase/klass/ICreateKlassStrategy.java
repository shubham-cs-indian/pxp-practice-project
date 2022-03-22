package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateKlassStrategy
    extends IConfigStrategy<IKlassModel, IGetKlassEntityWithoutKPModel> {
  
}
