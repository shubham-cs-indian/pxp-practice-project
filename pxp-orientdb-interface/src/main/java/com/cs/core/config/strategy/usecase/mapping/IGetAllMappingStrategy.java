package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllMappingStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllMappingsResponseModel> {
  
}
