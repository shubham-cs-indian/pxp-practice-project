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
 *  This task performs list clear operation
 * @author mangesh.metkari
 *
 */
@Component("listClearTask")
public class ListClearTask extends AbstractTask {
  
  public static final String             LIST_TO_CLEAR     = "LIST_TO_CLEAR";
  
  public static final List<String>       INPUT_LIST        = Arrays.asList(LIST_TO_CLEAR);
  public static final List<String>       OUTPUT_LIST       = Arrays.asList( EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES    = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES       = Arrays.asList(EventType.BUSINESS_PROCESS);
  
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
    String listToClear = (String) inputFields.get(LIST_TO_CLEAR);
    
    if (listToClear == null) {
      returnList.add(LIST_TO_CLEAR);
    }
    return returnList;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    // validating LIST_TO_CLEAR
    Collection<?> listToClear = DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(model, LIST_TO_CLEAR);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    if (listToClear.isEmpty()) {
      model.getExecutionStatusTable()
          .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { LIST_TO_CLEAR });
    }
    // performing the list append operation
    //clearing given list
    listToClear.clear();
    // updating the result in model
    model.getExecutionStatusTable()
        .addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID },
            MessageCode.GEN035, new String[] { ListClearTask.class.getName(), model.getTaskId() });
  }
  
}
