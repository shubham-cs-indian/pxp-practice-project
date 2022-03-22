package com.cs.core.config.strategy.usecase.asset;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteAssetsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteKlassResponseModel> {
  
}
