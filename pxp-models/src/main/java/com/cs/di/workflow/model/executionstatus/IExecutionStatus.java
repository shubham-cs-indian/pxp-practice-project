package com.cs.di.workflow.model.executionstatus;

import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.constants.ObjectCode;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IExecutionStatus<T extends IOutputExecutionStatusModel> extends Serializable {
  
  public void addSummary(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params);
  
  public void addError(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params);
  
  public void addSuccess(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params);
  
  public void addInformation(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params);
  
  public void addWarning(ObjectCode[] objectCodes, MessageCode statusMessage, String[] params);
  
  public void addSummary(MessageCode statusMessage);
  
  public void addError(MessageCode statusMessage);
  
  public void addSuccess(MessageCode statusMessage);
  
  public void addInformation(MessageCode statusMessage);
  
  public void addWarning(MessageCode statusMessage);
  
  public Map<MessageType, List<T>> getExecutionStatusTableMap();
  public void setExecutionStatusTableMap(Map<MessageType, List<T>> executionStatusTable);
  
  @Transient
  public boolean isErrorOccurred();
  
  public void merge(IExecutionStatus<T> toBeMerged);
  
  public void mergeAll(List<IExecutionStatus<T>> toBeMergedList);
}
