package com.cs.core.runtime.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import com.cs.config.standard.IStandardConfig;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.comment.Comment;
import com.cs.core.config.interactor.entity.comment.IComment;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.json.JSONBuilder;
import com.cs.core.rdbms.config.dto.FormFieldDTO;
import com.cs.core.rdbms.config.idto.IFormFieldDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.rdbms.task.idto.ITaskRecordDTOBuilder;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.taskinstance.CamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ICamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.model.taskinstance.AssetInfoModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.IAssetInfoModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceInformationModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskReferencedInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.TaskInstanceInformationModel;
import com.cs.core.runtime.interactor.model.taskinstance.TaskReferencedInstanceModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TaskUtil;
import com.cs.core.runtime.interactor.utils.klassinstance.TaskUtil.TaskStatus;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import com.cs.utils.BaseEntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Build Task instance using TaskRecordDTO 
 * @author bhagwat.bade
 *
 */
public class TaskInstanceBuilder {
  
  public static final String DEFAULT_TASK_STATUS = TaskStatus.TASKPLANNED.getTaskStatus();
  
  public static IGetTaskInstanceModel getTaskInstance(ITask taskConfig, ITaskRecordDTO taskRecordDTO,
      RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    IGetTaskInstanceModel taskInstance = TaskInstanceBuilder.getTaskInstanceModel(taskRecordDTO);
    Map<String, String> userNameVsId = rdbmsComponentUtils.getUserNameVsUserIdMap(); 
    TaskInstanceBuilder.setEventSchedule(taskRecordDTO, taskInstance);
    TaskInstanceBuilder.setRACIVSInfo(taskRecordDTO, taskInstance, userNameVsId);
    TaskInstanceBuilder.setLinkedEntities(taskRecordDTO, taskInstance,rdbmsComponentUtils);
    TaskInstanceBuilder.setContentInfo(taskRecordDTO, taskInstance, rdbmsComponentUtils);
    TaskInstanceBuilder.setTaskStatus(taskConfig, taskRecordDTO, taskInstance);
    TaskInstanceBuilder.setTaskPriority(taskConfig, taskRecordDTO, taskInstance);
    TaskInstanceBuilder.setSubTask(taskConfig, taskRecordDTO, rdbmsComponentUtils, taskInstance);
    TaskInstanceBuilder.setComments(taskRecordDTO, taskInstance, rdbmsComponentUtils);
    TaskInstanceBuilder.setAttachment(taskRecordDTO, taskInstance, rdbmsComponentUtils);
    TaskInstanceBuilder.setFormFields(taskRecordDTO, taskInstance);
    return taskInstance;
  }

  public static IGetTaskInstanceModel getTaskInstanceModel(ITaskRecordDTO taskRecordDTO) throws Exception
  {
    IGetTaskInstanceModel taskInstance = new GetTaskInstanceModel();
    
    taskInstance.setId(String.valueOf(taskRecordDTO.getTaskIID()));
    taskInstance.setName(taskRecordDTO.getTaskName());
    taskInstance.setLongDescription(taskRecordDTO.getDescription());
    taskInstance.setIsCamundaCreated(taskRecordDTO.isWfCreated());
    taskInstance.getTypes().add(taskRecordDTO.getTask().getCode());
    taskInstance.setStartDate(taskRecordDTO.getStartDate());
    taskInstance.setDueDate(taskRecordDTO.getDueDate());
    taskInstance.setOverDueDate(taskRecordDTO.getOverdueDate());
    taskInstance.setCreatedOn(taskRecordDTO.getCreatedTime());
    taskInstance.setCamundaProcessDefinationId(taskRecordDTO.getWfProcessID());
    taskInstance.setCamundaTaskInstanceId(taskRecordDTO.getWfTaskInstanceID());
    taskInstance.setCamundaProcessInstanceId(taskRecordDTO.getWfProcessInstanceID());
    return taskInstance;
  }
  
  public static void setEventSchedule(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance) throws Exception
  {
    IEventInstanceSchedule eventSchedule = taskInstance.getEventSchedule();
    eventSchedule.setStartTime(taskRecordDTO.getStartDate());
    eventSchedule.setEndTime(taskRecordDTO.getDueDate());
    eventSchedule.setId(taskRecordDTO.getTaskIID() + "-" + taskRecordDTO.getStartDate());
  }
  
