package com.cs.di.workflow.trigger.standard;

public interface IBusinessProcessTriggerService extends IWorkflowEventTriggerService<IBusinessProcessTriggerModel> {
    String REQUEST_MODEL = "REQUEST_MODEL";
    String RESPONSE_MODEL = "RESPONSE_MODEL";
}

