package com.cs.core.runtime.interactor.model.configuration;

public interface IIdAndContextModel extends IModel {
  
  public static String ID                  = "id";
  public static String CONTEXT_INSTANCE_ID = "contextInstanceId";
  public static String CONTEXT_ID          = "contextId";
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getId();
  
  public void setId(String id);
  
  public String getContextInstanceId();
  
  public void setContextInstanceId(String contextInstanceId);
}
