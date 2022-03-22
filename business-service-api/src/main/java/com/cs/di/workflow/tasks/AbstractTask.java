package com.cs.di.workflow.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO.WFTaskStatus;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.executionstatus.OutputExecutionStatusModel;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.IAbstractTask;
import com.cs.workflow.base.WorkflowConstants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

public abstract class AbstractTask implements IAbstractTask {
  
  public static final String EXECUTION_STATUS              = "EXECUTION_STATUS";
  public static final String WORKFLOW_MODEL                = "WORKFLOW_MODEL";
  public static final String TOTAL_COUNT                   = "totalCount";
  public static final String PROCESS_FILES_DOWNLOAD_URL    = "PROCESS_FILES_DOWNLOAD_URL";
  public static final String DI_PROCESSING_PATH ;
 
  /**
   * This block will initialize 'ROOT_FOLDER_PATH' from properties file
   */
  static {
    DI_PROCESSING_PATH = DiFileUtils.getDiProcessingPath();
  }

  

  /**
   * Execute a task
   * @param model
   */
  public abstract void executeTask(WorkflowTaskModel model);
  
  /**
   * Execute task
   * The method runs in transaction boundary(Annotation Driven transaction)
   * All calls from this method run in the transaction If an exception is thrown
   * while executing, it rollbacks all the SQL dataBase operation in a given transaction
   * The method always runs under new transaction because this method is called from spring transaction
   * and we want to run this method under "postgresTransactionManager" as a connection is used from it.
   * @param execution
   * @param reader
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @Override
  @Transactional(transactionManager = "postgresTransactionManager", propagation = Propagation.REQUIRES_NEW)
  public void execute(Object execution, IAbstractExecutionReader reader) throws JsonParseException, JsonMappingException, IOException
  {
    WorkflowTaskModel model = new WorkflowTaskModel();
    setWorkflowModel(model, execution, reader);
    setInputParameters(model, execution, reader);
    Long id = createTaskInstance(execution, reader, model);
    executeTask(model);
    setOutputParameters(model, execution, reader);
    endTaskInstance(model, execution, reader, id);
  }
  
  /**
   * Set workflow model
   *
   * @param model
   * @param execution
   * @param reader
   */
  private void setWorkflowModel(WorkflowTaskModel model, Object execution,
      IAbstractExecutionReader reader)
  {
    Object workflowModel = reader.getVariable(execution, WORKFLOW_MODEL);
    if (workflowModel != null) {
      model.setWorkflowModel((WorkflowModel) workflowModel);
    }
    Object taskId = reader.getVariable(execution, WorkflowConstants.TASK_ID);
    if (taskId != null) {
      model.setTaskId((String) taskId);
    }
  }
  
  /**
   * Set output parameter
   * @param taskModel
   * @param execution
   * @param reader
   */
  protected void setOutputParameters(WorkflowTaskModel taskModel, Object execution,
      IAbstractExecutionReader reader)
  {
    Map<String, Object> outputParameters = taskModel.getOutputParameters();
    outputParameters.put(EXECUTION_STATUS, taskModel.getExecutionStatusTable());

    String statusKey = reader.getVariable(execution, WorkflowConstants.TASK_ID) + "_status";
    if(taskModel.getExecutionStatusTable().isErrorOccurred()) {
      reader.setVariable(execution, statusKey, "EXCEPTION");
      setDefaultValuesForMissingOutputs(taskModel, outputParameters);
    } else
      reader.setVariable(execution, statusKey, "SUCCESS");

    for (Entry<String, Object> param : outputParameters
        .entrySet()) {
      String paramName = (String) reader.getVariable(execution, param.getKey());
      if (paramName == null || paramName.isBlank()) {
        continue;
      }
      Object existingValue = reader.getVariable(execution, paramName);
      reader.setVariable(execution, paramName, getParamValue(param.getValue(), existingValue));
    }
  }

