package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;

public interface IGetAllMappingsService
    extends IGetConfigService<IConfigGetAllRequestModel, IGetAllMappingsResponseModel> {
  
}
