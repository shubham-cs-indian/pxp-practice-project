package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteMappingService
    extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
