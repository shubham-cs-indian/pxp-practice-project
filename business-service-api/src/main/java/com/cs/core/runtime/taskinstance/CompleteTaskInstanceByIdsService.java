package com.cs.core.runtime.taskinstance;

import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByProcessDefinitionIdStrategy;
import com.cs.core.config.strategy.usecase.task.IGetTaskStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTOBuilder;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.exception.taskinstance.TaskCannotBeDoneException;
import com.cs.core.runtime.interactor.exception.taskinstance.TaskCannotBeSignedOffException;
import com.cs.core.runtime.interactor.exception.taskinstance.UserNotHaveReadPermissionForTask;
import com.cs.core.runtime.interactor.model.camunda.CamundaProcessModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.*;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedTagInstanceModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.taskinstance.*;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TaskUtil.TaskStatus;
import com.cs.core.runtime.strategy.cammunda.broadcast.ICompleteCamundaTaskByIdsStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import com.cs.workflow.camunda.CamundaProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompleteTaskInstanceByIdsService extends AbstractRuntimeService<IIdsListParameterModel, IBulkSaveResponseModelForTaskTab>
    implements ICompleteTaskInstanceByIdsService {
  
  @Autowired
  protected ICompleteCamundaTaskByIdsStrategy                  completeCamundaTaskByIdsStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                rdbmsComponentUtils;
  
  @Autowired
  protected IGetTaskStrategy                                   getTaskStrategy;
  
  @Autowired
  protected PermissionUtils                                    permissionUtils;
  
  @Autowired
  protected CamundaProcessUtils                                camundaProcessUtils;
  
  @Autowired
  protected IGetProcessDefinitionByProcessDefinitionIdStrategy getProcessDefinitionByProcessDefinitionIdStrategy;
  
  @Override
  protected IBulkSaveResponseModelForTaskTab executeInternal(IIdsListParameterModel model) throws Exception
  {
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    
    IBulkTaskInstanceResponseModel bulktaskinstance = new BulkTaskInstanceResponseModel();
    Map<String, IGetTaskInstanceModel> getTaskInstanceModel = new HashMap<>();
    
    Set<ITaskRecordDTO> taskRecords = new HashSet<>();
    for (String taskId : model.getIds()) {
      // TODO Handle multiple task fetch
      ITaskInstanceResponseModel taskinstanceresonse = new TaskInstanceResponseModel();
      ITaskRecordDTO taskRecord = taskRecordDAO.getTaskByTaskIID(Long.parseLong(taskId));
      ITaskModel taskConfig = this.getTaskInstance(taskRecord.getTask().getCode());
      IGetTaskInstanceModel getModel = this.getTaskInstance(taskConfig, taskRecord);    
      getTaskInstanceModel.put(taskId, getModel);
      taskinstanceresonse.setTasks(getTaskInstanceModel);
      bulktaskinstance.setSuccess(taskinstanceresonse);
    }
    
    IListModel<IGetTaskInstanceModel> getModels = new ListModel<>();
    getModels.setList(bulktaskinstance.getSuccess().getTasks().values());
    completeCamundaTaskByIdsStrategy.execute(getModels);
    
    ListModel<ISaveTaskInstanceModel> requestModel = (ListModel<ISaveTaskInstanceModel>) changeTaskStatus(getModels);
    
    IBulkSaveResponseModelForTaskTab returnModel = saveTaskInstances(requestModel, bulktaskinstance.getSuccess().getTasks());
    return returnModel;
  }
  
  private IListModel<ISaveTaskInstanceModel> changeTaskStatus(IListModel<IGetTaskInstanceModel> dataModels) throws Exception
  {
    List<ISaveTaskInstanceModel> returnList = new ArrayList<>();
    IListModel<ISaveTaskInstanceModel> returnModel = new ListModel<>();
    for (IGetTaskInstanceModel getModel : dataModels.getList()) {
      ISaveTaskInstanceModel dataModel = new SaveTaskInstanceModel();
      
      prepareSaveTaskInstanceModel(getModel, dataModel);
      
      ITagInstance statustag = getModel.getStatus();
      List<String> idsToDelete = new ArrayList<>();
      
      List<ITagInstanceValue> tagValues = statustag.getTagValues();
      ITagInstanceValue oldTagInstanceValue = tagValues.get(0);
      idsToDelete.add(oldTagInstanceValue.getId());
      
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setId(UUID.randomUUID().toString());
      tagInstanceValue.setTagId(SystemLevelIds.DONE);
      tagInstanceValue.setRelevance(100);
      List<ITagInstanceValue> tagInstanceValues = new ArrayList<>();
      tagInstanceValues.add(tagInstanceValue);
      
      List<IModifiedContentTagInstanceModel> modifiedTags = dataModel.getModifiedTags();
      IModifiedTagInstanceModel modifiedTagInstance = new ModifiedTagInstanceModel();
      Boolean isTaskDone = false;
      for (IModifiedContentTagInstanceModel modifiedTag : modifiedTags) {
        if (modifiedTag.getTagId().equals(SystemLevelIds.TASK_STATUS_TAG)) {
          ITagInstanceValue tagValue = modifiedTag.getAddedTagValues().get(0);
          idsToDelete.add(tagValue.getId());
          modifiedTag.setAddedTagValues(tagInstanceValues);
          modifiedTag.setDeletedTagValues(idsToDelete);
          isTaskDone = true;
        }
      }
      
      if (!isTaskDone) {
        modifiedTagInstance.setTagId(SystemLevelIds.TASK_STATUS_TAG);
        modifiedTagInstance.setId(UUID.randomUUID().toString());
        modifiedTagInstance.setBaseType("com.cs.runtime.interactor.entity.TagInstance");
        modifiedTagInstance.setAddedTagValues(tagInstanceValues);
        modifiedTagInstance.setDeletedTagValues(idsToDelete);
        modifiedTags.add(modifiedTagInstance);
      }
      returnList.add(dataModel);
    }
    returnModel.setList(returnList);
    return returnModel;
  }
  
  private void prepareSaveTaskInstanceModel(IGetTaskInstanceModel getModel, ISaveTaskInstanceModel dataModel)
  {
    dataModel.setTypes(getModel.getTypes());
    dataModel.setId(getModel.getId());
    dataModel.setSystemId(getModel.getSystemId());
    dataModel.setStartDate(getModel.getStartDate());
    dataModel.setPhysicalCatalogId(getModel.getPhysicalCatalogId());
    dataModel.setOverDueDate(getModel.getOverDueDate());
    dataModel.setOrganizationId(getModel.getOrganizationId());
    dataModel.setName(getModel.getName());
    dataModel.setIsCamundaCreated(getModel.getIsCamundaCreated());
    dataModel.setIsFavourite(getModel.getIsFavourite());
    dataModel.setIsNotifiedForDueDate(getModel.getIsNotifiedForDueDate());
    dataModel.setIsPublic(getModel.getIsPublic());
    dataModel.setDueDate(getModel.getDueDate());
    dataModel.setEventSchedule(getModel.getEventSchedule());
    dataModel.setLongDescription(getModel.getLongDescription());
    dataModel.setCamundaProcessDefinationId(getModel.getCamundaProcessDefinationId());
    dataModel.setBaseType(getModel.getBaseType());
    dataModel.setCamundaProcessInstanceId(getModel.getCamundaProcessInstanceId());
    dataModel.setCamundaTaskInstanceId(getModel.getCamundaTaskInstanceId());
    dataModel.setFormFields(getModel.getFormFields());
  }
  
  protected ITaskModel getTaskInstance(String takCode) throws Exception
  {
    IIdParameterModel iIdParameterModel = new IdParameterModel(takCode);
    return this.getTaskStrategy.execute(iIdParameterModel);
  }
  
  protected IGetTaskInstanceModel getTaskInstance(ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    return TaskInstanceBuilder.getTaskInstance(taskConfig, taskRecordDTO, this.rdbmsComponentUtils);
  }
  
  public IBulkSaveResponseModelForTaskTab saveTaskInstances(IListModel<ISaveTaskInstanceModel> dataModels,
      Map<String, IGetTaskInstanceModel> map) throws Exception
  {
    IListModel<ISaveTaskInstanceModel> saveModels = new ListModel<>();
    List<ISaveTaskInstanceModel> saveModelsList = new ArrayList<>();
    Set<String> requestIds = new HashSet<>();
    IListModel<IGetTaskInstanceModel> getModels = new ListModel<>();
    List<IGetTaskInstanceModel> getModelList = new ArrayList<>();
    IIdsListParameterModel ids = new IdsListParameterModel();
    IBulkSaveResponseModelForTaskTab success = new BulkSaveResponseModelForTaskTab();
    IBulkTaskInstanceResponseModel bulkTaskResponse = new BulkTaskInstanceResponseModel();
    ITaskInstanceResponseModel taskInstanceModel = new TaskInstanceResponseModel();
    IExceptionModel failure = new ExceptionModel();
    for (ISaveTaskInstanceModel dataModel : dataModels.getList()) {
      prepareRequestModel(dataModel);
      saveModelsList.add(dataModel);
    }
    saveModels.setList(saveModelsList);
    for (ISaveTaskInstanceModel dataModel : saveModels.getList()) {
      try {
        Map<String, Object> tasksMap = new HashMap<>();
        ITaskRecordDAO taskDAO = this.rdbmsComponentUtils.openTaskDAO();
        
        ITaskRecordDTO taskRecordDTO = taskDAO.getTaskByTaskIID(Long.valueOf(dataModel.getId()));
        
        ITaskModel taskConfig = this.getTaskInstance(taskRecordDTO.getTask().getCode());
        
        this.manageModifiedTask(dataModel, taskConfig, taskRecordDTO);
        
        taskDAO.updateTaskRecord(taskRecordDTO);
        
        ITaskRecordDTO updatedtaskRecord = taskDAO.getTaskByTaskIID(Long.valueOf(dataModel.getId()));
        IGetTaskInstanceModel model = TaskInstanceBuilder.getTaskInstance(taskConfig, updatedtaskRecord, this.rdbmsComponentUtils);
        map.put(dataModel.getId(), model);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, dataModel.getId(), (String) dataModel.getName());
      }
      taskInstanceModel.setTasks(map);
      bulkTaskResponse.setSuccess(taskInstanceModel);
      bulkTaskResponse.setFailure(failure);
      
    }
    Boolean isSaveFailed = true;
    for (ISaveTaskInstanceModel dataModel : saveModels.getList()) {
      
      String taskInstanceId = dataModel.getId();
      //IGetTaskInstanceModel getTaskInstanceModel = map.get(taskInstanceId);
      IGetTaskInstanceModel model = bulkTaskResponse.getSuccess().getTasks().get(dataModel.getId());
      
      if (model == null) {
        continue;
      }
      isSaveFailed = false;
      // dataModel.setStatusTagId(statusTagId);
      // dataModel.setPriorityTagId(priorityTagId);
      // taskInstanceUtils.createNotifications(dataModel, getModel, model);
      getGlobalPermission(dataModel, model);
      requestIds.add(model.getCamundaProcessDefinationId());
      getModelList.add(model);
    }
    
    if (isSaveFailed) {
      IExceptionModel failure_new = new ExceptionModel();
      ExceptionUtil.addFailureDetailsToFailureObject(failure_new, new TaskCannotBeDoneException(), null, null);
      success.setFailure(failure_new);
      return success;
    }
    ids.setIds(requestIds.stream().collect(Collectors.toList()));
    getModels.setList(getModelList);
    IGetCamundaProcessDefinitionResponseModel processDefinitions = getProcessDefinitionByProcessDefinitionIdStrategy.execute(ids);
    success.setSuccess(setReferencedProcessDefinition(processDefinitions, getModels));
    success.setFailure(bulkTaskResponse.getFailure());
    return success;
  }
  
  protected void manageModifiedTask(ISaveTaskInstanceModel dataModel, ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    taskRecordDTO.setTaskName(dataModel.getName());
    taskRecordDTO.setDescription(dataModel.getLongDescription());
    this.manageModifiedTag(dataModel, taskConfig, taskRecordDTO);
    this.manageModifiedDate(dataModel, taskRecordDTO);
    this.manageModifiedUserRole(dataModel, taskRecordDTO);
    this.manageSubTask(dataModel, taskConfig, taskRecordDTO);
    this.manageModifiedCommands(dataModel, taskRecordDTO);
    this.manageModifiedAttachments(dataModel, taskRecordDTO);
  }
  
  protected void manageModifiedAttachments(ISaveTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    this.manageAddedAttachments(dataModel.getAddedAttachments(), taskRecordDTO);
    this.manageDeletedAttachments(dataModel.getDeletedAttachments(), taskRecordDTO);
  }
  
  protected void manageAddedAttachments(List<String> addedAttachments, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    for (String attachmentId : addedAttachments) {
      taskRecordDTO.getAttachments().add(Long.parseLong(attachmentId));
    }
  }
  
  protected void manageDeletedAttachments(List<String> deletedAttachments, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    for (String attachmentId : deletedAttachments) {
      taskRecordDTO.getAttachments().remove(Long.parseLong(attachmentId));
    }
  }
  
  protected void manageModifiedTag(ISaveTaskInstanceModel dataModel, ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    List<IModifiedContentTagInstanceModel> modifiedTags = dataModel.getModifiedTags();
    if (modifiedTags != null && modifiedTags.size() > 0) {
      for (IModifiedContentTagInstanceModel modifiedContentTagInstance : modifiedTags) {
        List<ITagInstanceValue> addedTagValues = modifiedContentTagInstance.getAddedTagValues();
        ITagInstanceValue tagInstanceValue = addedTagValues.get(0);
        // Status tag
        if (modifiedContentTagInstance.getTagId().equals(taskConfig.getStatusTag())) {
          if (this.checkSubTaskStatusAllow(tagInstanceValue, taskRecordDTO)) {
            IPropertyDTO propertyByCode = RDBMSUtils.getPropertyByCode(taskConfig.getStatusTag());
            ITagValueDTO tagValueDTO = RDBMSUtils.getOrCreateTagValueDTO(tagInstanceValue.getTagId(), propertyByCode.getPropertyIID());
            taskRecordDTO.setStatusTag(tagValueDTO);
          }
        }
        // Priority tag
        else if (modifiedContentTagInstance.getTagId().equals(taskConfig.getPriorityTag())) {
          IPropertyDTO propertyByCode = RDBMSUtils.getPropertyByCode(taskConfig.getPriorityTag());
          ITagValueDTO tagValueDTO = RDBMSUtils.getOrCreateTagValueDTO(tagInstanceValue.getTagId(), propertyByCode.getPropertyIID());
          taskRecordDTO.setPriorityTag(tagValueDTO);
        }
      }
    }
  }
  
  private boolean checkSubTaskStatusAllow(ITagInstanceValue tagInstanceValue, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    boolean isAllow = true;
    for (ITaskRecordDTO subTask : taskRecordDTO.getChildren()) {
      int mainTaskOrdinal = TaskStatus.getOrdinal(tagInstanceValue.getTagId());
      ITagValueDTO statusTag = subTask.getStatusTag();
      int subTaskTaskOrdinal = TaskStatus.getOrdinal(statusTag.getCode());
      
      if (subTaskTaskOrdinal < mainTaskOrdinal) {
        switch (TaskStatus.valueOf(subTaskTaskOrdinal)) {
          case TASKREADY:
            throw new TaskCannotBeDoneException();
          case TASKINPROGRESS:
            throw new TaskCannotBeDoneException();
          case TASKDONE:
            throw new TaskCannotBeDoneException();
          case TASKVERIFIED:
            throw new TaskCannotBeDoneException();
          case TASKSIGNEDOFF:
            throw new TaskCannotBeSignedOffException();
          default:
            throw new TaskCannotBeDoneException();
        }
      }
      
    }
    return isAllow;
  }
  
  protected void manageModifiedCommands(ISaveTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    List<IComment> addedComments = dataModel.getAddedComments();
    if (addedComments != null && addedComments.size() > 0) {
      
      ITaskCommentDTOBuilder commentBuilder = this.rdbmsComponentUtils.openTaskDAO().newTaskCommentDTOBuilder();
      List<ITaskCommentDTO> comments = taskRecordDTO.getComments();
      
      addedComments.stream().forEach(command -> {
        
        // Attachments
        Set<Long> attachments = command.getAttachments().stream().map(attachmentId -> Long.parseLong(attachmentId))
            .collect(Collectors.toSet());
        
        ITaskCommentDTO commentDTO = commentBuilder.text(command.getText()).time(Long.valueOf(command.getTimestamp()))
            .userName(command.getPostedBy()).attachments(attachments).build();
        comments.add(commentDTO);
      });
    }
  }
  
  protected void manageModifiedDate(ISaveTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO)
  {
    // Start date
    Long startDate = dataModel.getStartDate() == null ? 0 : dataModel.getStartDate();
    taskRecordDTO.setStartDate(startDate);
    
    // Due date
    Long dueDate = dataModel.getDueDate() == null ? 0 : dataModel.getDueDate();
    taskRecordDTO.setDueDate(dueDate);
    
    // over due date
    Long overDueDate = dataModel.getOverDueDate() == null ? 0 : dataModel.getOverDueDate();
    taskRecordDTO.setOverdueDate(overDueDate);
  }
  
  protected void manageModifiedUserRole(ISaveTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    // Responsible
    this.handleModifiedUserRole(dataModel.getResponsible(), taskRecordDTO, RACIVS.RESPONSIBLE);
    
    // accountable
    this.handleModifiedUserRole(dataModel.getAccountable(), taskRecordDTO, RACIVS.ACCOUNTABLE);
    
    // consulted
    this.handleModifiedUserRole(dataModel.getConsulted(), taskRecordDTO, RACIVS.CONSULTED);
    
    // informed
    this.handleModifiedUserRole(dataModel.getInformed(), taskRecordDTO, RACIVS.INFORMED);
    
    // verify
    this.handleModifiedUserRole(dataModel.getVerify(), taskRecordDTO, RACIVS.VERIFIER);
    
    // signoff
    this.handleModifiedUserRole(dataModel.getSignoff(), taskRecordDTO, RACIVS.SIGNOFF);
  }
  
  protected void handleModifiedUserRole(ITaskRoleSaveEntity userRoleSaveEntity, ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS)
      throws Exception
  {
    handleModifiedUser(userRoleSaveEntity, taskRecordDTO, typeOfRACIVS);
    handleModifiedRole(userRoleSaveEntity, taskRecordDTO, typeOfRACIVS);
  }
  
  public void handleModifiedUser(ITaskRoleSaveEntity userRoleSaveEntity, ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS) throws Exception
  {
    Set<IUserDTO> users = taskRecordDTO.getUsersMap().get(typeOfRACIVS);
    if (users == null) {
      users = new HashSet<IUserDTO>();
      taskRecordDTO.addUser(typeOfRACIVS, users);
    }
    
    handleRemoveUser(userRoleSaveEntity.getDeletedUserIds(), users);
    handleAddedUser(userRoleSaveEntity.getAddedUserIds(), users);
  }
  
  public void handleAddedUser(List<String> addedUserIds, Set<IUserDTO> users) throws Exception
  {
    if (addedUserIds != null) {
      for (String userId : addedUserIds) {
        IUserDTO user = RDBMSUtils.getOrCreateUser(userId);
        users.add(user);
      }
    }
  }
  
  public void handleRemoveUser(List<String> deletedUserIds, Set<IUserDTO> users) throws Exception
  {
    if (users == null || users.size() <= 0)
      return;
    
    if (deletedUserIds != null) {
      deletedUserIds.stream().forEach(userId -> {
        users.removeIf(userDTO -> userDTO.getUserName().equals(userId));
      });
    }
  }
  
  public void handleModifiedRole(ITaskRoleSaveEntity userRoleSaveEntity, ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS) throws Exception
  {
    Set<String> roles = taskRecordDTO.getRolesMap().get(typeOfRACIVS);
    if (roles == null) {
      roles = new HashSet<>();
      taskRecordDTO.addRole(typeOfRACIVS, roles);
    }
    
    handleRemoveRole(userRoleSaveEntity.getDeletedRoleIds(), roles);
    handleAddedRole(userRoleSaveEntity.getAddedRoleIds(), roles);
  }
  
  public void handleRemoveRole(List<String> deletedRoleIds, Set<String> roles) throws Exception
  {
    if (roles == null || roles.size() <= 0)
      return;
    
    if (deletedRoleIds != null) {
      roles.removeAll(deletedRoleIds);
    }
  }
  
  protected void manageSubTask(ISaveTaskInstanceModel dataModel, ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    this.manageAddedSubTask(dataModel.getAddedSubtasks(), taskConfig, taskRecordDTO);
    this.manageModifiedSubTask(dataModel.getModifiedSubtasks(), taskConfig, taskRecordDTO);
    this.manageDeletedSubTask(dataModel.getDeletedSubtasks(), taskRecordDTO);
  }
  
  protected void manageAddedSubTask(List<ITaskInstance> addedSubtasks, ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    Set<ITaskRecordDTO> subTaskTaskRecords = addedSubtasks.stream().map(subtask -> {
      ITaskRecordDTO subTaskRecordDTO = null;
      try {
        subTaskRecordDTO = TaskInstanceBuilder.createTaskInstance(subtask, taskConfig, this.rdbmsComponentUtils, taskRecordDTO);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      return subTaskRecordDTO;
    }).collect(Collectors.toSet());
  }
  
  protected void manageModifiedSubTask(List<ISaveTaskInstanceModel> modifiedSubtasks, ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO)
      throws Exception
  {
    if (modifiedSubtasks == null || modifiedSubtasks.size() <= 0)
      return;
    
    ITaskRecordDAO taskDAO = this.rdbmsComponentUtils.openTaskDAO();
    for (ISaveTaskInstanceModel subTaskSaveTaskInstance : modifiedSubtasks) {
      ITaskRecordDTO subTaskRecord = taskDAO.getTaskByTaskIID(Long.valueOf(subTaskSaveTaskInstance.getId()));
      this.manageModifiedTask(subTaskSaveTaskInstance, taskConfig, subTaskRecord);
      taskDAO.updateTaskRecord(subTaskRecord);
    }
    
  }
  
  protected void manageDeletedSubTask(List<String> deletedSubtasks, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    if (deletedSubtasks == null || deletedSubtasks.size() <= 0)
      return;
    
    ITaskRecordDAO openTaskDAO = this.rdbmsComponentUtils.openTaskDAO();
    for (String deletedSubtask : deletedSubtasks) {
      openTaskDAO.deleteTaskRecord(Long.parseLong(deletedSubtask));
    }
  }
  
  public void handleAddedRole(List<String> addedRoleIds, Set<String> roles) throws Exception
  {
    if (addedRoleIds != null && addedRoleIds.size() > 0) {
      roles.addAll(addedRoleIds);
    }
  }
  
  public IListModel<IGetTaskInstanceModel> setReferencedProcessDefinition(IGetCamundaProcessDefinitionResponseModel processDefinitions,
      IListModel<IGetTaskInstanceModel> requestModels) throws Exception
  {
    IListModel<IGetTaskInstanceModel> responseModel = new ListModel<>();
    List<IGetTaskInstanceModel> responseModelList = new ArrayList<>();
    
    for (IGetTaskInstanceModel model : requestModels.getList()) {
      String processDefinationId = model.getCamundaProcessDefinationId();
      String processInstanceId = model.getCamundaProcessInstanceId();
      
      ICamundaProcessModel camundaProcessModel = new CamundaProcessModel();
      camundaProcessModel.setProcessDefinationId(processDefinationId);
      camundaProcessModel.setProcessDefination((String) processDefinitions.getProcessDefinition().get(processDefinationId));
      
      ArrayList<String> completedActivityIds = new ArrayList<String>();
      ArrayList<String> currentActivityIds = new ArrayList<String>();
      
      camundaProcessUtils.getCompletedActivity(processInstanceId, completedActivityIds);
      camundaProcessUtils.getCurrentActivity(processInstanceId, currentActivityIds);
      
      camundaProcessModel.setCurrentActivityIds(currentActivityIds);
      camundaProcessModel.setCompletedActivityIds(completedActivityIds);
      model.setReferencedProcessDefination(camundaProcessModel);
      responseModelList.add(model);
    }
    responseModel.setList(responseModelList);
    return responseModel;
  }
  
  public void getGlobalPermission(ISaveTaskInstanceModel dataModel, IGetTaskInstanceModel model) throws Exception
  {
    // collect the user ids from model
    List<String> listOfUsers = new ArrayList<>();
    listOfUsers.addAll(model.getAccountable().getUserIds());
    listOfUsers.addAll(model.getConsulted().getUserIds());
    listOfUsers.addAll(model.getResponsible().getUserIds());
    listOfUsers.addAll(model.getVerify().getUserIds());
    listOfUsers.addAll(model.getSignoff().getUserIds());
    listOfUsers.addAll(model.getInformed().getUserIds());
    List<String> listOfRoles = new ArrayList<>();
    listOfRoles.addAll(model.getResponsible().getRoleIds());
    listOfUsers.addAll(model.getConsulted().getRoleIds());
    listOfUsers.addAll(model.getResponsible().getRoleIds());
    listOfUsers.addAll(model.getVerify().getRoleIds());
    listOfUsers.addAll(model.getSignoff().getRoleIds());
    listOfUsers.addAll(model.getInformed().getRoleIds());
    
    List<String> roleIdsWithCurrentUser = permissionUtils.getRoleIdsWithCurrentUser(listOfUsers, listOfRoles);
    IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity = permissionUtils
        .getGlobalPermissionForSingleEntity(dataModel.getTypes().get(0), "task", roleIdsWithCurrentUser);
    if (!globalPermissionForSingleEntity.getGlobalPermission().getCanRead() && !model.getIsPublic()) {
      throw new UserNotHaveReadPermissionForTask();
    }
    model.setGlobalPermission(globalPermissionForSingleEntity.getGlobalPermission());
  }
  
  public void prepareRequestModel(ISaveTaskInstanceModel dataModel) throws Exception
  {
    List<String> types = dataModel.getTypes();
    ITaskModel task = getTaskStrategy.execute(new IdParameterModel(types.get(0)));
    String statusTagId = task.getStatusTag();
    String priorityTagId = task.getPriorityTag();
    dataModel.setStatusTagId(statusTagId);
    dataModel.setPriorityTagId(priorityTagId);
  }
  
}
