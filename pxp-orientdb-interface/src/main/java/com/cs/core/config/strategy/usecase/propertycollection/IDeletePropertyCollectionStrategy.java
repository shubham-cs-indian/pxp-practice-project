package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.IBulkDeletePropertyCollectionReturnModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeletePropertyCollectionStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeletePropertyCollectionReturnModel> {
  
}
