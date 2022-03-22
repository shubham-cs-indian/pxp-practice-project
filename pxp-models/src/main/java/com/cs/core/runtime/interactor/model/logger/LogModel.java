package com.cs.core.runtime.interactor.model.logger;

public class LogModel {
  
  protected String captureTimeStamp;
  
  protected String description;
  
  protected Object appData;
  
  protected String userScenario;
  
  protected String className;
  
  protected String logType;
  
  protected String mode;
  
  protected String requestId;
  
  protected String sessionId;
  
  protected String requestURI;
  
  protected Long   turnArroundTime;
  
  public String getCaptureTimeStamp()
  {
    return captureTimeStamp;
  }
  
  public void setCaptureTimeStamp(String captureTimeStamp)
  {
    this.captureTimeStamp = captureTimeStamp;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public Object getAppData()
  {
    return appData;
  }
  
  public void setAppData(Object appData)
  {
    this.appData = appData;
  }
  
  public String getUserScenario()
  {
    return userScenario;
  }
  
  public void setUserScenario(String userScenario)
  {
    this.userScenario = userScenario;
  }
  
  public String getClassName()
  {
    return className;
  }
  
  public void setClassName(String className)
  {
    this.className = className;
  }
  
  public String getLogType()
  {
    return logType;
  }
  
  public void setLogType(String logType)
  {
    this.logType = logType;
  }
  
  public String getMode()
  {
    return mode;
  }
  
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  
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
  
  public String getRequestURI()
  {
    return requestURI;
  }
  
  public void setRequestURI(String requestURI)
  {
    this.requestURI = requestURI;
  }
  
  public Long getTurnArroundTime()
  {
    return turnArroundTime;
  }
  
  public void setTurnArroundTime(Long turnArroundTime)
  {
    this.turnArroundTime = turnArroundTime;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"captureTimeStamp\":\"" + captureTimeStamp + "\",");
    strBuilder.append("\"description\":\"" + description + "\",");
    strBuilder.append("\"appData\":" + appData + ",");
    strBuilder.append("\"userScenario\":\"" + userScenario + "\",");
    strBuilder.append("\"className\":\"" + className + "\",");
    strBuilder.append("\"logType\":\"" + logType + "\",");
    strBuilder.append("\"mode\":\"" + mode + "\",");
    strBuilder.append("\"requestId\":\"" + (requestId != null ? requestId : "-") + "\",");
    strBuilder.append("\"sessionId\":\"" + (sessionId != null ? sessionId : "-") + "\",");
    strBuilder.append(
        "\"turnArroundTime\":\"" + (turnArroundTime != null ? turnArroundTime + "ms" : "-") + "\"");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
}
