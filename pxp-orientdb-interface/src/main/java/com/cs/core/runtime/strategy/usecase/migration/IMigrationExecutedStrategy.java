package com.cs.core.runtime.strategy.usecase.migration;

import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IMigrationExecutedStrategy
    extends IStrategy<IIdsListParameterModel, IIdsListParameterModel> {
  
}
