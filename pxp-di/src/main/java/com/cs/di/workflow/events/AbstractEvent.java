package com.cs.di.workflow.events;

import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.tasks.AbstractTask;
import com.cs.workflow.base.IAbstractEvent;

public abstract class AbstractEvent extends AbstractTask implements IAbstractEvent {
    @Override
    public void executeTask(WorkflowTaskModel model)
    {
    }
}
