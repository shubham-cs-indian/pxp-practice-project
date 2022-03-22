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

@Component("listRemoveFirstOccurrenceTask")
public class ListRemoveFirstOccurrenceTask extends AbstractTask {
  
  public static final String                REMOVE_ITEM         = "REMOVE_ITEM";
  public static final String                REMOVE_FROM_LIST    = "REMOVE_FROM_LIST";
  public static final String                REMOVED_RESULT_LIST = "REMOVED_RESULT_LIST";
  public static final String                EXECUTION_STATUS    = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST          = Arrays.asList(REMOVE_FROM_LIST, REMOVE_ITEM);
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
    // validation required as REMOVE_ITEM as it is Mandate param
    String inputPos = (String) inputFields.get(REMOVE_ITEM);
    if (inputPos.isBlank()) {
      returnList.add(REMOVE_ITEM);
    }
    
    return returnList;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    List<Object> modifiedList = Collections.EMPTY_LIST;
    List<Object> inputList = (List<Object>) DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, REMOVE_FROM_LIST);
    
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    if (inputList.isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { REMOVE_FROM_LIST });
    }
    
    Object removeItem = DiValidationUtil.validateAndGetOptionalString(model, REMOVE_ITEM);
    
    modifiedList = inputList.stream().collect(Collectors.toList());
    Boolean output = modifiedList.remove(removeItem);
    if (!output) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { REMOVE_ITEM });
    }
    
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ListRemoveFirstOccurrenceTask.class.getName(), model.getTaskId() });
    model.getOutputParameters().put(REMOVED_RESULT_LIST, modifiedList);
    model.getOutputParameters().put(EXECUTION_STATUS, model.getExecutionStatusTable());
  }
}
