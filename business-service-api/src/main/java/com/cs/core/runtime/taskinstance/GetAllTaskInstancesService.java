package com.cs.core.runtime.taskinstance;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.IGetAllTasksForDashboardStrategy;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceForDashboardRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceForDashboardResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetConfigDetailsForTasksDashboardModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceForDashboardModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceForDashboardRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TaskUtil;

@Service
public class GetAllTaskInstancesService extends AbstractRuntimeService<IIdParameterModel, IGetTaskInstanceForDashboardModel>
    implements IGetAllTaskInstancesService {
  
  @Autowired
  ISessionContext                  context;
  
  @Autowired
  IGetAllTasksForDashboardStrategy getAllTasksStrategy;
  
  @Autowired
  protected RDBMSComponentUtils    rdbmsComponentUtils;
  
  @Override
  public IGetTaskInstanceForDashboardModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    String userId = context.getUserId();
    dataModel.setCurrentUserId(userId);
    IGetConfigDetailsForTasksDashboardModel configDetails = getAllTasksStrategy.execute(dataModel);
    IGetTaskInstanceForDashboardModel tasksInstanceResponseModel = new GetTaskInstanceForDashboardResponseModel();
    IGetTaskInstanceForDashboardRequestModel requestModel = new GetTaskInstanceForDashboardRequestModel();
    requestModel.setConfigDetails(configDetails);
    requestModel.setUserDetails(dataModel);
    this.getTaskInfo(dataModel, configDetails, tasksInstanceResponseModel);
    tasksInstanceResponseModel.setConfigDetails(configDetails);
    return tasksInstanceResponseModel;
  }
  
  protected void getTaskInfo(IIdParameterModel dataModel, IGetConfigDetailsForTasksDashboardModel configDetails,
      IGetTaskInstanceForDashboardModel tasksInstanceResponseModel) throws Exception
  {
    RACIVS vRACIVS = TaskUtil.getRACIVS(dataModel.getId());
    String roleCode = configDetails.getRoleIdsOfCurrentUser().iterator().hasNext()
        ? configDetails.getRoleIdsOfCurrentUser().iterator().next()
        : "";
    Set<ITaskRecordDTO> taskRecords = this.rdbmsComponentUtils.openTaskDAO().getAllTask(roleCode, vRACIVS, dataModel.getType());
    
    // tasksInstanceResponseModel.setTasksCount(taskRecords.size());
    List<ITaskInstanceInformationModel> taskInstanceList = tasksInstanceResponseModel.getTaskInstanceList();
    taskRecords.forEach(taskRecord -> {
      ITaskModel taskConfig = configDetails.getReferencedTasks().get(taskRecord.getTask().getCode());
      ITaskInstanceInformationModel taskInstanceInformationModel;
      try {
        if (taskConfig != null) {
          taskInstanceInformationModel = TaskInstanceBuilder.getTaskInstanceInformationModel(taskRecord, taskConfig);
          taskInstanceList.add(taskInstanceInformationModel);
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    tasksInstanceResponseModel.setTasksCount(taskInstanceList == null ? 0 : taskInstanceList.size());
  }
  
}
