package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IGetConfigDetailsForEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetConfigDetailsForExportRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDetailsForEndpointStrategy extends
    IConfigStrategy<IGetConfigDetailsForExportRequestModel, IGetConfigDetailsForEndpointModel> {
  
}
