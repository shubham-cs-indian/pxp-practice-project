package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetCamundaProcessDefinationModel extends IModel {
  
  public static final String PROCESS_DEFINATION_ID = "processDefinationId";
  public static final String PROCESS_INSTANCE_ID   = "processInstanceId";
  
  public String getProcessDefinationId();
  
  public void setProcessDefinationId(String processDefinationId);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
}
