package com.cs.di.config.strategy.authorization;

import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.base.IDiStrategy;

public interface IDeletePartnerAuthorizationStrategy extends IDiStrategy<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
