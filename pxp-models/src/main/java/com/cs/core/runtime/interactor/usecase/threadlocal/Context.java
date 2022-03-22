package com.cs.core.runtime.interactor.usecase.threadlocal;

public class Context {
  
  private String requestId;
  
  private String sessionId;
  
  private Long   startTime;
  
  private Long   endTime;
  
  public String getRequestId()
  {
    return requestId;
  }
  
  public void setRequestId(String requestId)
  {
    this.requestId = requestId;
  }
  
  public String getSessionId()
  {
    return sessionId;
  }
  
  public void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
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
}
