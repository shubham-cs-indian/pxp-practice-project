package com.cs.core.runtime.interactor.model.logger;

import org.springframework.stereotype.Component;

@Component
public class ComponentData {
  
  protected String klassName;
  protected Long   startTime;
  protected Long   endTime;
  protected Long   turnAroundTime;
  
  public String getKlassName()
  {
    return klassName;
  }
  
  public void setKlassName(String klassName)
  {
    this.klassName = klassName;
  }
  
  public Long getStartTime()
  {
    return startTime;
  }
  
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  public Long getEndTime()
  {
    return endTime;
  }
  
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }
  
  public Long getTurnAroundTime()
  {
    return turnAroundTime;
  }
  
  public void setTurnAroundTime(Long turnAroundTime)
  {
    this.turnAroundTime = turnAroundTime;
  }
}