  public static void setFormFields(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance)
      throws Exception
  {
    List<IFormFieldDTO> camundaFormField = taskRecordDTO.getFormFields();
    List<ICamundaFormField> camundaFormFields = new ArrayList<>();
    for (IFormFieldDTO camundaFormFieldSingle : camundaFormField) {
      ICamundaFormField formField = new CamundaFormField();
      formField.setId(camundaFormFieldSingle.getId());
      formField.setLabel((String) camundaFormFieldSingle.getLabel());
      formField
          .setValue(ObjectMapperUtil.convertValue(camundaFormFieldSingle.getValue(), Map.class));
      formField.setProperties((Map<String, String>) camundaFormFieldSingle.getProperties());
      if ((camundaFormFieldSingle.getProperties()).containsKey("RoleId")) {
        Map<String, Object> type = (Map<String, Object>) new HashMap<String, Object>();
        type.put("name", "cutype");
        formField.setType(type);
      }
      else {
        formField
            .setType(ObjectMapperUtil.convertValue((camundaFormFieldSingle).getType(), Map.class));
      }
      camundaFormFields.add(formField);
    }
    taskInstance.setFormFields(camundaFormFields);
  }
  
  public static void setRACIVSInfo(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance, Map<String, String> userNameVsId) throws Exception
  {
    //Responsible
    TaskInstanceBuilder.setUserRole(taskInstance.getResponsible(), taskRecordDTO, RACIVS.RESPONSIBLE, userNameVsId);
    
    //accountable
    TaskInstanceBuilder.setUserRole(taskInstance.getAccountable(), taskRecordDTO, RACIVS.ACCOUNTABLE, userNameVsId);
    
    //consulted
    TaskInstanceBuilder.setUserRole(taskInstance.getConsulted(), taskRecordDTO, RACIVS.CONSULTED, userNameVsId);
    
    //informed
    TaskInstanceBuilder.setUserRole(taskInstance.getInformed(), taskRecordDTO, RACIVS.INFORMED, userNameVsId);
    
    //verify
    TaskInstanceBuilder.setUserRole(taskInstance.getVerify(), taskRecordDTO, RACIVS.VERIFIER, userNameVsId);
    
    //signoff
    TaskInstanceBuilder.setUserRole(taskInstance.getSignoff(), taskRecordDTO, RACIVS.SIGNOFF, userNameVsId);
  }

  public static void setUserRole(ITaskRoleEntity taskRoleEntity, ITaskRecordDTO taskRecordDTO, RACIVS typeOfRACIVS, Map<String, String> userNameVsId) throws Exception
  {
    Set<IUserDTO> userIds = taskRecordDTO.getUsersMap().get(typeOfRACIVS);
    if(userIds != null && ! userIds.isEmpty()) {
      List<String> userIdsStr = userIds.stream().map(userId -> userNameVsId.get(userId.getUserName())).collect(Collectors.toList());
      taskRoleEntity.getUserIds().addAll(userIdsStr);
    }
    Set<String> roleIds = taskRecordDTO.getRolesMap().get(typeOfRACIVS);
    if(roleIds != null && ! roleIds.isEmpty()) {
      List<String> roleIdsList = roleIds.stream().collect(Collectors.toList());
      taskRoleEntity.getRoleIds().addAll(roleIdsList);
    }
  }

