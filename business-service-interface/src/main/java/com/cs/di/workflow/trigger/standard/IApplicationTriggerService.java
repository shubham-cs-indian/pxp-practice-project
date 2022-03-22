package com.cs.di.workflow.trigger.standard;

public interface IApplicationTriggerService extends IWorkflowEventTriggerService<IApplicationTriggerModel> {
  public static final String SERVICE              = "SERVICE";
  public static final String PRIORITY             = "PRIORITY";
  public static final String SERVICE_DATA         = "SERVICE_DATA";
}
