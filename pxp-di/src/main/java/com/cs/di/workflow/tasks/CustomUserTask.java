package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.config.interactor.entity.datarule.LinkedEntities;
import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.propertycollection.SectionElementPosition;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.task.IGetTaskStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUserStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IRepeatCalendarEvent;
import com.cs.core.runtime.interactor.entity.eventinstance.RepeatCalendarEvent;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.taskinstance.CamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ICamundaFormField;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.entity.taskinstance.TaskRoleEntity;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.TaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.taskinstance.ICreateTaskInstance;
import com.cs.core.runtime.interactor.usecase.taskinstance.ISaveTaskInstance;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowConstants;
import com.cs.workflow.base.WorkflowType;

@Component("customUserTask")
public class CustomUserTask extends AbstractTask {
  
  @Autowired
  protected ICreateTaskInstance          createTaskInstance;
  
  @Autowired
  protected SessionContextCustomProxy     context;
  
  @Autowired
  protected ISaveTaskInstance            saveTaskInstance;
  
  @Autowired
  protected IGetUserStrategy             getUserStrategy;
  
  @Autowired
  protected IGetTaskStrategy             getTaskStrategy;
  
  @Autowired
  protected TransactionThreadData        transactionThread;
  
  protected IUserModel                   userModel;
  
  
  public static final String             RESPONSIBLE             = "responsible";
  public static final String             BASETYPE                = "baseType";
  public static final String             KLASSINSTANCEID         = "klassInstanceId";
  public static final String             ACCOUNTABLE             = "accountable";
  public static final String             SIGN_OFF                = "signoff";
  public static final String             CONSULTED               = "consulted";
  public static final String             INFORMED                = "informed";
  public static final String             VERIFIED                = "verify";
  public static final String             TYPES                   = "TYPES";
  public static final String             EXECUTION_STATUS        = "EXECUTION_STATUS";
  public static final String             PROCESS_DEFINITION_ID   = "processDefinitionId";
  public static final String             ACTIVITY_NAME           = "activityName";
  public static final String             FORM_FILEDS              = "form_fields";
  
  protected ThreadLocal<IUserSessionDTO> userSessionDTO          = new ThreadLocal<>();
  
  public static final List<String>       INPUT_LIST              = Arrays.asList(RESPONSIBLE, ACCOUNTABLE, SIGN_OFF, CONSULTED, INFORMED,
      VERIFIED, TYPES, BASETYPE, KLASSINSTANCEID, PROCESS_DEFINITION_ID);
  
  public static final List<String>       OUTPUT_LIST             = Arrays.asList(EXECUTION_STATUS);
  
  protected Set<String>                  mandateResponsibilities = new HashSet<String>(
      Arrays.asList(ITaskInstanceModel.RESPONSIBLE, ITaskInstanceModel.ACCOUNTABLE, ITaskInstanceModel.SIGN_OFF));
  
