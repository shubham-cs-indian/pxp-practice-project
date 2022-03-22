package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetMappingForImportStrategy
    extends IConfigStrategy<IGetMappingForImportRequestModel, IGetMappingForImportResponseModel> {
  
}
