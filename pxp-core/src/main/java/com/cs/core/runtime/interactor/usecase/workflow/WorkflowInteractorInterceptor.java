package com.cs.core.runtime.interactor.usecase.workflow;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

// @Component
// @Aspect
public class WorkflowInteractorInterceptor {
  
  @Around("execution(public * com.cs.runtime.interactor.usecase.base.IRuntimeInteractor.execute(..))")
  public Object aroundInteractor(ProceedingJoinPoint pjp) throws Throwable
  {
    try {
      String functionName = pjp.getSignature()
          .getName();
      String className = pjp.getTarget()
          .getClass()
          .getSimpleName();
      System.out.println("Class : " + className + " - Function : " + functionName);
      Object response = pjp.proceed();
      return response;
    }
    catch (Throwable e) {
      throw e;
    }
  }
}
