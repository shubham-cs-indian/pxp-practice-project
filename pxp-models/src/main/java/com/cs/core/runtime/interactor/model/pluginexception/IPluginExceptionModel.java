package com.cs.core.runtime.interactor.model.pluginexception;

public interface IPluginExceptionModel extends IExceptionModel {
  
  public static final String EXCEPTION_CLASS = "exceptionClass";
  
  public String getExceptionClass();
  
  public void setExceptionClass(String exceptionClass);
}
