package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@SuppressWarnings("unchecked")
@Component("listRemovePositionTask")
public class ListRemovePositionTask extends AbstractTask {
  
  public static final String                REMOVE_FROM_INDEX   = "REMOVE_FROM_INDEX";
  public static final String                REMOVE_FROM_LIST    = "REMOVE_FROM_LIST";
  public static final String                REMOVED_RESULT_LIST = "REMOVED_RESULT_LIST";
  public static final String                EXECUTION_STATUS    = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST          = Arrays.asList(REMOVE_FROM_LIST, REMOVE_FROM_INDEX);
  public static final List<String>          OUTPUT_LIST         = Arrays.asList(REMOVED_RESULT_LIST, EXECUTION_STATUS);
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
    List<String> returnList = new ArrayList<String>();
    // validation required as REMOVE_POSITION as it is Mandate param
    String inputPos = (String) inputFields.get(REMOVE_FROM_INDEX);
    if (inputPos.isBlank()) {
      returnList.add(REMOVE_FROM_INDEX);
    }
    
    return returnList;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    List<Object> modifiedList = Collections.EMPTY_LIST;
    
    String removePosition = DiValidationUtil.validateAndGetRequiredString(model, REMOVE_FROM_INDEX);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    List<Object> inputList = (List<Object>) DiValidationUtil.validateAndGetOptionalCollection(model, REMOVE_FROM_LIST);
    // REMOVE_FROM_LIST is optional
    if (inputList == null) {
      inputList = Collections.EMPTY_LIST;
    }
    int index = validateInputPosition(model, removePosition, inputList);
    
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    modifiedList = inputList.stream().collect(Collectors.toList());
    modifiedList.remove(index);
    
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ListRemovePositionTask.class.getName(), model.getTaskId() });
    model.getOutputParameters().put(REMOVED_RESULT_LIST, modifiedList);
    model.getOutputParameters().put(EXECUTION_STATUS, model.getExecutionStatusTable());
  }
  
  /**
   * parse String to number If parsing failed then update error message in model
   * 
   * @param model
   * @param index
   * @param inputPosition
   * @return int
   */
  private int validateInputPosition(WorkflowTaskModel model, Object inputPos, List<Object> inputList)
  {
    int index = -1;
    try {
      index = Integer.parseInt((String) inputPos) - 1;
      // validating index
      if (!(0 <= index && index <= inputList.size())) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
            new String[] { REMOVE_FROM_INDEX });
      }
    }
    catch (NumberFormatException e) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { REMOVE_FROM_INDEX });
    }
    return index;
  }
}
