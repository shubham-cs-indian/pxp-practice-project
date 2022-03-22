package com.cs.di.config.model.modeler;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IWorkflowTaskRequestModel extends IModel {
  
  public static final String WORKFLOW_TYPE = "workflowType";
  public static final String EVENT_TYPE    = "eventType";
  
  public String getWorkflowType();
  
  public void setWorkflowType(String workflowType);
  
  public String getEventType();
  
  public void setEventType(String eventType);
}
