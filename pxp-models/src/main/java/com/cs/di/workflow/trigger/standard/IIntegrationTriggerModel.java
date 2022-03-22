package com.cs.di.workflow.trigger.standard;

import java.util.Map;

public interface IIntegrationTriggerModel extends IWorkflowEventTriggerModel {

    Map<String, Object> getWorkflowParameters();
    void setWorkflowParameters(Map<String, Object> workflowParameters);

    IntegrationActionType getIntegrationActionType();
    void setIntegrationActionType(IntegrationActionType integrationActionType);

    enum IntegrationActionType {
        //Integration Event action types
        IMPORT("import"), EXPORT("export"), CONFIG_IMPORT, CONFIG_EXPORT;

        private String actionType;
        public String toString() {
            return actionType;
        }

        //Use this constructor for Application Events
        IntegrationActionType() {
            this.actionType = this.name();
        }
        
      //Use this constructor for Application Events
        IntegrationActionType(String actionType) {
            this.actionType = actionType;
        }
    }
}
