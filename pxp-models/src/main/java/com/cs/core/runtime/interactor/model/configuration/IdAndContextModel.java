package com.cs.core.runtime.interactor.model.configuration;

public class IdAndContextModel implements IIdAndContextModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          contextInstanceId;
  protected String          contextId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getContextInstanceId()
  {
    return contextInstanceId;
  }
  
  @Override
  public void setContextInstanceId(String contextInstanceId)
  {
    this.contextInstanceId = contextInstanceId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
}
