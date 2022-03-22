package com.cs.core.runtime.store.strategy.usecase.dynamic.collection;

import com.cs.core.runtime.interactor.model.collections.IDynamicCollectionModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ICreateDynamicCollectionStrategy extends
    IRuntimeStrategy<IDynamicCollectionModel, IDynamicCollectionModel> {
  
}