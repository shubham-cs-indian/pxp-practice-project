package com.cs.di.workflow.trigger;

import java.util.Set;

import com.cs.di.workflow.WorkflowModel;

public class WorkflowTriggerModel extends WorkflowModel implements IWorkflowTriggerModel {
    protected String workflowType;
    protected String physicalCatalogId;
    protected String endpointId;
    protected Set<String> processIds;
    protected String organizationId;
  
    protected WorkflowTriggerModel(String workflowType) {
        super();
        this.workflowType=workflowType;
    }

    @Override
    public String getPhysicalCatalogId() {
        return physicalCatalogId;
    }

    @Override
    public void setPhysicalCatalogId(String physicalCatalogId) {
        this.physicalCatalogId = physicalCatalogId;
    }

    @Override
    public String getEndpointId() {
        return endpointId;
    }

    @Override
    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public String getWorkflowType() {
        return workflowType;
    }

    @Override
    public Set<String> getProcessIds()
    {
      return processIds;
    }

    @Override
    public void setProcessIds(Set<String> processIds)
    {
      this.processIds = processIds;
    }
    
    @Override
    public String getOrganizationId()
    {
      return organizationId;
    }

    @Override
    public void setOrganizationId(String organizationId)
    {
      this.organizationId = organizationId;
    }

}
