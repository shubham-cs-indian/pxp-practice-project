package com.cs.core.runtime.taskinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.task.IGetTaskStrategy;
import com.cs.core.rdbms.config.idto.IFormFieldDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTOBuilder;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.runtime.builder.TaskInstanceBuilder;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.taskinstance.ICamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.exception.taskinstance.TaskCannotBeDoneException;
import com.cs.core.runtime.interactor.exception.taskinstance.TaskCannotBeSignedOffException;
import com.cs.core.runtime.interactor.model.camunda.GetCamundaProcessDefinationModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ISaveTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskRoleSaveEntity;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TaskUtil.TaskStatus;
import com.cs.core.runtime.strategy.cammunda.broadcast.IGetCamundaProcessDefinationStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaveTaskInstanceService
    extends AbstractRuntimeService<ISaveTaskInstanceModel, IGetTaskInstanceModel>
    implements ISaveTaskInstanceService {
  
  @Autowired
  protected RDBMSComponentUtils       rdbmsComponentUtils;
  
  @Autowired
  protected PermissionUtils  permissionUtils;
  
  @Autowired
  protected IGetTaskStrategy getTaskStrategy;
  
  @Autowired
  protected IGetCamundaProcessDefinationStrategy     getCamundaProcessDefinationStrategy;
  
  
  @Override
  public IGetTaskInstanceModel executeInternal(ISaveTaskInstanceModel dataModel) throws Exception
  {
    
    ITaskRecordDAO taskDAO = this.rdbmsComponentUtils.openTaskDAO();
    
    ITaskRecordDTO taskRecordDTO = taskDAO.getTaskByTaskIID(Long.valueOf(dataModel.getId()));
    
    ITaskModel taskConfig = this.getTaskInstance(taskRecordDTO.getTask().getCode());
    
    this.manageModifiedTask(dataModel, taskConfig, taskRecordDTO);
    
    taskDAO.updateTaskRecord(taskRecordDTO);
    
    ITaskRecordDTO updatedtaskRecord = taskDAO.getTaskByTaskIID(Long.valueOf(dataModel.getId()));
    IGetTaskInstanceModel model = TaskInstanceBuilder.getTaskInstance(taskConfig, updatedtaskRecord, this.rdbmsComponentUtils);
    
    List<String> roleIdsWithCurrentUser = permissionUtils.getRoleIdsWithCurrentUser();
    IGlobalPermissionWithAllowedModuleEntitiesModel globalPermissionForSingleEntity = permissionUtils
        .getGlobalPermissionForSingleEntity(model.getTypes()
            .get(0), CommonConstants.TASK, roleIdsWithCurrentUser);
    model.setGlobalPermission(globalPermissionForSingleEntity.getGlobalPermission());
    setReferencedProcessDefinition(model);
    return model;
  }
  
  protected ITaskModel getTaskInstance(String takCode) throws Exception
  {
    IIdParameterModel iIdParameterModel = new IdParameterModel(takCode);
    return this.getTaskStrategy.execute(iIdParameterModel);
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
    this.manageModifiedFormFields(dataModel, taskRecordDTO);
    this.manageModifiedAttachments(dataModel, taskRecordDTO);
  }
  
  protected void manageModifiedTag(ISaveTaskInstanceModel dataModel, ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    List<IModifiedContentTagInstanceModel> modifiedTags = dataModel.getModifiedTags();
    if(modifiedTags != null && modifiedTags.size() > 0) {
      for (IModifiedContentTagInstanceModel modifiedContentTagInstance : modifiedTags) {
        List<ITagInstanceValue> addedTagValues = modifiedContentTagInstance.getAddedTagValues();
        ITagInstanceValue tagInstanceValue = addedTagValues.get(0);
        //Status tag
        if(modifiedContentTagInstance.getTagId().equals(taskConfig.getStatusTag())) {
          if(this.checkSubTaskStatusAllow(tagInstanceValue, taskRecordDTO)) {
            IPropertyDTO propertyByCode = RDBMSUtils.getPropertyByCode(taskConfig.getStatusTag());
            ITagValueDTO tagValueDTO = RDBMSUtils.getOrCreateTagValueDTO(tagInstanceValue.getTagId(), propertyByCode.getPropertyIID());
            taskRecordDTO.setStatusTag(tagValueDTO);
          }
        }
        //Priority tag
        else if(modifiedContentTagInstance.getTagId().equals(taskConfig.getPriorityTag())) {
          IPropertyDTO propertyByCode = RDBMSUtils.getPropertyByCode(taskConfig.getPriorityTag());
          ITagValueDTO tagValueDTO = RDBMSUtils.getOrCreateTagValueDTO(tagInstanceValue.getTagId(), propertyByCode.getPropertyIID());
          taskRecordDTO.setPriorityTag(tagValueDTO);
        }
      }
    }
  }
  
  protected void manageModifiedFormFields(ISaveTaskInstanceModel dataModel,
      ITaskRecordDTO taskRecordDTO)
  {
    List<ICamundaFormField> formFields = dataModel.getFormFields();
    for (int i = 0; i < formFields.size(); i++) {
      IFormFieldDTO formFieldDTO = taskRecordDTO.getFormFields()
          .get(i);
      ICamundaFormField formFieldModel = formFields.get(i);
      formFieldDTO.setValue(formFieldModel.getValue());
    }
  }
  
  private boolean checkSubTaskStatusAllow(ITagInstanceValue tagInstanceValue,
      ITaskRecordDTO taskRecordDTO) throws Exception
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
  
  protected void manageModifiedDate(ISaveTaskInstanceModel dataModel, ITaskRecordDTO taskRecordDTO)
  {
    // Start date
    Long startDate = dataModel.getStartDate() == null ? 0 :dataModel.getStartDate(); 
    taskRecordDTO.setStartDate(startDate);
    
    // Due date
    Long dueDate = dataModel.getDueDate() == null ? 0 :dataModel.getDueDate(); 
    taskRecordDTO.setDueDate(dueDate);
    
    // over due date
    Long overDueDate = dataModel.getOverDueDate() == null ? 0 :dataModel.getOverDueDate(); 
    taskRecordDTO.setOverdueDate(overDueDate);
  }
  
  protected void manageModifiedUserRole(ISaveTaskInstanceModel dataModel,
      ITaskRecordDTO taskRecordDTO) throws Exception
  {
    //Responsible
    this.handleModifiedUserRole(dataModel.getResponsible(), taskRecordDTO, RACIVS.RESPONSIBLE);
    
    //accountable
    this.handleModifiedUserRole(dataModel.getAccountable(), taskRecordDTO, RACIVS.ACCOUNTABLE);
    
    //consulted
    this.handleModifiedUserRole(dataModel.getConsulted(), taskRecordDTO, RACIVS.CONSULTED);
    
    //informed
    this.handleModifiedUserRole(dataModel.getInformed(), taskRecordDTO, RACIVS.INFORMED);
    
    //verify
    this.handleModifiedUserRole(dataModel.getVerify(), taskRecordDTO, RACIVS.VERIFIER);
    
    //signoff
    this.handleModifiedUserRole(dataModel.getSignoff(), taskRecordDTO, RACIVS.SIGNOFF);
  }
  
  protected void handleModifiedUserRole(ITaskRoleSaveEntity userRoleSaveEntity,
      ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS) throws Exception
  {
    handleModifiedUser(userRoleSaveEntity, taskRecordDTO, typeOfRACIVS);
    handleModifiedRole(userRoleSaveEntity, taskRecordDTO, typeOfRACIVS);
  }

  public void handleModifiedUser(ITaskRoleSaveEntity userRoleSaveEntity, ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS) throws Exception
  {
    Set<IUserDTO> users = taskRecordDTO.getUsersMap().get(typeOfRACIVS);
    if(users == null) {
      users = new HashSet<IUserDTO>();
      taskRecordDTO.addUser(typeOfRACIVS, users);
    }
    
    handleRemoveUser(userRoleSaveEntity.getDeletedUserIds(), users);
    handleAddedUser(userRoleSaveEntity.getAddedUserIds(), users);
  }

  public void handleAddedUser(List<String> addedUserIds, Set<IUserDTO> users) throws Exception
  {
    Map<String, String> userIdVsName = rdbmsComponentUtils.getUserIdVsUserNameMap();
    if(addedUserIds != null) {
      for (String userId : addedUserIds) {
        String userName = userIdVsName.get(userId);
        IUserDTO user = RDBMSUtils.getOrCreateUser(userName);
        users.add(user);
      }
    }
  }
  
  public void handleRemoveUser(List<String> deletedUserIds, Set<IUserDTO> users) throws Exception
  {
    if(users == null || users.size() <= 0)
      return;
    Map<String, String> userIdVsName = rdbmsComponentUtils.getUserIdVsUserNameMap();
    if(deletedUserIds != null) {
      deletedUserIds.stream().forEach(userId -> {
        String userName = userIdVsName.get(userId);
        users.removeIf(userDTO -> userDTO.getUserName().equals(userName));
      });
    }
  }
  
  public void handleModifiedRole(ITaskRoleSaveEntity userRoleSaveEntity, ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS) throws Exception
  {
    Set<String> roles = taskRecordDTO.getRolesMap().get(typeOfRACIVS);
    if(roles == null) {
      roles = new HashSet<>();
      taskRecordDTO.addRole(typeOfRACIVS, roles);
    }
    
    handleRemoveRole(userRoleSaveEntity.getDeletedRoleIds(), roles);
    handleAddedRole(userRoleSaveEntity.getAddedRoleIds(), roles);
  }
  
  public void handleAddedRole(List<String> addedRoleIds, Set<String> roles) throws Exception
  {
    if(addedRoleIds != null && addedRoleIds.size() > 0) {
      roles.addAll(addedRoleIds);
    }
  }
  
  public void handleRemoveRole(List<String> deletedRoleIds, Set<String> roles) throws Exception
  {
    if(roles == null || roles.size() <= 0)
      return;
    
    if(deletedRoleIds != null) {
      roles.removeAll(deletedRoleIds);
    }
  }
  
  protected void manageModifiedAttachments(ISaveTaskInstanceModel dataModel,
      ITaskRecordDTO taskRecordDTO) throws Exception
  {
    this.manageAddedAttachments(dataModel.getAddedAttachments(), taskRecordDTO);
    this.manageDeletedAttachments(dataModel.getDeletedAttachments(), taskRecordDTO);
  }
  
  protected void manageAddedAttachments(List<String> addedAttachments,
      ITaskRecordDTO taskRecordDTO) throws Exception
  {
    for (String attachmentId : addedAttachments) {
      taskRecordDTO.getAttachments().add(Long.parseLong(attachmentId));
    }
  }
  
  protected void manageDeletedAttachments(List<String> deletedAttachments,
      ITaskRecordDTO taskRecordDTO) throws Exception
  {
    for (String attachmentId : deletedAttachments) {
      taskRecordDTO.getAttachments().remove(Long.parseLong(attachmentId));
    }
  }
  
  protected void manageModifiedCommands(ISaveTaskInstanceModel dataModel,
      ITaskRecordDTO taskRecordDTO) throws Exception
  {
    List<IComment> addedComments = dataModel.getAddedComments();
    if (addedComments != null && addedComments.size() > 0) {
      
      ITaskCommentDTOBuilder commentBuilder = this.rdbmsComponentUtils.openTaskDAO()
          .newTaskCommentDTOBuilder();
      List<ITaskCommentDTO> comments = taskRecordDTO.getComments();
      
      addedComments.stream()
          .forEach(command -> {
            
            //Attachments
            Set<Long> attachments = command.getAttachments()
                .stream()
                .map(attachmentId -> Long.parseLong(attachmentId))
                .collect(Collectors.toSet());
            
            ITaskCommentDTO commentDTO = commentBuilder.text(command.getText())
                .time(Long.valueOf(command.getTimestamp()))
                .userName(command.getPostedBy())
                 .attachments(attachments)
                .build();
            comments.add(commentDTO);
          });
    }
  }
  
  protected void manageSubTask(ISaveTaskInstanceModel dataModel, ITaskModel taskConfig,
      ITaskRecordDTO taskRecordDTO) throws Exception
  {
    this.manageAddedSubTask(dataModel.getAddedSubtasks(), taskConfig, taskRecordDTO);
    this.manageModifiedSubTask(dataModel.getModifiedSubtasks(), taskConfig, taskRecordDTO);
    this.manageDeletedSubTask(dataModel.getDeletedSubtasks(), taskRecordDTO);
  }
  
  protected void manageAddedSubTask(List<ITaskInstance> addedSubtasks, ITaskModel taskConfig,
      ITaskRecordDTO taskRecordDTO) throws Exception
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
  
  protected void manageModifiedSubTask(List<ISaveTaskInstanceModel> modifiedSubtasks,
      ITaskModel taskConfig, ITaskRecordDTO taskRecordDTO) throws Exception
  {
    if (modifiedSubtasks == null || modifiedSubtasks.size() <= 0)
      return;
    
    ITaskRecordDAO taskDAO = this.rdbmsComponentUtils.openTaskDAO();
    for (ISaveTaskInstanceModel subTaskSaveTaskInstance : modifiedSubtasks) {
      ITaskRecordDTO subTaskRecord = taskDAO
          .getTaskByTaskIID(Long.valueOf(subTaskSaveTaskInstance.getId()));
      this.manageModifiedTask(subTaskSaveTaskInstance, taskConfig, subTaskRecord);
      taskDAO.updateTaskRecord(subTaskRecord);
    }
    
  }
  
  protected void manageDeletedSubTask(List<String> deletedSubtasks, ITaskRecordDTO taskRecordDTO)
      throws Exception
  {
    if (deletedSubtasks == null || deletedSubtasks.size() <= 0)
      return;
    
    ITaskRecordDAO openTaskDAO = this.rdbmsComponentUtils.openTaskDAO();
    for (String deletedSubtask : deletedSubtasks) {
      openTaskDAO.deleteTaskRecord(Long.parseLong(deletedSubtask));
    }
  }
  
  public void setReferencedProcessDefinition(IGetTaskInstanceModel model) throws Exception
  {
    if (model.getIsCamundaCreated()) {
      String camundaProcessDefinationId = model.getCamundaProcessDefinationId();
      
      IGetCamundaProcessDefinationModel getProcessDefinationModel = new GetCamundaProcessDefinationModel(camundaProcessDefinationId,
          model.getCamundaProcessInstanceId());
      
      ICamundaProcessModel response = getCamundaProcessDefinationStrategy.execute(getProcessDefinationModel);
      model.setReferencedProcessDefination(response);
    }
  }
  
}
