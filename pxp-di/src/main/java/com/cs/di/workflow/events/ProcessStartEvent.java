package com.cs.di.workflow.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.strategy.usecase.task.GetProcessDefinitionByProcessDefinitionIdStrategy;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByProcessDefinitionIdStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.camunda.GetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO.WFTaskStatus;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.tasks.AbstractTask;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowConstants;
import com.cs.workflow.base.WorkflowType;

@Component("processStartEvent")
public class ProcessStartEvent extends AbstractEvent {
  
  public final List<String>                                  INPUT_LIST      = Arrays.asList(CommonConstants.TRANSACTION_DATA,
      ISessionContext.USER_SESSION_DTO, IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP);
  public final List<String>                                  OUTPUT_LIST     = Arrays.asList(AbstractTask.EXECUTION_STATUS);
  public final List<WorkflowType>                            WORKFLOW_TYPES  = Arrays.asList();
  public final List<EventType>                               EVENT_TYPES     = Arrays.asList(EventType.BUSINESS_PROCESS,
      EventType.INTEGRATION);
  private static IUserSessionDTO                             iUserSessionDTO;
  private static ITransactionData                            transactionData = DiUtils.createTransactionData();
  private static final String PROCESS_FILES_SUFFIX = "runtime/processInstanceFiles/";
  
  @Autowired
  private IGetProcessDefinitionByProcessDefinitionIdStrategy getProcessDefinitionStrategy;
  
  @Autowired
  protected TransactionThreadData                            transactionThread;
  
