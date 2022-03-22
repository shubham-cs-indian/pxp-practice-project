package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTargetsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteKlassResponseModel> {
  
}
