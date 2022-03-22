package com.cs.di.config.model.modeler;

public class WorkflowTaskRequestModel implements IWorkflowTaskRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          workflowType;
  protected String          eventType;
  
  @Override
  public String getWorkflowType()
  {
    return this.workflowType;
  }
  
  @Override
  public void setWorkflowType(String workflowType)
  {
    this.workflowType = workflowType;
  }
  
  @Override
  public String getEventType()
  {
    return this.eventType;
  }
  
  @Override
  public void setEventType(String eventType)
  {
    this.eventType = eventType;
  }
}
