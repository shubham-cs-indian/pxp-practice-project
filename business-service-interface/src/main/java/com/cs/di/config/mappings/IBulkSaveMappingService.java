package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingResponseModel;

public interface IBulkSaveMappingService
    extends ISaveConfigService<IBulkSaveMappingModel, IBulkSaveMappingResponseModel> {
  
}
