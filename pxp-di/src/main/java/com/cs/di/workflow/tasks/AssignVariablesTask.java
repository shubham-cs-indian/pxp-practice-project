package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

/**
 * This class is assign of values from one 
 * variable to another
 * @author Subham.Shaw
 *
 */
@SuppressWarnings("unchecked")
@Component("assignVariablesTask")
public class AssignVariablesTask extends AbstractTask {
  
  public static final String                INPUT_VARIABLES_MAP = "INPUT_VARIABLES_MAP";
  public static final String                EXECUTION_STATUS    = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST          = Arrays.asList(INPUT_VARIABLES_MAP);
  public static final List<String>          OUTPUT_LIST         = Arrays.asList(EXECUTION_STATUS);
  protected static final List<WorkflowType> WORKFLOW_TYPES      = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  protected static final List<EventType>    EVENT_TYPES         = Arrays.asList(EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  
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

  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    //No validation required for INPUT_VARIABLES_MAP
    return null;
  }

  /**
   * Set input parameters
   * @param model
   * @param execution
   * @param reader
   */
  @Override
  protected void setInputParameters(WorkflowTaskModel model, Object execution,
      IAbstractExecutionReader reader)
  {
    Map<String, Object> parameterMap = model.getInputParameters();
    List<String> inputList = new ArrayList<>(); 
    Map<String, String> inputMap = (Map<String, String>) reader.getVariable(execution, INPUT_VARIABLES_MAP);
    inputList.addAll(inputMap.keySet());
    inputList.addAll(getInputList());
    for (String param : inputList) {
      parameterMap.put(param, reader.getVariable(execution, param));
    }
  }
  
  /**
   * Set output parameter
   * @param taskModel
   * @param execution
   * @param reader
   */
  @Override
  protected void setOutputParameters(WorkflowTaskModel taskModel, Object execution,
      IAbstractExecutionReader reader)
  {
    String executionStatus = (String) reader.getVariable(execution, EXECUTION_STATUS);
    taskModel.getOutputParameters().put(executionStatus, taskModel.getExecutionStatusTable());
    for (Entry<String, Object> param : taskModel.getOutputParameters()
        .entrySet()) {
      reader.setVariable(execution, param.getKey(), param.getValue());
    }
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    Map<String, String> inputVariablesMap = (Map<String, String>) DiValidationUtil.validateAndGetOptionalMap(model, INPUT_VARIABLES_MAP);
    for (Map.Entry<String, String> entry : inputVariablesMap.entrySet()) {
      String assigneeVariable = entry.getKey();
      Object assigneeVariableValue = model.getInputParameters().get(assigneeVariable);
      String outputVariable = entry.getValue();
      if (assigneeVariable == null || assigneeVariable.isEmpty() || assigneeVariableValue == null) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
            new String[] { assigneeVariable });
        continue;
      }
      else if (outputVariable == null || outputVariable.isEmpty()) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
            new String[] { outputVariable });
        continue;
      }
      model.getOutputParameters().put(outputVariable, assigneeVariableValue);
      model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
          new String[] { AssignVariablesTask.class.getName(), model.getTaskId() });
       
    }
  }
  
}