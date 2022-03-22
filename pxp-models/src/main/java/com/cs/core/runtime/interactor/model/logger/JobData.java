package com.cs.core.runtime.interactor.model.logger;

public class JobData implements IJobData {
  
  protected String id;
  protected String useCase;
  protected String klassName;
  protected String type;
  protected Long   startTime;
  protected Long   endTime;
  protected Long   turnAroundTime;
  protected String executionStatus;
  protected String requestURL;
  protected String response;
  protected String request;
  protected String requestMethod;
  protected String parentTransactionId;
  
  public String getParentTransactionId()
  {
    return parentTransactionId;
  }
  
  public void setParentTransactionId(String parentTransactionId)
  {
    this.parentTransactionId = parentTransactionId;
  }
  
  public String getRequestMethod()
  {
    return requestMethod;
  }
  
  public void setRequestMethod(String requestMethod)
  {
    this.requestMethod = requestMethod;
  }
  
  public String getUseCase()
  {
    return useCase;
  }
  
  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }
  
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
    if (endTime != null && startTime != null) {
      return endTime - startTime;
    }
    return null;
  }
  
  public String getExecutionStatus()
  {
    return executionStatus;
  }
  
  public void setExecutionStatus(String executionStatus)
  {
    this.executionStatus = executionStatus;
  }
  
  public String getRequestURL()
  {
    return requestURL;
  }
  
  public void setRequestURL(String requestURL)
  {
    this.requestURL = requestURL;
  }
  
  public String getResponse()
  {
    return response;
  }
  
  public void setResponse(String response)
  {
    this.response = response;
  }
  
  public String getRequest()
  {
    return request;
  }
  
  public void setRequest(String request)
  {
    this.request = request;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getType()
  {
    return "job";
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
