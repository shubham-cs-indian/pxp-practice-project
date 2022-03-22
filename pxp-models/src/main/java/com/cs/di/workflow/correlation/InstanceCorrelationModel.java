package com.cs.di.workflow.correlation;

import com.cs.di.workflow.WorkflowModel;

import java.util.Map;

public class InstanceCorrelationModel extends WorkflowModel implements IInstanceCorrelationModel {
    String correlationId;
    Map<String, Object> workflowParameters;
    @Override
    public String getCorrelationId() {
        return this.correlationId;
    }

    @Override
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public Map<String, Object> getWorkflowParameters() {
        return this.workflowParameters;
    }

    @Override
    public void setWorkflowParameters(Map<String, Object> workflowParameters) {
        this.workflowParameters = workflowParameters;
    }
}