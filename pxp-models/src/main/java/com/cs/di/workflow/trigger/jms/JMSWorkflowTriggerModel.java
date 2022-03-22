package com.cs.di.workflow.trigger.jms;

import com.cs.di.workflow.trigger.WorkflowTriggerModel;

public class JMSWorkflowTriggerModel extends WorkflowTriggerModel implements IJMSWorkflowTriggerModel {
    public JMSWorkflowTriggerModel() {
        super("JMS");
    }
}
