package com.cs.core.config.interactor.model.processdetails;

public class ProcessModel implements IProcessModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          componentId;
  protected String          processInstanceId;
  
  public String getComponentId()
  {
    return componentId;
  }
  
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
}
