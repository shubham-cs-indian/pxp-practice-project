package com.cs.core.runtime.interactor.model.camunda;

import java.util.HashMap;
import java.util.Map;

public class CamundaExecuteProcessModel implements ICamundaExecuteProcessModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Map<String, Object> eventParameters  = new HashMap<>();
  protected String              klassInstanceId;
  protected String              processDefinationId;
  
  public CamundaExecuteProcessModel(String processDefinationId, String klassInstanceId,
      Map<String, Object> eventParameters)
  {
    this.processDefinationId = processDefinationId;
    this.klassInstanceId = klassInstanceId;
    this.eventParameters = eventParameters;
  }
  
  @Override
  public Map<String, Object> getEventParameters()
  {
    return eventParameters;
  }
  
  @Override
  public void setEventParameters(Map<String, Object> eventParameters)
  {
    this.eventParameters = eventParameters;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
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
}
