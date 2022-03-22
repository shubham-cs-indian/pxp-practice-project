package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("executionStatusCheckTask")
public class ExecutionStatusCheckTask extends AbstractTask {
  
  // input parameters
  public static final String             EXECUTION_STATUS_MAP = "EXECUTION_STATUS_MAP";
  public static final String             MESSAGE_TYPE        = "MESSAGE_TYPE";
  
  // output parameters
  public static final String             BOOLEAN_RESULT       = "BOOLEAN_RESULT";
  
  public static final List<String>       INPUT_LIST           = Arrays.asList(EXECUTION_STATUS_MAP, MESSAGE_TYPE);
 
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(BOOLEAN_RESULT, EXECUTION_STATUS);
  
  public static final List<WorkflowType> WORKFLOW_TYPES       = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES          = Arrays.asList(EventType.BUSINESS_PROCESS);
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    Boolean isPresent = null;
    try {
      IExecutionStatus<IOutputExecutionStatusModel> executionStatusTable = (IExecutionStatus<IOutputExecutionStatusModel>) model.getInputParameters().get(EXECUTION_STATUS_MAP);
      if (executionStatusTable == null) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
            new String[] { EXECUTION_STATUS_MAP });
        return;
      }
      
      Map<MessageType, List<IOutputExecutionStatusModel>> executionStatusMap = executionStatusTable.getExecutionStatusTableMap();
      if (executionStatusMap.isEmpty()) {
        model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
            new String[] { EXECUTION_STATUS_MAP });
      }
      
      String messageType = DiValidationUtil.validateAndGetOptionalString(model, MESSAGE_TYPE);
      isPresent = messageType == null ? false : !executionStatusMap.get(Enum.valueOf(MessageType.class, messageType)).isEmpty();
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ExecutionStatusCheckTask.class.getName(), model.getTaskId() });
  
    model.getOutputParameters().put(BOOLEAN_RESULT, isPresent);
    model.getOutputParameters().put(EXECUTION_STATUS, model.getExecutionStatusTable());
  }
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<>();
    // Validate required EXECUTION_STATUS_MAP
    String executionStatus = (String) inputFields.get(EXECUTION_STATUS_MAP);
    if (DiValidationUtil.isBlank(executionStatus)) {
      returnList.add(EXECUTION_STATUS_MAP);
    }
    
    String messageType = (String) inputFields.get(MESSAGE_TYPE);
    if (DiValidationUtil.isBlank(messageType)) {
      returnList.add(MESSAGE_TYPE);
    }
    return returnList;
  }
  
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
    return TaskType.SCRIPT_TASK;
  }
}
