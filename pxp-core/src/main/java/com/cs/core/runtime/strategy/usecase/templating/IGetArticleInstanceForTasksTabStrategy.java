package com.cs.core.runtime.strategy.usecase.templating;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetArticleInstanceForTasksTabStrategy
    extends IRuntimeStrategy<IGetInstanceRequestStrategyModel, IGetTaskInstanceResponseModel> {
  
}
