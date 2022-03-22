package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IInstanceGetByIdModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetInstanceByIdStrategy
    extends IRuntimeStrategy<IIdParameterModel, IInstanceGetByIdModel> {
  
}
