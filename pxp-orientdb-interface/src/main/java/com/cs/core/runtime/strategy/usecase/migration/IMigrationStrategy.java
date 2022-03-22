package com.cs.core.runtime.strategy.usecase.migration;

import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;

public interface IMigrationStrategy extends IStrategy<IMigrationModel, IIdParameterModel> {
  
}