  /**
   * This method will set the value of output parameters to null in case any error occurred during task execution
   * such that some or all output parameters are not set by the actual workflow task. This will ensure smooth execution of workflow
   *
   * @param taskModel
   * @param outputParameters
   */
  private void setDefaultValuesForMissingOutputs(WorkflowTaskModel taskModel, Map<String, Object> outputParameters) {
    if(this.getOutputList()==null) {
      return;
    }
    this.getOutputList().forEach(outputKey-> {
      if(!outputParameters.containsKey(outputKey))
        outputParameters.put(outputKey, null);
    });
  }

  /**
   * Get parameter value
   * @param newValue
   * @param existingValue
   * @return
   */
  private Object getParamValue(Object newValue, Object existingValue)
  {
//    if (newValue == null || (existingValue != null && !existingValue.getClass()
//        .isInstance(newValue))) {
//      return existingValue;
//    }
    return newValue;
  }
  
  /**
   * Set input parameters
   * @param model
   * @param execution
   * @param reader
   */
  protected void setInputParameters(WorkflowTaskModel model, Object execution,
      IAbstractExecutionReader reader)
  {
    Map<String, Object> parameterMap = model.getInputParameters();
    parameterMap.put(WorkflowConstants.PROCESS_INSTANCE_ID, reader.getProcessInstanceId(execution));
    parameterMap.put(WorkflowConstants.ACTIVITY_INSTANCE_ID, reader.getActivityInstanceId(execution));
    parameterMap.put(WorkflowConstants.TASK_INSTANCE_ID, reader.getId(execution));
    for (String param : this.getInputList()) {
      parameterMap.put(param, reader.getVariable(execution, param));
    }
  }
  
  /**
   * Check runtime value
   * @param value
   * @return
   */
  public static boolean isRuntimeValue(String value)
  {
    return value != null && value.startsWith("$");
  }
  
