package com.cs.core.config.businessapi.task;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.ICreateTaskStrategy;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateTaskService extends AbstractCreateConfigService<ITaskModel, IGetTaskModel>implements ICreateTaskService {
  
  @Autowired
  ICreateTaskStrategy  createTaskStrategy;
  
  @Override
  public IGetTaskModel executeInternal(ITaskModel model) throws Exception
  {
    String taskCode = model.getCode();
    if (StringUtils.isEmpty(taskCode)) {
      taskCode = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TASK.getPrefix());
    }
    model.setCode(taskCode);
    model.setId(taskCode);
    TaskValidations.validateTask(model, true);
    TaskType taskType = (model.getType()).equals(CommonConstants.TASK_TYPE_SHARED) ? TaskType.SHARED
        : TaskType.PERSONAL;
    RDBMSUtils.newConfigurationDAO().createTask(taskCode, taskType);
    return createTaskStrategy.execute(model);
  }
}
