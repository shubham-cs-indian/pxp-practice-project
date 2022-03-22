package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveTaskStrategy
    extends IConfigStrategy<IListModel<ITaskModel>, IBulkSaveTasksResponseModel> {
  
}
