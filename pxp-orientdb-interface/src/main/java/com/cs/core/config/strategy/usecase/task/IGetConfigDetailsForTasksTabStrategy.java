package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;

public interface IGetConfigDetailsForTasksTabStrategy
    extends IConfigStrategy<IMulticlassificationRequestModel, IGetConfigDetailsForTasksTabModel> {
  
}
