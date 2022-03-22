package com.cs.di.workflow.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.di.workflow.model.executionstatus.ExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;

public class WorkflowModel implements Serializable {
  
  /**
  * 
  */
  private static final long                                       serialVersionUID     = 1L;
  private String rootProcessInstanceId;
  private ITransactionData                                        transactionData;
  private IUserSessionDTO                                         userSessionDto;
  private Map<String, Object>                                     workflowParameterMap = new HashMap<>();
  
  private IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
  
  public WorkflowModel(String rootProcessInstanceId, ITransactionData transactionData, IUserSessionDTO userSessionDto)
  {
    super();
    this.rootProcessInstanceId = rootProcessInstanceId;
    this.transactionData = transactionData;
    this.userSessionDto = userSessionDto;
  }
  
  public String getRootProcessInstanceId()
  {
    return rootProcessInstanceId;
  }
  
  public IExecutionStatus<? extends IOutputExecutionStatusModel> getExecutionStatusTable()
  {
    return executionStatusTable;
  }
  
  public ITransactionData getTransactionData()
  {
    return transactionData;
  }
 
  public IUserSessionDTO getUserSessionDto()
  {
    return userSessionDto;
  }
  
  public void setUserSessionDto(IUserSessionDTO userSessionDto)
  {
    this.userSessionDto = userSessionDto;
  }
  
  public Map<String, Object> getWorkflowParameterMap()
  {
    return workflowParameterMap;
  }

  public void setWorkflowParameterMap(Map<String, Object> workflowParameterMap)
  {
    this.workflowParameterMap = workflowParameterMap;
  }
}
