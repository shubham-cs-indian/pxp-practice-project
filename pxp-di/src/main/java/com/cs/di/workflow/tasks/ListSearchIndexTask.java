package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
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
 * This component helps to get the index of given item to search
 * from  the list provided
 * @author mayuri.wankhade
 *
 */
@Component("listSearchIndexTask")
public class ListSearchIndexTask extends AbstractTask {
  
  public static final String                SEARCH_ITEM         = "SEARCH_ITEM";
  public static final String                SEARCH_IN_LIST      = "SEARCH_IN_LIST";
  public static final String                SEARCH_RESULT_INDEX = "SEARCH_RESULT_INDEX";
  public static final String                EXECUTION_STATUS    = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST          = Arrays.asList(SEARCH_ITEM, SEARCH_IN_LIST);
  public static final List<String>          OUTPUT_LIST         = Arrays.asList(SEARCH_RESULT_INDEX, EXECUTION_STATUS);
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
    // No validation required for INPUT_ITEM AND INPUT_LIST_SEARCH as it is optional
    // No validation required for OUTPUT_LIST
    return null;
  }

  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    int searchIndex = 0;
    // getting item to search OPTIONAL PARAM
    String searchItem = DiValidationUtil.validateAndGetOptionalString(model, SEARCH_ITEM);
    // validating Input_list_search OPTIONAL PARAM
    List<Object> inputList = (List<Object>) DiValidationUtil.validateAndGetOptionalCollection(model, SEARCH_IN_LIST);
    if (inputList == null) {
      inputList = new ArrayList<>();
    }
    // Returns the index of the first occurrence of the specified element in
    // this list, or 0
    searchIndex = inputList.indexOf(searchItem) + 1;
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ListSearchIndexTask.class.getName(), model.getTaskId() });
    model.getOutputParameters().put(SEARCH_RESULT_INDEX, searchIndex);
  }
  }

