package com.cs.di.workflow.trigger;

import java.util.Map;

import com.cs.di.workflow.IWorkflowModel;

public interface IWorkflowParameterModel extends IWorkflowModel{
  
  String WORKFLOW_PARAMETER_MAP = "workflowParameterMap";
  
  public Map<String, Object> getWorkflowParameterMap();
  
  public void setWorkflowParameterMap(Map<String, Object> workflowParameterMap);
}
