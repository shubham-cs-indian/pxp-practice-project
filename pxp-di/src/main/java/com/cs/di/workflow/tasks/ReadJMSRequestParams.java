package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author mangesh.metkari
 */
@Component("readJMSRequestParams")
public class ReadJMSRequestParams extends AbstractTask {
  public static final String                RECEIVED_DATA       = "RECEIVED_DATA";
  public static final String                EXECUTION_STATUS    = "EXECUTION_STATUS";
  public static final List<String>          INPUT_LIST          = Arrays.asList(RECEIVED_DATA);
  public static final List<String>          OUTPUT_LIST         = Arrays.asList(EXECUTION_STATUS);
  protected static final List<WorkflowType> WORKFLOW_TYPES      = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  protected static final List<EventType>    EVENT_TYPES         = Arrays.asList(EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  public static final String                REQUEST_PARAMS      = "requestParams";

  @Override
  public void executeTask(WorkflowTaskModel workflowTaskModel) {

  }

  @SuppressWarnings("unchecked")
  @Override
  protected void setOutputParameters(WorkflowTaskModel taskModel, Object execution,
                                     IAbstractExecutionReader reader)
  {
    Object param = taskModel.getInputParameters().get(RECEIVED_DATA);
    Map<String, Object> requestMap = null;
    if (param instanceof Map<?,?>) {
      requestMap = (Map<String, Object> ) DiValidationUtil.validateAndGetOptionalMap(taskModel, RECEIVED_DATA);
      for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
        try {
          Map<String, Object> jsonFileData = (entry.getValue() instanceof Map) ? (Map<String, Object>) entry.getValue() :
                  ObjectMapperUtil.readValue(entry.getValue().toString(), new TypeReference<Map<String, Object>>() {
                  });
          Map<String, Object> requestParams = (Map<String, Object>) jsonFileData.get("requestParams");
          if(requestParams!=null) {
            requestParams.forEach((key, val)-> {
              reader.setVariable(execution, key, val);
            });
          }
        } catch (Exception e) {
          taskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009, new String[] { entry.getKey() });
        }
        break;
      }
    }
    else if (param instanceof String) {
      String requestParams = param.toString();
      try {
        requestMap = new ObjectMapper().readValue(requestParams, Map.class);
        Map<String, Object> requestParameters = (Map<String, Object>) requestMap.get("requestParams");
        if(requestParameters!=null) {
          requestParameters.forEach((key, val)-> {
            reader.setVariable(execution, key, val);
          });
        }
      }
      catch (JsonProcessingException e) {
        taskModel.getExecutionStatusTable()
            .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { RECEIVED_DATA });
      }catch (Exception e) {
        taskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009, new String[] { "requestParams" });
      }
    }
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

  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    if (DiValidationUtil.isBlank((String) inputFields.get(RECEIVED_DATA))) {
      returnList.add(RECEIVED_DATA);
    }
    return returnList;
  }
}
