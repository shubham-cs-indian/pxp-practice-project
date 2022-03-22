package com.cs.core.config.interactor.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.auditlog.IAuditLogExportHouseKeepingService;
import com.cs.core.config.interactor.model.auditlog.IHouseKeepingRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class AuditLogExportHouseKeeping extends AbstractGetConfigInteractor<IHouseKeepingRequestModel, IModel>
    implements IAuditLogExportHouseKeeping {
  
  @Autowired
  protected IAuditLogExportHouseKeepingService auditLogExportHouseKeepingService;
  
  @Override
  protected IModel executeInternal(IHouseKeepingRequestModel model) throws Exception
  {
    return auditLogExportHouseKeepingService.execute(model);
  }
}
