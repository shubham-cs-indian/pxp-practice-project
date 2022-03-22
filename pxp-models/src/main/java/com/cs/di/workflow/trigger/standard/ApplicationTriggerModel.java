package com.cs.di.workflow.trigger.standard;

import com.cs.core.bgprocess.idto.IBGProcessDTO;

public class ApplicationTriggerModel extends WorkflowEventTriggerModel implements IApplicationTriggerModel {
    private String serviceData;
    private IBGProcessDTO.BGPPriority priority;
    protected ApplicationActionType applicationActionType;

    @Override
    public ApplicationActionType getApplicationActionType() {
        return applicationActionType;
    }

    @Override
    public void setApplicationActionType(ApplicationActionType applicationActionType) {
        this.applicationActionType = applicationActionType;
        this.triggeringType = applicationActionType.toString();
    }

    @Override
    public String getServiceData() {
        return serviceData;
    }

    @Override
    public void setServiceData(String serviceData) {
        this.serviceData = serviceData;
    }

    @Override
    public IBGProcessDTO.BGPPriority getPriority() {
        return priority;
    }

    @Override
    public void setPriority(IBGProcessDTO.BGPPriority priority) {
        this.priority = priority;
    }
}
