package com.cs.di.workflow.correlation;

import com.cs.di.workflow.IWorkflowModel;

import java.util.Map;

public interface IInstanceCorrelationModel extends IWorkflowModel {
    String CORRELATION_ID          = "correlationId";
    String WORKFLOW_PARAMETERS     = "workflowParameters";

    String getCorrelationId();
    void setCorrelationId(String correlationId);
    Map<String, Object> getWorkflowParameters();
    void setWorkflowParameters(Map<String, Object> workflowParameters);
}
