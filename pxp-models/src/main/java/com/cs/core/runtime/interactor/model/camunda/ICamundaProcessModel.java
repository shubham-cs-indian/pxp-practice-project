package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICamundaProcessModel extends IModel {
  
  public static final String PROCESS_DEFINATION_ID  = "processDefinationId";
  public static final String PROCESS_DEFINATION     = "processDefination";
  public static final String CURRENT_ACTIVITY_IDS   = "currentActivityIds";
  public static final String COMPLETED_ACTIVITY_IDS = "completedActivityIds";
  
  public String getProcessDefinationId();
  
  public void setProcessDefinationId(String processDefinationId);
  
  public String getProcessDefination();
  
  public void setProcessDefination(String processDefination);
  
  public List<String> getCurrentActivityIds();
  
  public void setCurrentActivityIds(List<String> currentActivityIds);
  
  public List<String> getCompletedActivityIds();
  
  public void setCompletedActivityIds(List<String> completedActivityIds);
}