  @Autowired
  GetProcessDefinitionByProcessDefinitionIdStrategy          getProcessDefinitionByProcessDefinitionIdStrategy;

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
    return TaskType.EVENT_TASK;
  }

  @Override
  protected void setInputParameters(WorkflowTaskModel model, Object execution,
                                    IAbstractExecutionReader reader) {
    super.setInputParameters(model, execution, reader);
    //Adding if to avoid resetting of workflow model and processinstanceId by call activity (subprocess)
    if(model.getWorkflowModel()==null) {
      String processDefinitionId = reader.getProcessDefinitionId(execution);
      Map<String, Object> workflowParameterMap = model.getInputParameters().get(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP) != null
              ? (Map<String, Object>) model.getInputParameters().get(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP) : new HashMap<>();
      model.setWorkflowModel(new WorkflowModel((String)model.getInputParameters().get(WorkflowConstants.PROCESS_INSTANCE_ID),
              getTransactionDataObj(model, workflowParameterMap, processDefinitionId), getUserSessionDto(model)));
      model.getWorkflowModel().setWorkflowParameterMap(workflowParameterMap);
      reader.setVariable(execution, WorkflowConstants.INSTANCE_IID, new HashMap<Long, Long>());
      reader.setVariable(execution, WorkflowConstants.ROOT_PROCESS_INSTANCE_ID, model.getWorkflowModel().getRootProcessInstanceId());
    }
  }

  /**
   * creating default transactionData if it is not present in model
   *
   * @param model
   * @param workflowParameterMap 
   * @param processDefinitionId 
   * @return
   */
  private ITransactionData getTransactionDataObj(WorkflowTaskModel model, Map<String, Object> workflowParameterMap, String processDefinitionId)
  {
    Object transactionDataObj = model.getInputParameters().get(CommonConstants.TRANSACTION_DATA);
    if (transactionDataObj != null && transactionDataObj instanceof ITransactionData) {
      transactionThread.setTransactionData((TransactionData) transactionData);
      return (ITransactionData) transactionDataObj;
    }
    IGetCamundaProcessDefinitionResponseModel processInstance = new GetCamundaProcessDefinitionResponseModel();
    IIdsListParameterModel ids = new IdsListParameterModel(Arrays.asList(processDefinitionId));
    try {
       processInstance = getProcessDefinitionByProcessDefinitionIdStrategy.execute(ids);
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("Error while fetching process defination Id");
      RDBMSLogger.instance().exception(e);
    }
    transactionData.setOrganizationId(processInstance.getOrganizationsIds().isEmpty() ? null : processInstance.getOrganizationsIds().get(0));
    transactionData.setPhysicalCatalogId(processInstance.getPhysicalCatalogIds().isEmpty() ? null : processInstance.getPhysicalCatalogIds().get(0));
    transactionThread.setTransactionData((TransactionData) transactionData);
    return transactionData;
  }

  /**
   * creating default userSessionDTO if it is not present in model
   *
   * @param model
   * @return
   */
  private IUserSessionDTO getUserSessionDto(WorkflowTaskModel model)
  {
    Object userSessionDtoObj = model.getInputParameters().get(ISessionContext.USER_SESSION_DTO);
    if (userSessionDtoObj != null && userSessionDtoObj instanceof IUserSessionDTO) {
      return (IUserSessionDTO) userSessionDtoObj;
    }
    if (iUserSessionDTO == null)
      iUserSessionDTO = DiUtils.createUserSessionDto();
    return iUserSessionDTO;
  }

  @Override
  protected void setOutputParameters(WorkflowTaskModel taskModel, Object execution, IAbstractExecutionReader reader)
  {
    reader.setVariable(execution, AbstractTask.WORKFLOW_MODEL, taskModel.getWorkflowModel());
    reader.setVariable(execution, PROCESS_FILES_DOWNLOAD_URL, DiUtils.DI_BASE_URL+PROCESS_FILES_SUFFIX+taskModel.getWorkflowModel().getRootProcessInstanceId());
    initializeWorkflowVariables(execution, reader);
    super.setOutputParameters(taskModel, execution, reader);
  }

  @SuppressWarnings("unchecked")
  private void initializeWorkflowVariables(Object execution, IAbstractExecutionReader reader)
  {
    Map<String, String> properties = reader.getProperties(execution);
    String runtimeVariablesString = properties.get(IAbstractExecutionReader.RUNTIME_VARIABLES);
    if (runtimeVariablesString == null || runtimeVariablesString.isBlank()) {
      return;
    }
    String[] runtimeVariables = runtimeVariablesString.split(",");
    for (String name : runtimeVariables) {
      Object value = reader.getVariable(execution, name);
      if (value == null) {
        reader.setVariable(execution, name, null);
      }
    }
  }

  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    return null;
  }
  
  /**
   * Create new Workflow instance.
   * 
   * @param execution
   * @param reader
   * @return instanceIID
   */
  protected Long createTaskInstance(Object execution, IAbstractExecutionReader reader, WorkflowTaskModel model)
  {
    try {
      ITransactionData transactionData = model.getWorkflowModel().getTransactionData();
      ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO(model.getWorkflowModel().getUserSessionDto(), transactionData);
      //Added for WF created with No Physical catalogue in it 
      if (localeCatalogDAO == null) {
        localeCatalogDAO = DiUtils.createLocaleCatalogDAO(DiUtils.createUserSessionDto(), DiUtils.createTransactionData());
      }
      IWorkflowStatusDAO workflowStatusDAO = localeCatalogDAO.openWorkflowStatusDAO();
      IWorkflowStatusDTO statusDTO = workflowStatusDAO.newWorkflowStatusDTO();
      Long processInstanceId = Long.valueOf(reader.getProcessInstanceId(execution));
      statusDTO.setProcessInstanceID(processInstanceId);
      String processDefinitionId = reader.getProcessDefinitionId(execution);

      IGetCamundaProcessDefinitionResponseModel response = getWorkflowIdAndLabel(processDefinitionId);
      statusDTO.setLabel(response.getLabel());// WF label from orient
      statusDTO.setCreateUserID(workflowStatusDAO.getUserIID());
      statusDTO.setStartTime(System.currentTimeMillis());
      statusDTO.setProcessId(response.getId());
      statusDTO.setStatus(WFTaskStatus.valueOf(WFTaskStatus.PENDING.toString()).ordinal());// TODO : it will be done with PXPFDEV-15274
      String endpointId = transactionData.getEndpointId();
      statusDTO.setEndpointId(endpointId == null || endpointId.isEmpty() ? (String) model.getWorkflowModel().getWorkflowParameterMap().get(CommonConstants.ENDPOINT_ID)
              : endpointId);
      statusDTO.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
      workflowStatusDAO.createWorkflowInstance(statusDTO);

      Map<Long, Long> instanceIidMap = (Map<Long, Long>)reader.getVariable(execution, WorkflowConstants.INSTANCE_IID);
      instanceIidMap.put(processInstanceId, statusDTO.getInstanceIID());
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return null;
  }

  /**
   * Get workflow Id and label from OriantDB config details by
   * processDefinitionId.
   *
   * @param processDefinitionId
   * @return
   * @throws Exception
   */
  private IGetCamundaProcessDefinitionResponseModel getWorkflowIdAndLabel(String processDefinitionId) throws Exception
  {
    IdsListParameterModel model = new IdsListParameterModel();
    model.setIds(Arrays.asList(processDefinitionId));
    return getProcessDefinitionStrategy.execute(model);
  }

  /* Empty method to avoid ending workflow instance at start event .
   * @param model
   */
  @Override
  protected void endTaskInstance(WorkflowTaskModel model, Object execution, IAbstractExecutionReader reader, Long id)
  {
  }
}
