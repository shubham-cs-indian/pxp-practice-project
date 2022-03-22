package com.cs.di.workflow.trigger.standard;

import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;

public interface IApplicationTriggerModel extends IWorkflowEventTriggerModel {
    String getServiceData();
    void setServiceData(String serviceData);
    BGPPriority getPriority();
    void setPriority(BGPPriority priority);
    ApplicationActionType getApplicationActionType();
    void setApplicationActionType(ApplicationActionType applicationActionType);

    enum ApplicationActionType {
        TRANSFER, PROPERTY_DELETE, TAG_VALUE_DELETE, BULK_CLONING, ASSET_UPLOAD, ICON_UPLOAD, ASSET_LINK_SHARING, BULK_UPDATE, SMART_DOCUMENT_UPLOAD, MARK_DUPLICATE_ASSETS;

        private String actionType;
        public String toString() {
            return actionType;
        }

        ApplicationActionType() {
            this.actionType = this.name();
        }
    }
}
