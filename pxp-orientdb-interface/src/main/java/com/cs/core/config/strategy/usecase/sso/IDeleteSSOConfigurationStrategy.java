package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteSSOConfigurationStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
