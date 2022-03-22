package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateAuditLogExportTrackerService extends ICreateConfigService<IGetGridAuditLogRequestModel, IModel> {
  
}
