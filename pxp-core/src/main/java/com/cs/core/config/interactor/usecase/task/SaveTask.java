package com.cs.core.config.interactor.usecase.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.businessapi.task.ISaveTaskService;
import com.cs.core.config.businessapi.task.PropagateTaskChangesToTaskInstancesTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.interactor.model.task.IPropagateTaskChangesModel;
import com.cs.core.config.interactor.model.task.ISaveTaskResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.PropagateTaskChangesModel;
import com.cs.core.config.strategy.usecase.task.ISaveTaskStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;

@Service
public class SaveTask
    extends AbstractSaveConfigInteractor<IListModel<ITaskModel>, IBulkSaveTasksResponseModel>
    implements ISaveTask {

  @Autowired
  protected ISaveTaskService saveTaskAPI;

  @Override
  public IBulkSaveTasksResponseModel executeInternal(IListModel<ITaskModel> model) throws Exception
  {
    return saveTaskAPI.execute(model);
  }

}
