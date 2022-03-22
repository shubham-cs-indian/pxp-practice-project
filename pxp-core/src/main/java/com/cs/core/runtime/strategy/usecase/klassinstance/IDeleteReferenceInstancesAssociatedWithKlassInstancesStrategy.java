package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IDeleteReferenceInstancesAssociatedWithKlassInstancesStrategy
    extends IRuntimeStrategy<IDeleteKlassInstanceModel, IDeleteKlassInstanceModel> {
  
}
