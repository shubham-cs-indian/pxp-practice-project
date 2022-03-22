package com.cs.core.config.target;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTargetService
    extends IDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
