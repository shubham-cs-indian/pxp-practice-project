package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

/**
 * This task counts the number of occurrence of given element 
 * @author mangesh.metkari
 *
 */
@Component("listCountElementTask")
public class ListCountElementTask extends AbstractTask {
  
  public static final String             LIST_TO_COUNT_ELEMENT   = "LIST_TO_COUNT_ELEMENT";
  
  public static final String             RESULT_LISTCOUNTELEMENT = "RESULT_LISTCOUNTELEMENT";
  public static final String             COUNT_ITEM             = "COUNT_ITEM";
  
  public static final List<String>       INPUT_LIST              = Arrays.asList(LIST_TO_COUNT_ELEMENT, COUNT_ITEM);
  public static final List<String>       OUTPUT_LIST             = Arrays.asList(RESULT_LISTCOUNTELEMENT, EXECUTION_STATUS);
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
    String countItem = (String) inputFields.get(COUNT_ITEM);
    String listToCountElement = (String) inputFields.get(LIST_TO_COUNT_ELEMENT);
    if (DiValidationUtil.isBlank(countItem)) {
      returnList.add(COUNT_ITEM);
    }
    if (listToCountElement == null) {
      returnList.add(LIST_TO_COUNT_ELEMENT);
    }
    return returnList;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    // validating LIST_TO_COUNT_ELEMENT
    Collection<?> listToCountElement = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TO_COUNT_ELEMENT);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    // getting COUNT_ITEM from inputParameters
    Object countItem = model.getInputParameters().get(COUNT_ITEM);
    if (countItem == null) {
      model.getExecutionStatusTable()
          .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { COUNT_ITEM });
    }
    if (listToCountElement.isEmpty()) {
      model.getExecutionStatusTable()
          .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { LIST_TO_COUNT_ELEMENT });
    }
    // performing the list element count operation
    Long elementCount = listToCountElement.stream().filter(str -> str.equals(countItem)).count();
    // updating the result in model
    model.getOutputParameters().put(RESULT_LISTCOUNTELEMENT, elementCount);
    model.getExecutionStatusTable()
        .addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID },MessageCode.GEN035,
            new String[] { ListCountElementTask.class.getName(), model.getTaskId() });
  }  
}
