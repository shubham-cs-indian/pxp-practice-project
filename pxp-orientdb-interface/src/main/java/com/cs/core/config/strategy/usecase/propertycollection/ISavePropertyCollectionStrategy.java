package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISavePropertyCollectionStrategy
    extends IConfigStrategy<ISavePropertyCollectionModel, ISavePropertyCollectionResponseModel> {
  
}
