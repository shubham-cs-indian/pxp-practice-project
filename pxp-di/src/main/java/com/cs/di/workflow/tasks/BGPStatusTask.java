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
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
@Component("bgpStatusTask")
public class BGPStatusTask extends AbstractTask {
  
  public static final String             CALL_BACK_BODY_DATA = "callBackBodyData";
  public static final String             SUCCESS_IIDS        = "SUCCESS_IIDS";
  public static final String             FAILED_IIDS         = "FAILED_IIDS";
  
  public static final List<String>       INPUT_LIST          = Arrays.asList(CALL_BACK_BODY_DATA);
  public static final List<String>       OUTPUT_LIST         = Arrays.asList(SUCCESS_IIDS, FAILED_IIDS, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES      = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES         = Arrays.asList(EventType.BUSINESS_PROCESS);
  
  @SuppressWarnings("unchecked")
  @Override
  public void executeTask(WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> processedOrUnprocessedTransferRecoards = (Map<String, Object>) DiValidationUtil
        .validateAndGetOptionalMap(workflowTaskModel, CALL_BACK_BODY_DATA);
    List<Object> successIIDs = (List<Object>) processedOrUnprocessedTransferRecoards.get(SUCCESS_IIDS);
    List<Object> failedIIDs  = (List<Object>) processedOrUnprocessedTransferRecoards.get(FAILED_IIDS);
    
     if (processedOrUnprocessedTransferRecoards != null && !processedOrUnprocessedTransferRecoards.isEmpty()) {
       IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel.getExecutionStatusTable();
        
       executionStatusTable.addInformation(new ObjectCode[] { ObjectCode.TOTAL_COUNT }, MessageCode.GEN042,
          new String[] { String.valueOf(successIIDs.size() + failedIIDs.size())});
      
       executionStatusTable.addInformation(new ObjectCode[] { ObjectCode.SUCCESS_COUNT }, MessageCode.GEN042,
          new String[] { String.valueOf(successIIDs.size())});
       
       executionStatusTable.addInformation(new ObjectCode[] { ObjectCode.SUCCEEDED_IIDS }, MessageCode.GEN042,
           new String[] { String.valueOf(successIIDs) });
      
       executionStatusTable.addInformation(new ObjectCode[] { ObjectCode.FAILED_COUNT }, MessageCode.GEN042,
          new String[] { String.valueOf(failedIIDs.size())});
       
       executionStatusTable.addInformation(new ObjectCode[] { ObjectCode.FAILED_IIDS }, MessageCode.GEN042,
           new String[] { String.valueOf(failedIIDs) }); 
      
      workflowTaskModel.getOutputParameters().put(SUCCESS_IIDS, successIIDs);
      workflowTaskModel.getOutputParameters().put(FAILED_IIDS, failedIIDs);
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
    return TaskType.SERVICE_TASK;

  }

  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    // TODO Auto-generated method stub
    return new ArrayList<String>();
  }

 
}
