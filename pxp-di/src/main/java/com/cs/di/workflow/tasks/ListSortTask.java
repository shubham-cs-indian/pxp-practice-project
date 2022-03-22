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
 * This class performs the Sort operation for given list
 * in the specified order and returns the sorted list
 * @author Subham.Shaw
 */
@SuppressWarnings("unchecked")
@Component("listSortTask")
public class ListSortTask extends AbstractTask {
  
  public static final String                LIST_TO_SORT       = "LIST_TO_SORT";
  public static final String                SORT_ORDER         = "SORT_ORDER";
  public static final String                RESULT_SORTED_LIST = "RESULT_SORTED_LIST";
  public static final String                EXECUTION_STATUS   = "EXECUTION_STATUS";
  public static final String                SORTORDER_ASC      = "asc";
  public static final String                SORTORDER_DESC     = "desc";
  public static final List<String>          INPUT_LIST         = Arrays.asList(LIST_TO_SORT, SORT_ORDER);
  public static final List<String>          OUTPUT_LIST        = Arrays.asList(RESULT_SORTED_LIST, EXECUTION_STATUS);
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
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String listToSort = (String) inputFields.get(LIST_TO_SORT);
    if (listToSort == null) {
      returnList.add(LIST_TO_SORT);
    }
    // No validation required for SORT_ORDER as it is UI selected
    // No validation required for OUTPUT_LIST
    return returnList;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {  
    // validating and fetching input_list
    Collection<?> listToSort = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TO_SORT);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    if (listToSort.isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { LIST_TO_SORT });
    }
      
    // validating and fetching sort order 
    String sortOrder = DiValidationUtil.validateAndGetOptionalString(model, SORT_ORDER);
    List<String> outputList = (List<String>) listToSort.stream().collect(Collectors.toList());
    // performing the list sort operation by ascending
    if (sortOrder.equalsIgnoreCase(SORTORDER_ASC)) {
      Collections.sort(outputList);
    }
    // for descending
    else if (sortOrder.equalsIgnoreCase(SORTORDER_DESC)) {
      Collections.sort(outputList, Collections.reverseOrder());
    }
    
    // updating the result in model
    model.getOutputParameters().put(RESULT_SORTED_LIST, outputList);
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ListSortTask.class.getName(), model.getTaskId() });
  }
  
}