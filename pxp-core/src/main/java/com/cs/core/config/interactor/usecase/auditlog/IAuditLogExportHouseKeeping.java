package com.cs.core.config.interactor.usecase.auditlog;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.auditlog.IHouseKeepingRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAuditLogExportHouseKeeping
    extends IGetConfigInteractor<IHouseKeepingRequestModel, IModel> {
  
}
