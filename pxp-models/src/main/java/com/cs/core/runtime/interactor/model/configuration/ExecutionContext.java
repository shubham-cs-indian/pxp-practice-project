package com.cs.core.runtime.interactor.model.configuration;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExecutionContext {
  
  protected Map<String, Object> executionData = new HashMap<>();
  
  public Map<String, Object> getExecutionData()
  {
    return executionData;
  }
  
  public void setExecutionData(Map<String, Object> executionData)
  {
    this.executionData = executionData;
  }
  
  public void putExecutionData(String key, Object data)
  {
    this.executionData.put(key, data);
  }
  
  public Object getExecutionData(String key)
  {
    return this.executionData.get(key);
  }
}
