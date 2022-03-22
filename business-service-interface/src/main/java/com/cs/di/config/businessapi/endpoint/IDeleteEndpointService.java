package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteEndpointService extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
