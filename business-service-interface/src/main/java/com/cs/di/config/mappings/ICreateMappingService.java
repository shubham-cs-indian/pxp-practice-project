package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;

public interface ICreateMappingService extends ICreateConfigService<IMappingModel, ICreateOrSaveMappingModel> {
  
}
