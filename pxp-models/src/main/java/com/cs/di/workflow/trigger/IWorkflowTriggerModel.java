package com.cs.di.workflow.trigger;

import java.util.Set;

import com.cs.di.workflow.IWorkflowModel;

public interface IWorkflowTriggerModel extends IWorkflowModel {
    String WORKFLOW_TYPE     = "workflowType";
    String PHYSICAL_CATALOG_ID = "physicalCatalogId";
    String ENDPOINT_ID         = "endpointId";
    String PROCESS_IDS         = "processIds";
    String ORGANIZATION_ID     = "organizationId";

    String getPhysicalCatalogId();
    void setPhysicalCatalogId(String physicalCatalogId);
    String getEndpointId();
    void setEndpointId(String endpointId);
    String getWorkflowType();
    Set<String> getProcessIds();
    void setProcessIds(Set<String> processIds);
    String getOrganizationId();
    void setOrganizationId(String organizationId);
}
