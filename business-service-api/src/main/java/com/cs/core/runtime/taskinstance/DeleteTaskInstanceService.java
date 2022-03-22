package com.cs.core.runtime.taskinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.*;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForMultipleInstancesStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUsersByRoleStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.interactor.entity.notification.EntityInfo;
import com.cs.core.runtime.interactor.entity.notification.IEntityInfo;
import com.cs.core.runtime.interactor.exception.camunda.TaskIsInProgressException;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveDeletePermission;
import com.cs.core.runtime.interactor.exception.taskinstance.UserNothaveDeletePermissionForTask;
import com.cs.core.runtime.interactor.model.configuration.*;
import com.cs.core.runtime.interactor.model.notification.INotificationModel;
import com.cs.core.runtime.interactor.model.notification.NotificationModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.taskinstance.BulkDeleteTaskInstanceReturnModel;
import com.cs.core.runtime.interactor.model.taskinstance.IBulkDeleteTaskInstanceReturnModel;
import com.cs.core.runtime.interactor.model.taskinstance.IDeleteTaskInstancesRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import com.cs.workflow.camunda.IBPMNEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("unchecked")
@Service
public class DeleteTaskInstanceService extends AbstractRuntimeService<IDeleteTaskInstancesRequestModel, IBulkDeleteTaskInstanceReturnModel>
    implements IDeleteTaskInstanceService {
  
  @Autowired
  protected ISessionContext                                  context;
  
  @Autowired
  protected PermissionUtils                                  permissionUtils;
  
  @Autowired
  protected IGetGlobalPermissionForMultipleInstancesStrategy getGlobalPermissionForMultipleEntityStrategy;
  
  @Autowired
  IGetUsersByRoleStrategy                                    getUsersByRoleStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                              rdbmsComponentUtils;
  
  @Autowired
  protected IBPMNEngineService  camundaServiceManager;

  @Override
  public IBulkDeleteTaskInstanceReturnModel executeInternal(IDeleteTaskInstancesRequestModel dataModel) throws Exception
  {
    IExceptionModel failure = new ExceptionModel();
    String loginUserId = context.getUserId();
    IIdsListParameterModel idModel = new IdsListParameterModel();
    idModel.setIds(dataModel.getIds());
    
    Set<ITaskRecordDTO> taskRecords = this.getTaskInstance(dataModel);
    
    List<String> idsToDelete = new ArrayList<>();
    getIdsToDelete(loginUserId, dataModel.getIds(), idsToDelete, failure, taskRecords);
    dataModel.setIds(idsToDelete);
    IBulkDeleteTaskInstanceReturnModel returnModel = new BulkDeleteTaskInstanceReturnModel();
    if(dataModel.getIds().size() > 0) {
      this.deleteTaskInstance(dataModel);
      returnModel.setSuccess(dataModel.getIds());
      
      //TODO Handle notification later
      //createNotifications(dataModel.getIds(),taskRecords);
    }
    
    returnModel.setFailure(failure);
    return returnModel;
  }
  
  protected void deleteTaskInstance(IDeleteTaskInstancesRequestModel dataModel) throws Exception
  {
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    for (String taskId : dataModel.getIds()) {
      //TODO Handle multiple task fetch
      taskRecordDAO.deleteTaskRecord(Long.parseLong(taskId));
    }
  }
  
  protected Set<ITaskRecordDTO> getTaskInstance(IDeleteTaskInstancesRequestModel dataModel) throws Exception
  {
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    Set<ITaskRecordDTO> taskRecords = new HashSet<>();
    for (String taskId : dataModel.getIds()) {
      //TODO Handle multiple task fetch
      ITaskRecordDTO taskRecord = taskRecordDAO.getTaskByTaskIID(Long.parseLong(taskId));
      if(taskRecord != null) {
        taskRecords.add(taskRecord);
      }
    }
    return taskRecords;
  }
  
  protected void getIdsToDelete(String loginUserId , List<String> ids,List<String> idsToDelete,IExceptionModel failure, Set<ITaskRecordDTO> taskRecords) throws Exception
  {
    List<String> typeIds = new ArrayList<>();
    IGetGlobalPermissionForMultipleInstancesRequestModel getGlobalPermissionModel = new GetGlobalPermissionForMultipleInstancesRequestModel();
    List<IGetGlobalPermissionRequestModel> requestList = new ArrayList<>();
    getGlobalPermissionModel.setUserId(loginUserId);
    Map<String, String> instanceIdTypeMap = new HashMap<>();
    for (ITaskRecordDTO taskRecord : taskRecords) {
      String type = taskRecord.getTask().getTaskCode();
      if (!typeIds.contains(type)) {
        typeIds.add(type);
      }
      if (taskRecord.isWfCreated()) {
        if (camundaServiceManager.isTaskInstanceRunning(taskRecord.getWfTaskInstanceID())) {
          ids.remove(String.valueOf(taskRecord.getTaskIID()));
          ExceptionUtil.addFailureDetailsToFailureObject(failure, new TaskIsInProgressException(), String.valueOf(taskRecord.getTaskIID()),
              null);
        }
        else {
          prepareRequestModelForPermission(requestList, instanceIdTypeMap, taskRecord, type);
        }
      }
      else {
        prepareRequestModelForPermission(requestList, instanceIdTypeMap, taskRecord, type);
      }
    }
    getGlobalPermissionModel.setRequestList(requestList);
    IGetGlobalPermissionForMultipleEntityResponseModel response = getGlobalPermissionForMultipleEntityStrategy.execute(getGlobalPermissionModel);
    Map<String, IGlobalPermissionWithAllowedModuleEntitiesModel> globalPermissionMap = response.getGlobalPermission();
    for (String id : ids) {
      IGlobalPermissionWithAllowedModuleEntitiesModel permissionModel = globalPermissionMap.get(id);
      try {
        if (permissionModel != null) {
          if (permissionModel.getGlobalPermission().getCanDelete()) {
            idsToDelete.add(id);
          }
          else {
            throw new UserNothaveDeletePermissionForTask();
          }
        }
        else {
          // Already deleted.
          idsToDelete.add(id);
        }
      }
      catch (UserNotHaveDeletePermission e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, id, null);
      }
    }
  }
  
  private void prepareRequestModelForPermission(List<IGetGlobalPermissionRequestModel> requestList,
      Map<String, String> instanceIdTypeMap, ITaskRecordDTO taskRecord,
      String type) throws Exception
  {
    instanceIdTypeMap.put(String.valueOf(taskRecord.getTaskIID()), type);
    List<String> roleIdsWithCurrentUser = permissionUtils.getRoleIdsWithCurrentUser();
    IGetGlobalPermissionRequestModel requestModel = new GetGlobalPermissionRequestModel();
    requestModel.setId(String.valueOf(taskRecord.getTaskIID()));
    requestModel.setEntityId(type);
    requestModel.setRoleIdsContainingLoginUser(roleIdsWithCurrentUser);
    requestModel.setType(CommonConstants.TASK);
    requestModel.setCreatedBy(getTaskCreateBy(taskRecord));
    requestList.add(requestModel);
  }
  
  private String getTaskCreateBy(ITaskRecordDTO taskRecord)
  {
    Set<IUserDTO> users = taskRecord.getUsersMap()
        .get(RACIVS.ACCOUNTABLE);
    if (users != null && users.size() > 0) {
      Iterator<IUserDTO> iterator = users.iterator();
      IUserDTO user = iterator.next();
      return user.getUserName();
    }
    return "";
  }
  
  private void createNotifications(List<String> deletedIds, Set<ITaskRecordDTO> taskRecords) throws Exception
  {
    List<INotificationModel> notifications = new ArrayList<>();
    for (ITaskRecordDTO taskRecord : taskRecords) {
      if(deletedIds.contains(String.valueOf(taskRecord.getTaskIID()))){
        List<String> userIdsToCreateNotificationFor = new ArrayList<>();
        fillUsersToCreateNotificationFor(taskRecord.getUsersMap().get(RACIVS.INFORMED), taskRecord.getRolesMap().get(RACIVS.INFORMED), userIdsToCreateNotificationFor);
        String taskId = String.valueOf(taskRecord.getTaskIID());
        String label = taskRecord.getTaskName();
        String action = CommonConstants.TASK_DELETED;
        for (String userId : userIdsToCreateNotificationFor) {
          notifications.add(getNotificationObject(action, "task deleted", userId, taskId, null, label, null));
        }
      }
    }
    if(notifications.size() > 0){
      IListModel<INotificationModel> notificationListModel = new ListModel<>();
      notificationListModel.setList(notifications);
      //createNotificationsStrategy.execute(notificationListModel);
    }
  }
  
  private void fillUsersToCreateNotificationFor(Set<IUserDTO> users,Set<String> roles,List<String> userIdsToCreateNotificationFor) throws Exception
  {
   
    for (IUserDTO user : users) {     
      userIdsToCreateNotificationFor.add(user.getUserName());
    }
    for (String roleId : roles) {
      IIdParameterModel model = new IdParameterModel();
      model.setId(roleId);
      IGetGridUsersResponseModel getGridUsersResponseModel = getUsersByRoleStrategy.execute(model);
      for (IUser user : getGridUsersResponseModel.getUsersList()) {
        userIdsToCreateNotificationFor.add(user.getId());
      }
    }
  }
  
  private INotificationModel getNotificationObject(String action,String description,String actedFor,String taskInstanceId,String subTaskId,
      String taskLabel, String subtaskLabel) throws RDBMSException, Exception 
  {
    INotificationModel notification = new NotificationModel();
    notification.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TASK.getPrefix()));
    notification.setActedBy(context.getUserId());
    notification.setActedFor(actedFor);
    notification.setAction(action);
    notification.setDescription(description);
    notification.setStatus(CommonConstants.UNREAD);
    notification.setCreatedOn(System.currentTimeMillis());
    IEntityInfo entityInfo = new EntityInfo();
    entityInfo.setId(taskInstanceId);
    entityInfo.setSubEntityId(subTaskId);
    entityInfo.setType(CommonConstants.TASK);
    entityInfo.setLabel(taskLabel);
    entityInfo.setSubEntityLabel(subtaskLabel);
    notification.setEntityInfo(entityInfo);
    return notification;
  }
}
