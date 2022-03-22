package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICamundaProcessInstanceModel extends IModel {
  
  public static final String PROCESS_INSTANCE_ID = "processInstanceId";
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
}
