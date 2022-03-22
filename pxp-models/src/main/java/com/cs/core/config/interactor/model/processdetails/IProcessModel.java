package com.cs.core.config.interactor.model.processdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IProcessModel extends IModel {
  
  public static final String ID                  = "id";
  public static final String COMPONENT_ID        = "componentId";
  public static final String PROCESS_INSTANCE_ID = "processInstanceId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
}
