package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;

public interface IGetMappingService extends IGetConfigService<IGetOutAndInboundMappingModel, IMappingModel> {
  
}
