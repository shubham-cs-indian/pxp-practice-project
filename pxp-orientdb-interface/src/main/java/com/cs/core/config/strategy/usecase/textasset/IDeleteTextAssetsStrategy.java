package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteTextAssetsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteKlassResponseModel> {
  
}
