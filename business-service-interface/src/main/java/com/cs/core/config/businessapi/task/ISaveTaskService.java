package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

public interface ISaveTaskService  extends ISaveConfigService<IListModel<ITaskModel>, IBulkSaveTasksResponseModel>{

}
