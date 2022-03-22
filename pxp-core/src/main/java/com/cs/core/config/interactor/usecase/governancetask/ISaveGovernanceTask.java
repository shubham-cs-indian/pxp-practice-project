package com.cs.core.config.interactor.usecase.governancetask;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

public interface ISaveGovernanceTask
    extends ISaveConfigInteractor<IListModel<ITaskModel>, IBulkSaveTasksResponseModel> {
  
}
