package com.cs.di.workflow.trigger.task;

import com.cs.di.workflow.trigger.WorkflowTriggerModel;

public class TaskTriggerModel extends WorkflowTriggerModel implements ITaskTriggerModel {
    protected TaskTriggerModel() {
        super("TASK");
    }
}
