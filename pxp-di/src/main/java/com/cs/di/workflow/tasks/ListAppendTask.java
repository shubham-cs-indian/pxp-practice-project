package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 * This task performs list append operation by appending new item in the given
 * list
 * 
 * @author mangesh.metkari
 *
 */
@Component("listAppendTask")
public class ListAppendTask extends AbstractTask {
  
  public static final String                LIST_TO_WHICH_APPEND  = "LIST_TO_WHICH_APPEND";
  public static final String                APPEND_ITEM          = "APPEND_ITEM";
  public static final String                APPEND_RESULT_LIST = "APPENDED_RESULT_LIST";
  
  public static final List<String>          INPUT_LIST           = Arrays.asList(LIST_TO_WHICH_APPEND, APPEND_ITEM);
  public static final List<String>          OUTPUT_LIST          = Arrays.asList(APPEND_RESULT_LIST, EXECUTION_STATUS);
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
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String appendItem = (String) inputFields.get(APPEND_ITEM);
    String listToWhichAppend = (String) inputFields.get(LIST_TO_WHICH_APPEND);
    
    if (DiValidationUtil.isBlank(appendItem)) {
      returnList.add(APPEND_ITEM);
    }
    
    if (listToWhichAppend == null) {
      returnList.add(LIST_TO_WHICH_APPEND);
    }
    return returnList; 
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    // validating LIST_TOWHICH_APPEND
    Collection<?> listToWhichAppend = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TO_WHICH_APPEND);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    // getting APPEND_ITEM from inputParameters
    Object listItem = model.getInputParameters().get(APPEND_ITEM);
    if (listItem == null) {
      model.getExecutionStatusTable()
          .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { APPEND_ITEM });
    }
    //
    if (listToWhichAppend.isEmpty()) {
      model.getExecutionStatusTable()
          .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,new String[] { LIST_TO_WHICH_APPEND });
    }
    // performing the list append operation
    List<Object> outputListAppend = listToWhichAppend.stream().collect(Collectors.toList());
    outputListAppend.add(listItem);
    // updating the result in model
    model.getOutputParameters().put(APPEND_RESULT_LIST, outputListAppend);
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID },
            MessageCode.GEN035, new String[] { ListExtendTask.class.getName(), model.getTaskId() });
  }  
}
