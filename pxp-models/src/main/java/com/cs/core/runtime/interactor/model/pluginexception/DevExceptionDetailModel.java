package com.cs.core.runtime.interactor.model.pluginexception;

public class DevExceptionDetailModel extends ExceptionDetailModel
    implements IDevExceptionDetailModel {
  
  private static final long     serialVersionUID = 1L;
  protected StackTraceElement[] stack;
  protected String              exceptionClass;
  protected String              detailMessage;
  
  public StackTraceElement[] getStack()
  {
    return stack;
  }
  
  public void setStack(StackTraceElement[] stack)
  {
    this.stack = stack;
  }
  
  public String getExceptionClass()
  {
    return exceptionClass;
  }
  
  public void setExceptionClass(String exceptionClass)
  {
    this.exceptionClass = exceptionClass;
  }
  
  public String getDetailMessage()
  {
    return detailMessage;
  }
  
  public void setDetailMessage(String detailMessage)
  {
    this.detailMessage = detailMessage;
  }
}
