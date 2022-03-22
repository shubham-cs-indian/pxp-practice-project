package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.configdetails.IDeleteReturnModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteRoleStrategy
    extends IConfigStrategy<IIdsListParameterModel, IDeleteReturnModel> {
  
}