  public static void setLinkedEntities(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance,RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    ILinkedEntities linkedEntity = new LinkedEntities();
    linkedEntity.setId(String.valueOf(taskRecordDTO.getTaskIID())+ "-" + String.valueOf(taskRecordDTO.getEntityIID()));
    linkedEntity.setContentId(String.valueOf(taskRecordDTO.getEntityIID()));
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils
        .getBaseEntityDAO(taskRecordDTO.getEntityIID());
    BaseType baseType = baseEntityDAO.getBaseEntityDTO()
        .getBaseType();
    switch (baseType) {
      case ARTICLE:
        linkedEntity.setType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
        break;
      case ASSET:
        linkedEntity.setType(Constants.ASSET_INSTANCE_BASE_TYPE);
        break;
      case TEXT_ASSET:
        linkedEntity.setType(Constants.TEXTASSET_INSTANCE_BASE_TYPE);
        break;
      default:
        break;
    }
    if(taskRecordDTO.getPropertyIID() != 0) {
      IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByIID(taskRecordDTO.getPropertyIID());
      linkedEntity.setElementId(propertyDTO.getPropertyCode());
      
      if(propertyDTO.getPropertyCode().equals(IStandardConfig.StandardProperty.assetcoverflowattribute.toString())) {
        IJSONContent xyposition = taskRecordDTO.getPosition();
        IPosition position = linkedEntity.getPosition();
        position.setX(xyposition.getInitField(IPosition.X, 0));
        position.setY(xyposition.getInitField(IPosition.Y, 0));
      }
    }
    taskInstance.getLinkedEntities().add(linkedEntity);
  }

