package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IGetKlassesForMappingModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassesForMappingStrategy
    extends IConfigStrategy<IIdParameterModel, IGetKlassesForMappingModel> {
  
}
