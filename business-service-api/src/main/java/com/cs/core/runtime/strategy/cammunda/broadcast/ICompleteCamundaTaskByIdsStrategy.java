package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;

public interface ICompleteCamundaTaskByIdsStrategy
    extends ICamundaStrategy<IListModel<IGetTaskInstanceModel>, IModel> {
  
}
