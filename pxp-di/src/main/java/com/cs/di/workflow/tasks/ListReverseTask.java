package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

/**
 * This class performs the Reverse operation for given list
 * and returns the reversed list
 * @author Subham.Shaw
 */
@Component("listReverseTask")
public class ListReverseTask extends AbstractTask {
  
  public static final String                LIST_TO_REVERSE      = "LIST_TO_REVERSE";
  public static final String                RESULT_REVERSED_LIST = "RESULT_REVERSED_LIST";
  public static final String                EXECUTION_STATUS     = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST           = Arrays.asList(LIST_TO_REVERSE);
  public static final List<String>          OUTPUT_LIST          = Arrays.asList(RESULT_REVERSED_LIST, EXECUTION_STATUS);
  protected static final List<WorkflowType> WORKFLOW_TYPES       = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  protected static final List<EventType>    EVENT_TYPES          = Arrays.asList(EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  
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
    String listToReverse = (String) inputFields.get(LIST_TO_REVERSE);
    if (listToReverse == null) {
      returnList.add(LIST_TO_REVERSE);
    }
    // No validation required for OUTPUT_LIST
    return returnList;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    // validating and fetching input_list
    Collection<?> listToReverse = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TO_REVERSE);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    if (listToReverse.isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { LIST_TO_REVERSE });
    }
    // performing the list reverse operation
    List<Object> outputListReversed = listToReverse.stream().collect(Collectors.toList());
    Collections.reverse(outputListReversed);
    
    // updating the result in model
    model.getOutputParameters().put(RESULT_REVERSED_LIST, outputListReversed);
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ListReverseTask.class.getName(), model.getTaskId() });
  }
}