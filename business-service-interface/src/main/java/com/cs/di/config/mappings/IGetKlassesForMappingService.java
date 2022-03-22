package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetKlassesForMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetKlassesForMappingService
    extends IGetConfigService<IIdParameterModel, IGetKlassesForMappingModel> {
  
}
