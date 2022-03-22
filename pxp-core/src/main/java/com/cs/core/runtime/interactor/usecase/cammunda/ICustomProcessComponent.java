package com.cs.core.runtime.interactor.usecase.cammunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public interface ICustomProcessComponent extends IProcessComponent, JavaDelegate {
  
  public void executeInternal(DelegateExecution delegateExecution) throws Exception;
  
  @Override
  public default void execute(DelegateExecution delegateExecution) throws Exception
  {
    executeInternal(delegateExecution);
  }
}
