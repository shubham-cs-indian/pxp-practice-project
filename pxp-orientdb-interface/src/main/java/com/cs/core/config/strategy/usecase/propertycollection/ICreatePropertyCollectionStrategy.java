package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreatePropertyCollectionStrategy
    extends IConfigStrategy<IPropertyCollectionModel, IGetPropertyCollectionModel> {
  
}
