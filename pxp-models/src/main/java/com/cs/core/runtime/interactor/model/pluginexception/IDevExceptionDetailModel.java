package com.cs.core.runtime.interactor.model.pluginexception;

public interface IDevExceptionDetailModel extends IExceptionDetailModel {
  
  public static final String STACK           = "stack";
  public static final String EXCEPTION_CLASS = "exceptionClass";
  
  public String getExceptionClass();
  
  public void setExceptionClass(String exceptionClass);
  
  public StackTraceElement[] getStack();
  
  public void setStack(StackTraceElement[] Stack);
  
  public String getDetailMessage();
  
  public void setDetailMessage(String detailMessage);
}
