package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;

public interface ICreateOutBoundMappingService
    extends IConfigService<IOutBoundMappingModel, ICreateOutBoundMappingResponseModel> {
  
}
