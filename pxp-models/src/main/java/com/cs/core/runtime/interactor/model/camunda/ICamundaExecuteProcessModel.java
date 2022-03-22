package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ICamundaExecuteProcessModel extends IModel {
  
  public static final String EVENT_PARAMETERS      = "eventParameters";
  public static final String KLASS_INSTANCE_IS     = "klassInstanceId";
  public static final String PROCESS_DEFINITION_ID = "processDefinationId";
  
  public Map<String, Object> getEventParameters();
  
  public void setEventParameters(Map<String, Object> eventParameters);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getProcessDefinationId();
  
  public void setProcessDefinationId(String processDefinationId);
}
