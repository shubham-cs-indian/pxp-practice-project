package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteUserService
    extends IConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
