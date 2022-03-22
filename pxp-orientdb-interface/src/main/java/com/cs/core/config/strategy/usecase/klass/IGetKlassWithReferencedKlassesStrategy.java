package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassWithReferencedKlassesStrategy
    extends IConfigStrategy<IIdParameterModel, IGetKlassModel> {
  
}
