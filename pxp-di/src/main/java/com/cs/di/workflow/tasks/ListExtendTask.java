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
 * This component performs list extend operation by appeding new list in exisitng list  
 * @author mayuri.wankhade
 *
 */
@Component("listExtendTask")
public class ListExtendTask extends AbstractTask {
  
  public static final String                LIST_TOWHICH_APPEND  = "LIST_TOWHICH_APPEND";
  public static final String                LIST_TOAPPENDED      = "LIST_TOAPPENDED";
  public static final String                APPENDED_RESULT_LIST = "APPENDED_RESULT_LIST";
  public static final String                EXECUTION_STATUS     = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST           = Arrays.asList(LIST_TOWHICH_APPEND, LIST_TOAPPENDED);
  public static final List<String>          OUTPUT_LIST          = Arrays.asList(APPENDED_RESULT_LIST, EXECUTION_STATUS);
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
  @SuppressWarnings("unchecked")
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String listToWhichAppend = (String) inputFields.get(LIST_TOWHICH_APPEND);
    if (listToWhichAppend == null) {
      returnList.add(LIST_TOWHICH_APPEND);
    }
    String listToAppend = (String) inputFields.get(LIST_TOAPPENDED);
    if (listToAppend == null) {
      returnList.add(LIST_TOAPPENDED);
    }
    // No validation required for OUTPUT_LIST
    return returnList;
  }

  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    Collection<?> listToWhichAppend = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TOWHICH_APPEND);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    Collection<?> listToAppend = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TOAPPENDED);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    if (listToWhichAppend.isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { LIST_TOWHICH_APPEND });
    }
    if (listToAppend.isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { LIST_TOAPPENDED });
    }
    // performing the list extend operation
    List<Object> outputListAppend = listToWhichAppend.stream().collect(Collectors.toList());
    outputListAppend.addAll(listToAppend);
    // updating the result in model
    model.getOutputParameters().put(APPENDED_RESULT_LIST, outputListAppend);
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { ListExtendTask.class.getName(), model.getTaskId() });
  }
}
