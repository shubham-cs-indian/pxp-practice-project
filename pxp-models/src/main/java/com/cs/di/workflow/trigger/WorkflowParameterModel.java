package com.cs.di.workflow.trigger;

import java.util.HashMap;
import java.util.Map;

public class WorkflowParameterModel implements IWorkflowParameterModel{
  private static final long serialVersionUID = 1L;
  protected Map<String, Object> workflowParameterMap = new HashMap<>();

  @Override
  public Map<String, Object> getWorkflowParameterMap()
  {
    return workflowParameterMap;
  }

  @Override
  public void setWorkflowParameterMap(Map<String, Object> workflowParameterMap)
  {
    this.workflowParameterMap = workflowParameterMap;
  }
}