  @SuppressWarnings("unchecked")
  public static void setContentInfo(ITaskRecordDTO taskRecordDTO,
      IGetTaskInstanceModel taskInstance, RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    IAssetInfoModel assetinfo = new AssetInfoModel();
    Map<String,Object> mapObj = new ObjectMapper().convertValue(assetinfo, Map.class);
    Map<String, ITaskReferencedInstanceModel> listOfRefInstances = taskInstance.getReferencedInstances();
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(taskRecordDTO.getEntityIID());
    ITaskReferencedInstanceModel taskReferencedInstanceModel = new TaskReferencedInstanceModel();
    taskReferencedInstanceModel.setId(String.valueOf(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID()));  
    taskReferencedInstanceModel.setAssetInstance(mapObj);
    taskReferencedInstanceModel.setLabel(baseEntityDAO.getBaseEntityDTO().getBaseEntityName());
    taskReferencedInstanceModel.setTypes(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO));
    listOfRefInstances.put(String.valueOf(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID()), taskReferencedInstanceModel);
  }
  
  public static ITaskInstanceInformationModel getTaskInstanceInformationModel(ITaskRecordDTO taskRecordDTO, ITask taskConfig) throws Exception
  {
    ITaskInstanceInformationModel taskInstanceInformationModel = new TaskInstanceInformationModel();

    taskInstanceInformationModel.setId(String.valueOf(taskRecordDTO.getTaskIID()));
    taskInstanceInformationModel.setDueDate(taskRecordDTO.getDueDate());
    taskInstanceInformationModel.setName(taskRecordDTO.getTaskName());
    taskInstanceInformationModel.setIsCamundaCreated(taskRecordDTO.isWfCreated());
    taskInstanceInformationModel.setOverDueDate(taskRecordDTO.getOverdueDate());
    taskInstanceInformationModel.getTypes().add(taskRecordDTO.getTask().getCode());
    
    ILinkedEntities linkedEntity = new LinkedEntities();
    linkedEntity.setId(String.valueOf(taskRecordDTO.getTaskIID())+ "-" + String.valueOf(taskRecordDTO.getEntityIID()));
    linkedEntity.setContentId(String.valueOf(taskRecordDTO.getEntityIID()));
    if(taskRecordDTO.getPropertyIID() != 0) {
      IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByIID(taskRecordDTO.getPropertyIID());
      linkedEntity.setElementId(propertyDTO.getPropertyCode());
      if (propertyDTO.getPropertyCode()
          .equals(IStandardConfig.StandardProperty.assetcoverflowattribute.toString())) {
        IJSONContent xyposition = taskRecordDTO.getPosition();
        IPosition position = linkedEntity.getPosition();
        position.setX(xyposition.getInitField(IPosition.X, 0));
        position.setY(xyposition.getInitField(IPosition.Y, 0));
      }
    }

    taskInstanceInformationModel.getLinkedEntities().add(linkedEntity);
    
    ITagValueDTO statusTag = taskRecordDTO.getStatusTag();
    String StatusTagConfig = taskConfig == null ? "taskstatustag" : taskConfig.getStatusTag();
    ITagInstance statusTagInstance = setTagInstance(taskRecordDTO, statusTag, StatusTagConfig);
    taskInstanceInformationModel.setStatus(statusTagInstance);
    
    ITagValueDTO priorityTag = taskRecordDTO.getPriorityTag();
    String priorityTagConfig = taskConfig == null ? "" : taskConfig.getPriorityTag();
    ITagInstance priorityTagInstance = setTagInstance(taskRecordDTO, priorityTag, priorityTagConfig);
    taskInstanceInformationModel.setPriority(priorityTagInstance);
    
    return taskInstanceInformationModel;
  }
  
  public static void setTaskStatus(ITask taskConfig, ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance)
  {
    ITagValueDTO statusTag = taskRecordDTO.getStatusTag();
    String StatusTagConfig = taskConfig == null ? "taskstatustag" : taskConfig.getStatusTag();
    ITagInstance tagInstance = setTagInstance(taskRecordDTO, statusTag, StatusTagConfig);
    taskInstance.setStatus(tagInstance);
  }
  
  public static void setTaskPriority(ITask taskConfig, ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance)
  {
    ITagValueDTO priorityTag = taskRecordDTO.getPriorityTag();
    String priorityTagConfig = taskConfig == null ? "" : taskConfig.getPriorityTag();
    ITagInstance tagInstance = setTagInstance(taskRecordDTO, priorityTag, priorityTagConfig);
    taskInstance.setPriority(tagInstance);
  }

  private static ITagInstance setTagInstance(ITaskRecordDTO taskRecordDTO, ITagValueDTO tagValue, String tagCode)
  {
    ITagInstance tagInstance = null;
    if(tagCode != null && ! tagCode.trim().equals("")) {
      tagInstance = new TagInstance();
      tagInstance.setId(taskRecordDTO.getTaskIID() + "-" + tagCode);
      tagInstance.setBaseType(TagInstance.class.getName());
      tagInstance.setTagId(tagCode);
      
      if(tagValue != null) {
        List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
        ITagInstanceValue tagInstanceValue = new TagInstanceValue();
        tagInstanceValue.setTagId(tagValue.getCode());
        tagInstanceValue.setId(tagCode + "-" + tagValue.getCode());
        tagInstanceValue.setCode(tagCode + "-" + tagValue.getCode());
        tagInstanceValue.setRelevance(100);
        tagInstanceValues.add(tagInstanceValue);
        tagInstance.setTagValues(tagInstanceValues);
      }
    }
    return tagInstance;
  }
  
  public static void setComments(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance, RDBMSComponentUtils rdbmsComponentUtils)
  {
    List<ITaskCommentDTO> comments = taskRecordDTO.getComments();
    if(comments != null && comments.size() > 0) {
      List<IComment> commentInstances = taskInstance.getComments();
      comments.stream().forEach(comment -> {
        try {
          IComment commentInstance = new Comment();
          commentInstance.setId(String.valueOf(comment.getTime()));
          commentInstance.setPostedBy(comment.getUserName());
          commentInstance.setText(comment.getText());
          TaskInstanceBuilder.setAttachmentData(commentInstance, comment, taskInstance, rdbmsComponentUtils);
          commentInstances.add(commentInstance);
        }
        catch (Exception e) {
         throw new RuntimeException(e);
        }
      });
    }
  }
  
  public static void setAttachment(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance,
      RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    List<String> attachments = taskInstance.getAttachments();
    Map<String, IAssetInfoModel> referencedAssets = taskInstance.getReferencedAssets();
    Set<Long> attachmentIds = taskRecordDTO.getAttachments();
    for (long attachmentId : attachmentIds) {
      attachments.add(String.valueOf(attachmentId));
      IAssetInfoModel assetInfoModel = TaskInstanceBuilder.getAssetInfo(attachmentId, rdbmsComponentUtils);
      referencedAssets.put(String.valueOf(attachmentId), assetInfoModel);
    }
  }
  
  public static void setAttachmentData(IComment commentInstance, ITaskCommentDTO comment,
      IGetTaskInstanceModel taskInstance, RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    List<String> attachments = commentInstance.getAttachments();
    Map<String, IAssetInfoModel> referencedAssets = taskInstance.getReferencedAssets();
    
    for (long attachmentId : comment.getAttachments()) {
      attachments.add(String.valueOf(attachmentId));
      IAssetInfoModel assetInfoModel = TaskInstanceBuilder.getAssetInfo(attachmentId, rdbmsComponentUtils);
      referencedAssets.put(String.valueOf(attachmentId), assetInfoModel);
    }
  }
  
  public static IAssetInfoModel getAssetInfo(long attachmentId, RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(attachmentId);
    IJSONContent entityExtension = baseEntityDAO.getBaseEntityDTO()
        .getEntityExtension();
    
    IAssetInfoModel assetInfoModel = new AssetInfoModel();
    assetInfoModel.setType(entityExtension.getInitField(IImageAttributeInstance.TYPE, ""));
    assetInfoModel.setAssetObjectKey(entityExtension.getInitField(IImageAttributeInstance.ASSET_OBJECT_KEY, ""));
    assetInfoModel.setFilename(entityExtension.getInitField(IImageAttributeInstance.FILENAME, ""));
    assetInfoModel.setPreviewKey(entityExtension.getInitField(IImageAttributeInstance.PREVIEW_IMAGE_KEY, ""));
    assetInfoModel.setThumbKey(entityExtension.getInitField(IImageAttributeInstance.THUMB_KEY, ""));
    
    IJSONContent propertiesJson = null;
    propertiesJson = entityExtension.getInitField(IImageAttributeInstance.PROPERTIES, propertiesJson);
    if(propertiesJson != null) {
      HashMap<String, String> readValue = ObjectMapperUtil.readValue(propertiesJson.toString(), HashMap.class);
      assetInfoModel.setProperties(readValue);
    }
    
    return assetInfoModel;
  }
  
  public static void setSubTask(ITask taskConfig, ITaskRecordDTO taskRecordDTO,
      RDBMSComponentUtils rdbmsComponentUtils, IGetTaskInstanceModel taskInstance)
  {
    List<ITaskInstance> subTasks = taskRecordDTO.getChildren().stream().map(subTaskRecord -> {
      ITaskInstance subTaskInstance = null;
      try {
        subTaskInstance = TaskInstanceBuilder.getTaskInstance(taskConfig, subTaskRecord, rdbmsComponentUtils);
        taskInstance.getReferencedAssets().putAll(((IGetTaskInstanceModel)subTaskInstance).getReferencedAssets());
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      return subTaskInstance;
    }).collect(Collectors.toList());
    
    taskInstance.setSubTasks(subTasks);
  }
  
  
  public static ITaskRecordDTO createTaskInstance(ITaskInstance dataModel,
      ITaskModel referencedTask, RDBMSComponentUtils rdbmsComponentUtils, ITaskRecordDTO parentTask) throws Exception
  {
    ITaskRecordDAO taskRecordDAO = rdbmsComponentUtils.openTaskDAO();
    TaskType taskType = TaskUtil.getTakType(referencedTask.getType());
    ITaskDTO taskDTO = rdbmsComponentUtils.getLocaleCatlogDAO()
        .newTaskDTO(referencedTask.getCode(), taskType);
    
    ITaskRecordDTO taskRecordDTO = null;
    
    if (dataModel.getLinkedEntities() != null && dataModel.getLinkedEntities()
        .size() > 0) {
      Map<String, String> userIdVsNameMap = rdbmsComponentUtils.getUserIdVsUserNameMap();
      //Set current user IId as default
      Set<IUserDTO> currentUserIID = new HashSet<>();
      currentUserIID.add(RDBMSUtils.getOrCreateUser(rdbmsComponentUtils.getUserName()));

      // Set responsible user
      Set<IUserDTO> responsibilityuser = new HashSet<>();
      List<String> responsibleuser = dataModel.getResponsible().getUserIds();
      if (responsibleuser != null) {
        for (String userId : responsibleuser) {
          IUserDTO user = RDBMSUtils.getOrCreateUser(userIdVsNameMap.get(userId));
          responsibilityuser.add(user);
        }
      }

      // Set responsible role
      Set<String> responsibliltyrole = new HashSet<String>();
      List<String> responsiblerole = dataModel.getResponsible().getRoleIds();
      if (responsiblerole != null) {
        for (String roleid : responsiblerole) {
          responsibliltyrole.add(roleid);
        }
      }

      // Set signoff user
      Set<IUserDTO> signoffUsers = new HashSet<>();
      List<String> signoffusers = dataModel.getSignoff().getUserIds();
      if (signoffusers != null) {
        for (String userId : signoffusers) {
          IUserDTO user = RDBMSUtils.getOrCreateUser(userIdVsNameMap.get(userId));
          signoffUsers.add(user);
        }
      }

      // Set consulted user
      Set<IUserDTO> consultedUsers = new HashSet<>();
      List<String> consultedusers = dataModel.getConsulted().getUserIds();
      if (consultedusers != null) {
        for (String userId : consultedusers) {
          IUserDTO user = RDBMSUtils.getOrCreateUser(userIdVsNameMap.get(userId));
          consultedUsers.add(user);
        }
      }

       // Set consulted role
      Set<String> consultedRole = new HashSet<String>();
      List<String> consultedrole = dataModel.getConsulted().getRoleIds();
      if (consultedrole != null) {
        for (String roleid : consultedrole) {
          consultedRole.add(roleid);
        }
      }

      // Set informed users
      Set<IUserDTO> informedUsers = new HashSet<>();
      List<String> informedusers = dataModel.getInformed().getUserIds();
      if (informedusers != null) {
        for (String userId : informedusers) {
          IUserDTO user = RDBMSUtils.getOrCreateUser(userIdVsNameMap.get(userId));
          informedUsers.add(user);
        }
      }

      // Set informed role
      Set<String> informedRole = new HashSet<String>();
      List<String> informedrole = dataModel.getInformed().getUserIds();
      if (informedrole != null) {
        for (String roleid : informedrole) {
          informedRole.add(roleid);
        }
      }

      // Set verified users
      Set<IUserDTO> verifiedUsers = new HashSet<>();
      List<String> verifiedusers = dataModel.getVerify().getUserIds();
      if (verifiedusers != null) {
        for (String userId : verifiedusers) {
          IUserDTO user = RDBMSUtils.getOrCreateUser(userIdVsNameMap.get(userId));
          verifiedUsers.add(user);
        }
      }
      //Status tag
      String taskStatus = referencedTask.getStatusTag();
      ITagValueDTO newTagValueDTO = null;
      if(taskStatus != null && ! taskStatus.trim().equals("")) {
        IPropertyDTO propertyByCode = RDBMSUtils.getPropertyByCode(taskStatus);
        newTagValueDTO = RDBMSUtils.getOrCreateTagValueDTO(DEFAULT_TASK_STATUS, propertyByCode.getPropertyIID());
      }
      
      //entity IID
      long entityIID = 0;
      ILinkedEntities linkedEntity = dataModel.getLinkedEntities()
          .get(0);
      if(linkedEntity.getContentId() != null && ! linkedEntity.getContentId().trim().equals("")) {
        entityIID = Long.parseLong(linkedEntity.getContentId());
      }else if(parentTask != null) {
        entityIID = parentTask.getEntityIID();
      }
      
      // formfields
      List<ICamundaFormField> formfield = dataModel.getFormFields();
      List<IFormFieldDTO> camundaFormFields = new ArrayList<>();
      for (ICamundaFormField camundaFormField : formfield) {
        FormFieldDTO formField = new FormFieldDTO();
        formField.setId(camundaFormField.getId());
        formField.setLabel(camundaFormField.getLabel());
        formField.setValue(ObjectMapperUtil.convertValue(camundaFormField.getValue(), Map.class));
        formField.setProperties((Map<String, String>) camundaFormField.getProperties());
        if (camundaFormField.getProperties()
            .containsKey("RoleId")) {
          Map<String, Object> type = (Map<String, Object>) new HashMap<String, Object>();
          type.put("name", "cutype");
          formField.setType(type);
        }
        else {
          formField.setType(ObjectMapperUtil.convertValue(camundaFormField.getType(), Map.class));
        }
        camundaFormFields.add(formField);
      }
      
      //property
      long propertyIID = 0l;
      String elementId = linkedEntity.getElementId();
      if(elementId != null && ! elementId.trim().equals("")) {
          IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(elementId);
          propertyIID = propertyDTO.getPropertyIID();
      }
      
      //position
      IPosition position = linkedEntity.getPosition();
      String positionJSON = null;
      if(position != null && (position.getX() != -1 || position.getX() != -1)) {
        IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(IStandardConfig.StandardProperty.assetcoverflowattribute.toString());
        propertyIID = propertyDTO.getPropertyIID();
        positionJSON = JSONBuilder.assembleJSON(JSONBuilder.newJSONField(IPosition.X, position.getX()),
            JSONBuilder.newJSONField(IPosition.Y, position.getY()));
      }

      //parent task
      long parentTaskIID = 0l;
      if(parentTask != null)
        parentTaskIID = parentTask.getTaskIID();
      
      ITaskRecordDTOBuilder taskRecordDTOBuilder = taskRecordDAO
          .newTaskRecordDTOBuilder(entityIID, taskDTO);
      
       taskRecordDTO = taskRecordDTOBuilder.startDate(System.currentTimeMillis())
          .createdTime(System.currentTimeMillis())
          .taskName(dataModel.getName())
          .parentTaskIID(parentTaskIID)
          .propertyIID(propertyIID)
          .wfCreated(dataModel.getIsCamundaCreated())
           .userMap(RACIVS.RESPONSIBLE, ( responsibilityuser.size() == 0 ? currentUserIID :responsibilityuser))
           .roleMap(RACIVS.RESPONSIBLE,( responsibliltyrole))
           .userMap(RACIVS.ACCOUNTABLE, currentUserIID)
           .userMap(RACIVS.CONSULTED, (consultedUsers))
           .roleMap(RACIVS.CONSULTED,( consultedRole))
           .userMap(RACIVS.INFORMED, (informedUsers))
           .roleMap(RACIVS.INFORMED,( informedRole))
           .userMap(RACIVS.VERIFIER, ( verifiedUsers))
           .userMap(RACIVS.SIGNOFF, ( signoffUsers.size() == 0 ? currentUserIID :signoffUsers))
           .wfProcessID(dataModel.getCamundaProcessDefinationId())
           .wfProcessInstanceID(dataModel.getCamundaProcessInstanceId())
           .wfTaskInstanceID(dataModel.getCamundaTaskInstanceId())
          .statusTag(newTagValueDTO)
          .position(positionJSON)
          .wfFormFields(camundaFormFields)
          .build();
      taskRecordDAO.createTaskRecord(taskRecordDTO);
      }
      
    return taskRecordDTO;
  }
  
  public static ITaskInstance getTakInstanceForCalenderView(ITaskRecordDTO taskRecordDTO, RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    IGetTaskInstanceModel taskInstance = TaskInstanceBuilder.getTaskInstanceModel(taskRecordDTO);
    TaskInstanceBuilder.setCalendarDates(taskRecordDTO, taskInstance);
    TaskInstanceBuilder.setEventSchedule(taskRecordDTO, taskInstance);
    TaskInstanceBuilder.setLinkedEntities(taskRecordDTO, taskInstance,rdbmsComponentUtils);
    TaskInstanceBuilder.setCalenderViewSubTask(taskRecordDTO, rdbmsComponentUtils, taskInstance);
    return taskInstance;
  }
  
  public static void setCalendarDates(ITaskRecordDTO taskRecordDTO, IGetTaskInstanceModel taskInstance) throws Exception
  {
    List<ITimeRange> calendarDates = taskInstance.getCalendarDates();
    ITimeRange timeRange = new TimeRange();
    timeRange.setStartTime(taskRecordDTO.getStartDate());
    timeRange.setEndTime(taskRecordDTO.getDueDate());
    calendarDates.add(timeRange);
  }
  
  public static void setCalenderViewSubTask(ITaskRecordDTO taskRecordDTO,
      RDBMSComponentUtils rdbmsComponentUtils, IGetTaskInstanceModel taskInstance)
  {
    List<ITaskInstance> subTasks = taskRecordDTO.getChildren().stream().map(subTaskRecord -> {
      ITaskInstance subTaskInstance = null;
      try {
        subTaskInstance = TaskInstanceBuilder.getTakInstanceForCalenderView(subTaskRecord, rdbmsComponentUtils);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
      return subTaskInstance;
    }).collect(Collectors.toList());
    
    taskInstance.setSubTasks(subTasks);
  }
}
