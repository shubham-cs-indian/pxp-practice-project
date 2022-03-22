package com.cs.di.config.strategy.authorization;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeletePartnerAuthorizationStrategy extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
