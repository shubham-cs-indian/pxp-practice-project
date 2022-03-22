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
/**
 * This component modifies existing list adding item at particular index
 * @author mayuri.wankhade
 *
 */
@Component("listInsertPositionTask")
public class ListInsertPositionTask extends AbstractTask {
  
  public static final String                INSERT_ITEM        = "INSERT_ITEM";
  public static final String                INSERT_AT_INDEX    = "INSERT_AT_INDEX";
  public static final String                INSERT_INTO_LIST   = "INSERT_INTO_LIST";
  public static final String                INSERT_RESULT_LIST = "INSERT_RESULT_LIST";
  public static final String                EXECUTION_STATUS   = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST         = Arrays.asList(INSERT_ITEM, INSERT_AT_INDEX, INSERT_INTO_LIST);
  public static final List<String>          OUTPUT_LIST        = Arrays.asList(INSERT_RESULT_LIST, EXECUTION_STATUS);
  protected static final List<WorkflowType> WORKFLOW_TYPES     = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  protected static final List<EventType>    EVENT_TYPES        = Arrays.asList(EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  
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
  @SuppressWarnings("unchecked")
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    // validation required as INPUT_POSITION as it is Mandate param
    String inputPos = (String) inputFields.get(INSERT_AT_INDEX);
    if (inputPos.isBlank()) {
      returnList.add(INSERT_AT_INDEX);
    }
    // No validation required as INPUT_ITEM is optional
    // No validation required as INPUT_LIST_POS is optional
    // No validation required for OUTPUT_LIST
    return returnList;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    List<Object> inputPosList = null;
    String inputItem = null;
    List<Object> modifiedList = Collections.EMPTY_LIST;
    int index = -1;
    String inputPos = DiValidationUtil.validateAndGetRequiredString(model, INSERT_AT_INDEX);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    else {
      inputItem = DiValidationUtil.validateAndGetOptionalString(model, INSERT_ITEM);
      inputPosList = (List<Object>) DiValidationUtil.validateAndGetOptionalCollection(model, INSERT_INTO_LIST);
      // INPUT_LIST_POS is optional
      if (inputPosList==null) {
        inputPosList = Collections.EMPTY_LIST;
      }
      index = validateInputPosition(model, inputPos, inputPosList);
    }
    
    if (!model.getExecutionStatusTable().isErrorOccurred()) {
      // updating the result in model
      modifiedList = inputPosList.stream().collect(Collectors.toList());
      modifiedList.add(index, inputItem);
      model.getOutputParameters().put(INSERT_RESULT_LIST, modifiedList);
      model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID },
          MessageCode.GEN035, new String[] { ListInsertPositionTask.class.getName(), model.getTaskId() });
    }
    else {
      return;
    }
  }
  
  /**
   * parse String to number
   * If parsing failed then update error message in model
   * @param model
   * @param index
   * @param inputPosition
   * @return int
   */
  private int validateInputPosition(WorkflowTaskModel model, Object inputPos, List<Object> inputPosList)
  {
    int index = -1;
    try {
      index = Integer.parseInt((String) inputPos) - 1;
     // validating index
      if (!(0 <= index && index <= inputPosList.size())) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
            new String[] { INSERT_AT_INDEX });
      }
    }
    catch (NumberFormatException e) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { INSERT_AT_INDEX });
    }
    return index;
  }
}
