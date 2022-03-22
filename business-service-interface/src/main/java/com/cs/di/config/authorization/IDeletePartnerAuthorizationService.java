package com.cs.di.config.authorization;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeletePartnerAuthorizationService extends IConfigService<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
