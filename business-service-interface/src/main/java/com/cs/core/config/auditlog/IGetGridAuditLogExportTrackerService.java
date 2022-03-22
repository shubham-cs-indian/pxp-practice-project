package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogExportRequestModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogExportResponseModel;

public interface IGetGridAuditLogExportTrackerService extends
    IGetConfigService<IGetGridAuditLogExportRequestModel, IGetGridAuditLogExportResponseModel> {
  
}
