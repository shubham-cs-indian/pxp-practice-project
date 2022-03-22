package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteGovernanceTasksStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteReturnModel> {
  
}
