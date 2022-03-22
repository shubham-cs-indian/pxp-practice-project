package com.cs.di.workflow.trigger.standard;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.workflow.base.EventType;

@Service
public class IntegrationTriggerService extends WorkflowEventTriggerService<IIntegrationTriggerModel> implements IIntegrationTriggerService {

    @Override
    public IListModel<IWorkflowParameterModel> getQualifyingWorkflows(IIntegrationTriggerModel workflowEventModel) throws Exception
    {
      workflowEventModel.setEventType(EventType.INTEGRATION.name());
      WorkflowEventTriggerModel filterModel = createBasicWorkflowFilter(workflowEventModel);
      if (filterModel.getEndpointId() == null || filterModel.getEndpointId().isEmpty()) {
        filterModel.setEndpointId(workflowEventModel.getEndpointId());
      }
      return getProcessIds(filterModel);
    }

    @Override
    protected void addExecutionParameters(IIntegrationTriggerModel workflowEventModel, Map<String, Object> workflowParameters) {
        Map<String, Object> eventParameters = workflowEventModel.getWorkflowParameters();
        switch (workflowEventModel.getIntegrationActionType()) {
            case IMPORT:
                addImportParameters(workflowEventModel, workflowParameters, eventParameters);
                break;
            case EXPORT:
                addExportParameters(workflowEventModel, workflowParameters, eventParameters);
                break;
            case CONFIG_EXPORT:
                addConfigExportParameters(workflowParameters, eventParameters);
                break;
            case CONFIG_IMPORT:
                addConfigImportParameters(workflowParameters, eventParameters);
                break;
        }
    }

    private void addImportParameters(IIntegrationTriggerModel workflowEventModel, Map<String, Object> workflowParameters, Map<String, Object> eventParameters) {
      workflowParameters.put(RECEIVED_DATA, eventParameters.get(RECEIVED_DATA));
    }
    
    private void addExportParameters(IIntegrationTriggerModel workflowEventModel, Map<String, Object> workflowParameters, Map<String, Object> eventParameters) {
      workflowParameters.put(SEARCH_CRITERIA, eventParameters.get(SEARCH_CRITERIA));
    }
    
    private void addConfigExportParameters(Map<String, Object> workflowParameters, Map<String, Object> eventParameters) {
        workflowParameters.put(CONFIG_PROPERTY_TYPE, eventParameters.get(CONFIG_PROPERTY_TYPE));
        workflowParameters.put(CONFIG_ENTITY_CODES, eventParameters.get(CONFIG_ENTITY_CODES));
    }

    private void addConfigImportParameters(Map<String, Object> workflowParameters, Map<String, Object> eventParameters) {
        workflowParameters.put(RECEIVED_DATA, eventParameters.get(RECEIVED_DATA));
        workflowParameters.put(NODE_TYPE, eventParameters.get(NODE_TYPE));
        workflowParameters.put(PERMISSION_TYPES, eventParameters.get(PERMISSION_TYPES));
        workflowParameters.put(ROLE_IDS, eventParameters.get(ROLE_IDS));
    }
}
