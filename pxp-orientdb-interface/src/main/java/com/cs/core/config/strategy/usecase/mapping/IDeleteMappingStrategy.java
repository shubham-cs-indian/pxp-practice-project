package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteMappingStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
