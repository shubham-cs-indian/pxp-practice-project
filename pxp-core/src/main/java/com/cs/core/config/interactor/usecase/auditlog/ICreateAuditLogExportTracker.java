package com.cs.core.config.interactor.usecase.auditlog;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateAuditLogExportTracker extends ICreateConfigInteractor<IGetGridAuditLogRequestModel, IModel> {
  
}
