package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllMappingIndexesStrategy
    extends IConfigStrategy<IMappingModel, IIdsListParameterModel> {
  
}
