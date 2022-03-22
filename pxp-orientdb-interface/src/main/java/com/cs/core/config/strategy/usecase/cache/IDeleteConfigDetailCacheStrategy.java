package com.cs.core.config.strategy.usecase.cache;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDeleteConfigDetailCacheStrategy
    extends IConfigStrategy<IIdParameterModel, IModel> {
  
}
