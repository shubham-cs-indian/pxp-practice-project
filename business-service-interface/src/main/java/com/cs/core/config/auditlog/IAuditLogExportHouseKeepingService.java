package com.cs.core.config.auditlog;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.auditlog.IHouseKeepingRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAuditLogExportHouseKeepingService
    extends IGetConfigService<IHouseKeepingRequestModel, IModel> {
  
}
