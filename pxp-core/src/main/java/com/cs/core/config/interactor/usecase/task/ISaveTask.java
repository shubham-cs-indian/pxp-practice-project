package com.cs.core.config.interactor.usecase.task;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

public interface ISaveTask
    extends ISaveConfigInteractor<IListModel<ITaskModel>, IBulkSaveTasksResponseModel> {
  
}
