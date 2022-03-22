package com.cs.core.config.interactor.model.viewconfiguration;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.viewconfiguration.IViewConfiguration;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IViewConfigurationModel
    extends IConfigModel, IViewConfiguration, IConfigResponseWithAuditLogModel {

}
