package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.interactor.model.mapping.IRuntimeMappingModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllMappingByEndpointIdStrategy
    extends IConfigStrategy<IGetMappingForImportRequestModel, IRuntimeMappingModel> {
  
}
