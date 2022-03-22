package com.cs.core.runtime.interactor.usecase.threadlocal;

import org.springframework.stereotype.Component;

@Component
public class ApplicationThreadLocal {
  
  private final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();
  
  public void setValue(Context value)
  {
    threadLocal.set(value);
  }
  
  public Context getValue()
  {
    return threadLocal.get();
  }
  
  public void unset()
  {
    threadLocal.remove();
  }
}
