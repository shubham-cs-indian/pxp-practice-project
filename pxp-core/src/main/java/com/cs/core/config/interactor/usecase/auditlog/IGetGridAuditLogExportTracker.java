package com.cs.core.config.interactor.usecase.auditlog;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogExportRequestModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogExportResponseModel;

public interface IGetGridAuditLogExportTracker extends
    IGetConfigInteractor<IGetGridAuditLogExportRequestModel, IGetGridAuditLogExportResponseModel> {
  
}
