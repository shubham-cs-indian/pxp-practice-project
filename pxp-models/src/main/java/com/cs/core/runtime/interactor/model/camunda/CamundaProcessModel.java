package com.cs.core.runtime.interactor.model.camunda;

import java.util.ArrayList;
import java.util.List;

public class CamundaProcessModel implements ICamundaProcessModel {
  
  private static final long serialVersionUID     = 1L;
  protected String          processDefinationId;
  protected String          processDefination;
  protected List<String>    currentActivityIds   = new ArrayList<>();
  protected List<String>    completedActivityIds = new ArrayList<>();
  
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
  
  @Override
  public String getProcessDefination()
  {
    return processDefination;
  }
  
  @Override
  public void setProcessDefination(String processDefination)
  {
    this.processDefination = processDefination;
  }
  
  @Override
  public List<String> getCurrentActivityIds()
  {
    return currentActivityIds;
  }
  
  @Override
  public void setCurrentActivityIds(List<String> currentActivityIds)
  {
    this.currentActivityIds = currentActivityIds;
  }
  
  @Override
  public List<String> getCompletedActivityIds()
  {
    return completedActivityIds;
  }
  
  @Override
  public void setCompletedActivityIds(List<String> completedActivityIds)
  {
    this.completedActivityIds = completedActivityIds;
  }
}
