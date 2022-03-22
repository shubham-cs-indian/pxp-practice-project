package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IGetMappedMappingRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetMappedMappingResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllMappedMappingStrategy
    extends IConfigStrategy<IGetMappedMappingRequestModel, IGetMappedMappingResponseModel> {
  
}
