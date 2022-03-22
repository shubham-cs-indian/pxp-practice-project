package com.cs.di.workflow.trigger;

import java.util.List;

public interface IWorkflowTriggerService<T extends IWorkflowTriggerModel> {
    void triggerQualifyingWorkflows(T workflowTriggerModel) throws Exception;
    void triggerQualifyingWorkflows(T workflowTriggerModel, List<String> executedProcessIds) throws Exception;
}
