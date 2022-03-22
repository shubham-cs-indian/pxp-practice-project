package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IGetMappedEndpointRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllMappedEndpointsStrategy
    extends IConfigStrategy<IGetMappedEndpointRequestModel, IGetMappingForImportResponseModel> {
  
}
