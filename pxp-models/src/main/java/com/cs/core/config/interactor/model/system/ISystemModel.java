package com.cs.core.config.interactor.model.system;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.system.ISystem;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ISystemModel extends IConfigModel, ISystem, IConfigResponseWithAuditLogModel {
  
}
