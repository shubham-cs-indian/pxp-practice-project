package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateOutBoundMappingStrategy
    extends IConfigStrategy<IOutBoundMappingModel, ICreateOutBoundMappingResponseModel> {
  
}
