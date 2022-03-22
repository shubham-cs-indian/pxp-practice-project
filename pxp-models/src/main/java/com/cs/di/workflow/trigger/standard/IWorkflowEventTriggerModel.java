package com.cs.di.workflow.trigger.standard;

import com.cs.di.workflow.trigger.IWorkflowTriggerModel;

public interface IWorkflowEventTriggerModel extends IWorkflowTriggerModel {
    String EVENT_TYPE          = "eventType";
    String TRIGGERING_TYPE     = "triggeringType";

    String getEventType();
    void setEventType(String eventType);

    String getTriggeringType();
}
