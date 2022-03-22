package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.configdetails.IInheritDefaultValueResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IPropagateChangesToInstancesStrategy
    extends IRuntimeStrategy<IContentsPropertyDiffModel, IInheritDefaultValueResponseModel> {
  
}
