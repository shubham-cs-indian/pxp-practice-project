package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetPropertyCollectionStrategy
    extends IConfigStrategy<IIdParameterModel, IGetPropertyCollectionModel> {
  
}
