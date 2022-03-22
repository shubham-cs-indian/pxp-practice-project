package com.cs.di.workflow.trigger.standard;

import com.cs.di.workflow.trigger.WorkflowTriggerModel;

public class WorkflowEventTriggerModel extends WorkflowTriggerModel implements IWorkflowEventTriggerModel {
    protected String eventType;
    protected String triggeringType;
   
    protected WorkflowEventTriggerModel() {
        super("STANDARD");
    }

    public WorkflowEventTriggerModel(String triggeringType) {
        this();
        this.triggeringType = triggeringType;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String getTriggeringType() {
        return triggeringType;
    }
}