  public static final List<EventType>    EVENT_TYPES             = Arrays.asList(EventType.BUSINESS_PROCESS);
  public static final List<WorkflowType> WORKFLOW_TYPES          = Arrays.asList(WorkflowType.TASKS_WORKFLOW);
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }
  
  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }
  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.USER_TASK;
  }
  
  @Override
  protected void setInputParameters(WorkflowTaskModel taskModel, Object execution, IAbstractExecutionReader reader)
  {
    super.setInputParameters(taskModel, execution, reader);
    Map<String, Object> parameterMap = taskModel.getInputParameters();
    Object processDefintionId = reader.getProcessDefinitionId(execution);
    parameterMap.put(PROCESS_DEFINITION_ID, processDefintionId);
    String activityName = reader.getActivityName(execution);
    context.setUserSessionDTOInThreadLocal(taskModel.getWorkflowModel().getUserSessionDto());
    List<?> formFields = reader.getFormFields(execution);
    parameterMap.put(FORM_FILEDS,formFields);
    parameterMap.put(ACTIVITY_NAME, activityName);
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    List<String> types = (List<String>) inputFields.get(TYPES);
    if (types == null || types.size() == 0) {
      returnList.add(TYPES);
    }
    return returnList;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    String taskInstanceId = (String) model.getInputParameters().get(WorkflowConstants.TASK_INSTANCE_ID);
    String userIdFromProcess = model.getWorkflowModel().getTransactionData().getUserId();
    ITaskInstanceModel taskInstanceModel = new TaskInstanceModel();
    try {
      setTaskInstanceModel(model, taskInstanceModel);
      taskInstanceModel.setIsCamundaCreated(true);
      taskInstanceModel.setCamundaTaskInstanceId(taskInstanceId);
      taskInstanceModel.setCamundaProcessInstanceId((String) model.getInputParameters().get(WorkflowConstants.PROCESS_INSTANCE_ID));
      taskInstanceModel.setCamundaProcessDefinationId((String) model.getInputParameters().get(PROCESS_DEFINITION_ID));
      
      List<Map<String, Object>> formFields = (List<Map<String, Object>>) model.getInputParameters().get(FORM_FILEDS);
      List<ICamundaFormField> camundaFormFields = new ArrayList<>();
      for (Map<String, Object> camundaFormField : formFields) {
        ICamundaFormField formField = new CamundaFormField();
        formField.setId((String) camundaFormField.get("id"));
        formField.setLabel((String) camundaFormField.get("label"));
        formField.setValue(ObjectMapperUtil.convertValue(camundaFormField.get("value"), Map.class));
        formField.setProperties(ObjectMapperUtil.convertValue(camundaFormField.get("properties"), Map.class));
        if (((Map<String, Object>) camundaFormField.get("properties")).containsKey("RoleId")) {
          Map<String, Object> type = (Map<String, Object>) new HashMap<String, Object>();
          type.put("name", "cutype");
          formField.setType(type);
        }
        else {
          formField.setType(ObjectMapperUtil.convertValue(camundaFormField.get("type"), Map.class));
        }
        camundaFormFields.add(formField);
      }
      taskInstanceModel.setFormFields(camundaFormFields);
      userModel = getUserStrategy.execute(new IdParameterModel((String) userIdFromProcess));      
      taskInstanceModel.setResponsible(getRACIVSModel(RESPONSIBLE, model));
      taskInstanceModel.setAccountable(getRACIVSModel(ACCOUNTABLE, model));
      taskInstanceModel.setConsulted(getRACIVSModel(CONSULTED, model));
      taskInstanceModel.setInformed(getRACIVSModel(INFORMED, model));
      taskInstanceModel.setVerify(getRACIVSModel(VERIFIED, model));
      taskInstanceModel.setSignoff(getRACIVSModel(SIGN_OFF, model));
      
      createTaskInstance.execute(taskInstanceModel);
      
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }
  
  private ITaskRoleEntity getRACIVSModel(String responsibility, WorkflowTaskModel model) throws Exception
  {
    String responsibilityJson = (String) model.getInputParameters().get(responsibility);
    TaskRoleEntity taskRoleEntity;
    taskRoleEntity = ObjectMapperUtil.readValue(responsibilityJson, TaskRoleEntity.class);
    
    if (mandateResponsibilities.contains(responsibility)) {
      addCurrentUserIfEmpty(taskRoleEntity.getUserIds());
    }
    return taskRoleEntity;
  }
  
  private void addCurrentUserIfEmpty(List<String> users)
  {
    if (users.isEmpty()) {
      users.add(userModel.getId());
    }
    
  }
  
  @SuppressWarnings("unchecked")
  private void setTaskInstanceModel(WorkflowTaskModel model, ITaskInstanceModel taskInstanceModel) throws Exception
  {
    transactionThread.setTransactionData(DiUtils.getTransactionData(model));
    Long timestamp = System.currentTimeMillis();
    String userIdFromProcess = model.getWorkflowModel().getTransactionData().getUserId();
    String physicalCatalogId = model.getWorkflowModel().getTransactionData().getPhysicalCatalogId();
    String organizationId = model.getWorkflowModel().getTransactionData().getOrganizationId();
    taskInstanceModel.setId(UUID.randomUUID().toString());
    taskInstanceModel.setBaseType("com.cs.runtime.interactor.entity.taskinstance.TaskInstance");
    taskInstanceModel.setCreatedBy(userIdFromProcess);
    taskInstanceModel.setCreatedOn(timestamp);
    taskInstanceModel.setName((String) model.getInputParameters().get(ACTIVITY_NAME));
    taskInstanceModel.setStartDate(timestamp);
    taskInstanceModel.setIsFavourite(false);
    taskInstanceModel.setIsPublic(false);
    taskInstanceModel.setLongDescription("");
    taskInstanceModel.setPhysicalCatalogId(physicalCatalogId);
    taskInstanceModel.setOrganizationId(organizationId);
    List<String> types = (List<String>) model.getInputParameters().get(TYPES);
    taskInstanceModel.setTypes(types);
    Map<String, Boolean> dayOfTheWeek = new HashMap<String, Boolean>();
    dayOfTheWeek.put("FRI", false);
    dayOfTheWeek.put("MON", false);
    dayOfTheWeek.put("SAT", false);
    dayOfTheWeek.put("SUN", false);
    dayOfTheWeek.put("THU", false);
    dayOfTheWeek.put("TUE", false);
    dayOfTheWeek.put("WED", false);
    
    IRepeatCalendarEvent repeateCalanderEvent = new RepeatCalendarEvent();
    repeateCalanderEvent.setRepeatType("NONE");
    repeateCalanderEvent.setEndsAfter(0);
    repeateCalanderEvent.setRepeatEvery(1);
    repeateCalanderEvent.setDaysOfWeek(dayOfTheWeek);
    
    IEventInstanceSchedule eventInstanceSchedule = new EventInstanceSchedule();
    eventInstanceSchedule.setId(UUID.randomUUID().toString());
    eventInstanceSchedule.setLastModifiedBy(userIdFromProcess);
    eventInstanceSchedule.setStartTime(timestamp);
    eventInstanceSchedule.setRepeat(repeateCalanderEvent);
    taskInstanceModel.setEventSchedule(eventInstanceSchedule);
    
    // camundaservice.get
    String baseType = model.getInputParameters().get(IKlassInstance.BASETYPE).toString();
    List<ILinkedEntities> linkedEntities = new ArrayList<ILinkedEntities>();
    ILinkedEntities linkedEntity = new LinkedEntities();
    linkedEntity.setContentId((String) model.getInputParameters().get(KLASSINSTANCEID));
    linkedEntity.setId(UUID.randomUUID().toString());
    switch (baseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        linkedEntity.setType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
        break;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        linkedEntity.setType(Constants.ASSET_INSTANCE_BASE_TYPE);
        break;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        linkedEntity.setType(Constants.TEXTASSET_INSTANCE_BASE_TYPE);
        break;
      default:
        break;
    }
    
    IPosition position = new SectionElementPosition();
    position.setX(-1);
    position.setY(-1);
    linkedEntity.setPosition(position);
    
    linkedEntities.add(linkedEntity);
    taskInstanceModel.setLinkedEntities(linkedEntities);
    
    ITagInstance status = new TagInstance();
    status.setBaseType("com.cs.runtime.interactor.entity.TagInstance");
    status.setId(UUID.randomUUID().toString());
    status.setTagId("taskstatustag");
    
    List<ITagInstanceValue> tagValues = new ArrayList<ITagInstanceValue>();
    ITagInstanceValue tagValue = new TagInstanceValue();
    tagValue.setRelevance(100);
    tagValue.setId(UUID.randomUUID().toString());
    tagValue.setTimestamp(timestamp.toString());
    tagValue.setTagId("taskplanned");
    tagValues.add(tagValue);
    
    status.setTagValues(tagValues);
    taskInstanceModel.setStatus(status);
    try {
      ITaskModel resp = getTaskStrategy.execute(new IdParameterModel(types.get(0)));
      
      String priorityTagId = resp.getPriorityTag();
      if (priorityTagId != null) {
        ITagInstance priority = new TagInstance();
        priority.setBaseType("com.cs.runtime.interactor.entity.TagInstance");
        priority.setId(UUID.randomUUID().toString());
        priority.setTagId(priorityTagId);
        priority.setTagValues(new ArrayList<>());
        taskInstanceModel.setPriority(priority);
      }
    }
    catch (Exception exception) {
      exception.printStackTrace();
      RDBMSLogger.instance().exception(exception);
    }
  }
  
}