  /** Create new Task instance.
   * @param execution
   * @param reader
   * @param model 
   * @return instanceIID
   */
  protected Long createTaskInstance(Object execution, IAbstractExecutionReader reader, WorkflowTaskModel model) 
  {
    Long instanceIID =null;
    try {
      ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO(model.getWorkflowModel().getUserSessionDto(),
          model.getWorkflowModel().getTransactionData());
      IWorkflowStatusDAO workflowStatusDAO = localeCatalogDAO.openWorkflowStatusDAO();
      IWorkflowStatusDTO statusDTO = workflowStatusDAO.newWorkflowStatusDTO();
      Long processInstanceId = Long.valueOf(reader.getProcessInstanceId(execution));
      statusDTO.setProcessInstanceID(processInstanceId);
      statusDTO.setLabel(reader.getActivityName(execution));//task label is coming from here
      statusDTO.setCreateUserID(workflowStatusDAO.getUserIID());
      statusDTO.setStartTime(System.currentTimeMillis());
      statusDTO.setTaskInstanceId(reader.getActivityInstanceId(execution));

      Map<Long, Long> instanceIidMap = (Map<Long, Long>)reader.getVariable(execution, WorkflowConstants.INSTANCE_IID);
      statusDTO.setParentIID(instanceIidMap.get(processInstanceId));
      statusDTO.setStatus(WFTaskStatus.valueOf(WFTaskStatus.PENDING.toString()).ordinal());//TODO : it will be done with PXPFDEV-15274
      workflowStatusDAO.createTaskInstance(statusDTO);
      instanceIID = statusDTO.getInstanceIID();
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return instanceIID;
  }
  
   /** Update Task instance when it gets completed.
   * @param model
   * @param reader 
   * @param execution 
   * @param id 
   */
  protected void endTaskInstance(WorkflowTaskModel model, Object execution, IAbstractExecutionReader reader, Long id)
  {
    try {
      ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO((IUserSessionDTO) model.getWorkflowModel().getUserSessionDto(),
          (ITransactionData) model.getWorkflowModel().getTransactionData());
      IWorkflowStatusDAO workflowStatusDAO = localeCatalogDAO.openWorkflowStatusDAO();
      IWorkflowStatusDTO statusDTO = workflowStatusDAO.newWorkflowStatusDTO();
      statusDTO.setInstanceIID(id);
      statusDTO.setSuccess(filterExecutionStatus(model, MessageType.SUCCESS));
      statusDTO.setError(filterExecutionStatus(model, MessageType.ERROR));
      statusDTO.setInformation(filterExecutionStatus(model, MessageType.INFORMATION));     
      statusDTO.setWarning(filterExecutionStatus(model, MessageType.WARNING));
      statusDTO.setSummary(filterExecutionStatus(model, MessageType.SUMMARY));
      statusDTO.setEndTime(System.currentTimeMillis());
      Long jobId =  (Long) model.getOutputParameters().get("JOB_ID");
      statusDTO.setJobID(jobId == null ? 0l : jobId);
      if (0l == statusDTO.getJobID()) {
        setNonBGPTaskStatus(statusDTO);
      }
      workflowStatusDAO.updateWFOrTaskStatus(statusDTO);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  /**
   * This decides the Task status for Non BGP
   * on the basis for summary for respective task 
   *
   * @return IStatusModel
   */
  private void setNonBGPTaskStatus(IWorkflowStatusDTO statusDTO)
  {
    try {
      List<IOutputExecutionStatusModel> summaryList = ObjectMapperUtil.readValue(statusDTO.getSummary(),
          new TypeReference<List<OutputExecutionStatusModel>>()
          {
          });
      
      Integer successCount = 0;
      Integer totalCount = 0;
      for (IOutputExecutionStatusModel summary : summaryList) {
        if (MessageCode.GEN000.equals(summary.getMessageCode())) {
          
          List<ObjectCode> objectCodes = Arrays.asList(summary.getObjectCodes());
          int index = 0;
          for(ObjectCode objectCode : objectCodes) {
            if (ObjectCode.SUCCESS_COUNT.equals(objectCode)) {
              successCount = Integer.valueOf(Arrays.asList(summary.getObjectValues()).get(index++));
            }
            else if (ObjectCode.TOTAL_COUNT.equals(objectCode)) {
              totalCount = Integer.valueOf(Arrays.asList(summary.getObjectValues()).get(index++));
            }
          }
        }
      }
      
      if (Objects.equals(successCount, totalCount))
        statusDTO.setStatus(WFTaskStatus.valueOf(WFTaskStatus.ENDED_SUCCESS.toString()).ordinal());
      else
        statusDTO.setStatus(WFTaskStatus.valueOf(WFTaskStatus.ENDED_ERRORS.toString()).ordinal());
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  /**
   * This method filter the ExecutionStatus
   * based of MessageType and form JSON
   * to store as JSON string
   * @param model
   * @param messageType
   * @return
   */
  public String filterExecutionStatus(WorkflowTaskModel model, MessageType messageType)
  {
    Gson gson = new Gson();
    Map<MessageType, ?> map = model.getExecutionStatusTable().getExecutionStatusTableMap();
    Map<MessageType, ?> result = map.entrySet().stream().filter(x -> x.getKey().equals(messageType))
        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    List<?> msgList = (List<?>) result.get(messageType); 
    return !msgList.isEmpty() ? gson.toJson(msgList) : null;
  }
  
  public String getOrCreateProcessingPath(WorkflowTaskModel model, String relativePath)
  {   
    String processingPathStr = getProcessingPath(model, relativePath);
    File processingFolder = new File(processingPathStr);
    if (!processingFolder.exists()) {
      processingFolder.mkdirs();
    }    
    return processingPathStr;
  }
  
  public String getProcessingPath(WorkflowTaskModel model, String relativePath)
  {   
    StringBuffer processingPath = new StringBuffer();
    processingPath.append(DI_PROCESSING_PATH + File.separator + model.getInputParameters()
        .get("processInstanceId"));
    if (relativePath.startsWith(File.separator)) {
      processingPath.append(relativePath);
    }
    else {
      processingPath.append(File.separator + relativePath);
    }  
    return processingPath.toString();
  }
  
  public String getOrCreateProcessingTempFolder(WorkflowTaskModel model)
  {
    return getOrCreateProcessingPath(model, "_" + model.getTaskId()) + File.separator;
  }
}
