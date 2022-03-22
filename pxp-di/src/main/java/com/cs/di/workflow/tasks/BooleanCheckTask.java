package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("booleanCheckTask")
public class BooleanCheckTask extends AbstractTask {
  
  // input parameters
  public static final String             BOOLEAN_CONDITION = "BOOLEAN_CONDITION";
  
  // output parameters
  public static final String             BOOLEAN_RESULT    = "BOOLEAN_RESULT";
  
  public static final List<String>       INPUT_LIST        = Arrays.asList(BOOLEAN_CONDITION);
  
  public static final List<String>       OUTPUT_LIST       = Arrays.asList(BOOLEAN_RESULT, EXECUTION_STATUS);
  
  public static final List<WorkflowType> WORKFLOW_TYPES    = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES       = Arrays.asList(EventType.BUSINESS_PROCESS);
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    String booleanCondition = DiValidationUtil.validateAndGetRequiredString(model, BOOLEAN_CONDITION);
    Boolean booleanResult = null;
    try {
      booleanResult = new SpelExpressionParser().parseExpression(booleanCondition).getValue(new StandardEvaluationContext(),
        Boolean.class);
    }catch(Exception e) {
        RDBMSLogger.instance().exception(e);
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { booleanCondition });
    }
    
    model.getExecutionStatusTable().addSuccess(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN035,
        new String[] { BooleanCheckTask.class.getName(), model.getTaskId() });
    
    model.getOutputParameters().put(BOOLEAN_RESULT, booleanResult);
    model.getOutputParameters().put(EXECUTION_STATUS, model.getExecutionStatusTable());
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<>();
    // Validate required BOOLEAN_CONDITION
    String booleanCondition = (String) inputFields.get(BOOLEAN_CONDITION);
    
    if (DiValidationUtil.isBlank(booleanCondition) || !validaBooleanExpression(booleanCondition)) {
      returnList.add(BOOLEAN_CONDITION);
    }
    
    return returnList;
  }
  
  /**
   * @param booleanCondition
   * @return the boolean result depending on the input expression is valid
   *         boolean expression or not.
   */
  private boolean validaBooleanExpression(String booleanCondition)
  {
    try {
      new SpelExpressionParser().parseExpression(booleanCondition).getValue(new StandardEvaluationContext(), Boolean.class);
    }
    catch (Exception e) {
      return false;
    }
    
    return true;
  }
  
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
  
}
