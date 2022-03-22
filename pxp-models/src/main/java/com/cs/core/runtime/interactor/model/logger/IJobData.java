package com.cs.core.runtime.interactor.model.logger;

public interface IJobData extends ILogData {
  
  public String getUseCase();
  
  public void setUseCase(String useCase);
  
  public String getKlassName();
  
  public void setKlassName(String klassName);
  
  public String getRequestURL();
  
  public void setRequestURL(String requestURL);
  
  public String getResponse();
  
  public void setResponse(String response);
  
  public String getRequest();
  
  public void setRequest(String request);
}
