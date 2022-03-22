package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IUpdateIdentifierStatusForSearchableInstanceStrategy
    extends IRuntimeStrategy<IUpdateSearchableInstanceRequestModel, IModel> {
  
}
