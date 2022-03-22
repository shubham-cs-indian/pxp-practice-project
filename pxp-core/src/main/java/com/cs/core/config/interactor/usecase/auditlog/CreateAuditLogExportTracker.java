package com.cs.core.config.interactor.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractConfigImportExportInteractor;
import com.cs.core.config.auditlog.ICreateAuditLogExportTrackerService;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class CreateAuditLogExportTracker extends AbstractConfigImportExportInteractor<IGetGridAuditLogRequestModel, IModel>
    implements ICreateAuditLogExportTracker {
  
  @Autowired
  protected ICreateAuditLogExportTrackerService createAuditLogExportTrackerService;
  
  @Override
  protected IModel executeInternal(IGetGridAuditLogRequestModel requestModel) throws Exception
  {
    return createAuditLogExportTrackerService.execute(requestModel);
  }
}
