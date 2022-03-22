package com.cs.di.workflow.trigger.standard;

import com.cs.di.workflow.trigger.WorkflowTriggerService;

public abstract class WorkflowEventTriggerService<T extends IWorkflowEventTriggerModel> extends WorkflowTriggerService<T> implements IWorkflowEventTriggerService<T> {
    protected WorkflowEventTriggerModel createBasicWorkflowFilter(IWorkflowEventTriggerModel workflowEventModel) {
        WorkflowEventTriggerModel filterModel = new WorkflowEventTriggerModel(workflowEventModel.getTriggeringType());
        filterModel.setEventType(workflowEventModel.getEventType());
        fillTransactionData(filterModel);
        return filterModel;
    }
}
