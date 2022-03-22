package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;

public interface ISaveMappingService extends ISaveConfigService<ISaveMappingModel, ICreateOrSaveMappingModel> {
  
}
