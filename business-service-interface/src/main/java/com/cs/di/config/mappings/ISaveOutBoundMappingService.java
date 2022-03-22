package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.mapping.ICreateOutBoundMappingResponseModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;

public interface ISaveOutBoundMappingService
    extends ISaveConfigService<ISaveOutBoundMappingModel, ICreateOutBoundMappingResponseModel> {
  
}
