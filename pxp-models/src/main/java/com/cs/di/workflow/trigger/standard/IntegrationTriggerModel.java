package com.cs.di.workflow.trigger.standard;

import java.util.Map;

public class IntegrationTriggerModel extends WorkflowEventTriggerModel implements IIntegrationTriggerModel {

    protected IntegrationActionType integrationActionType;
   
    @Override
    public IntegrationActionType getIntegrationActionType() {
        return integrationActionType;
    }

    @Override
    public void setIntegrationActionType(IntegrationActionType integrationActionType) {
        this.integrationActionType = integrationActionType;
        this.triggeringType = integrationActionType.toString();
    }

    private Map<String, Object> workflowParameters;
    @Override
    public Map<String, Object> getWorkflowParameters() {
        return workflowParameters;
    }

    @Override
    public void setWorkflowParameters(Map<String, Object> workflowParameters) {
        this.workflowParameters = workflowParameters;
    }
}
