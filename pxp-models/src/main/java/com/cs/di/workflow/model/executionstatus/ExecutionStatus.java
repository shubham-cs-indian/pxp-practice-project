package com.cs.di.workflow.model.executionstatus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.constants.ObjectCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class manages four types of execution status that contain respective
 * message codes for all the records processed during execution of a single
 * workflow component
 */
public class ExecutionStatus implements IExecutionStatus<OutputExecutionStatusModel> {
  
  private Map<MessageType, List<OutputExecutionStatusModel>> executionStatusTable;
  
  @Override
  public Map<MessageType, List<OutputExecutionStatusModel>> getExecutionStatusTableMap()
  {
    return executionStatusTable;
  }
  
  @Override
  public void setExecutionStatusTableMap(Map<MessageType, List<OutputExecutionStatusModel>> executionStatusTable)
  {
    this.executionStatusTable = executionStatusTable;
  }
  
  public ExecutionStatus()
  {
    executionStatusTable = new EnumMap<>(MessageType.class);
    executionStatusTable.put(MessageType.SUMMARY, new ArrayList<OutputExecutionStatusModel>());
    executionStatusTable.put(MessageType.ERROR, new ArrayList<OutputExecutionStatusModel>());
    executionStatusTable.put(MessageType.SUCCESS, new ArrayList<OutputExecutionStatusModel>());
    executionStatusTable.put(MessageType.INFORMATION, new ArrayList<OutputExecutionStatusModel>());
    executionStatusTable.put(MessageType.WARNING, new ArrayList<OutputExecutionStatusModel>());
  }
  
  public void addError(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params)
  {
    addStatus(objectCodes, statusMessage, MessageType.ERROR, params);
  }
  
  public void addSuccess(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params)
  {
    addStatus(objectCodes, statusMessage, MessageType.SUCCESS, params);
  }
  
  public void addInformation(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params)
  {
    addStatus(objectCodes, statusMessage, MessageType.INFORMATION, params);
  }
  
  public void addWarning(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params)
  {
    addStatus(objectCodes, statusMessage, MessageType.WARNING, params);
  }
  
  public void addSummary(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params)
  {
    addStatus(objectCodes, statusMessage, MessageType.SUMMARY, params);
  }

  public void addError(MessageCode statusMessage)
  {
    addStatus(null, statusMessage, MessageType.ERROR, null);
  }
  
  public void addSuccess(MessageCode statusMessage)
  {
    addStatus(null, statusMessage, MessageType.SUCCESS, null);
  }
  
  public void addInformation(MessageCode statusMessage)
  {
    addStatus(null, statusMessage, MessageType.INFORMATION, null);
  }
  
  public void addWarning(MessageCode statusMessage)
  {
    addStatus(null, statusMessage, MessageType.WARNING, null);
  }
  
  public void addSummary(MessageCode statusMessage)
  {
    addStatus(null, statusMessage, MessageType.SUMMARY, null);
  }
  
  private IOutputExecutionStatusModel addStatus(ObjectCode[] objectCodes, MessageCode statusMessage,
      MessageType messageType, String[] params)
  {
    OutputExecutionStatusModel status = new OutputExecutionStatusModel();
    status.setObjectCodes(objectCodes);
    status.setMessageCode(statusMessage);
    status.setObjectValues(params);
    status.setMessageType(messageType.getName());
    executionStatusTable.get(messageType).add(status);
    return status;
  }
  
  @Override
  @JsonIgnore
  public boolean isErrorOccurred()
  {
    return !executionStatusTable.get(MessageType.ERROR).isEmpty();
  }
  
  
  @Override
  public void mergeAll(List<IExecutionStatus<OutputExecutionStatusModel>> toBeMergedList)
  {
    toBeMergedList.forEach(statusTable->this.merge(statusTable));
  }

  @Override
  public void merge(IExecutionStatus<OutputExecutionStatusModel> toBeMerged)
  {
    if(toBeMerged==null) {
      return;
    } 
    // k : Message Type and v : Execution Status i.e value of (Message Type,
    // Message Code, Object Codes, Message Text).
    ((ExecutionStatus)toBeMerged).executionStatusTable.forEach((messageType, executionStatus) -> {
      this.executionStatusTable.get(messageType)
          .addAll(executionStatus);
    });
  }
}
