package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;

public interface IGetOutBoundMappingService
    extends IGetConfigService<IGetOutAndInboundMappingModel, IOutBoundMappingModel> {
  
}
