package com.cs.core.runtime.interactor.model.camunda;

public class GetCamundaProcessDefinationModel implements IGetCamundaProcessDefinationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          processDefinationId;
  protected String          processInstanceId;
  
  public GetCamundaProcessDefinationModel()
  {
  }
  
  public GetCamundaProcessDefinationModel(String processDefinationId, String processInstanceId)
  {
    this.processDefinationId = processDefinationId;
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public String getProcessDefinationId()
  {
    return processDefinationId;
  }
  
  @Override
  public void setProcessDefinationId(String processDefinationId)
  {
    this.processDefinationId = processDefinationId;
  }
  
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
