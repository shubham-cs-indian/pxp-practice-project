package com.cs.core.runtime.interactor.model.camunda;

public class CamundaProcessInstanceModel implements ICamundaProcessInstanceModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          processInstanceId;
  
  @Override
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
}
