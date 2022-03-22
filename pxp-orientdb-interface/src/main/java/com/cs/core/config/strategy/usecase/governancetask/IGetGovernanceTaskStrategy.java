package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGovernanceTaskStrategy extends IConfigStrategy<IIdParameterModel, ITaskModel> {
  
}
