package com.cs.core.config.interactor.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.auditlog.IGetGridAuditLogExportTrackerService;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogExportRequestModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogExportResponseModel;

@Service
public class GetGridAuditLogExportTracker
    extends AbstractGetConfigInteractor<IGetGridAuditLogExportRequestModel, IGetGridAuditLogExportResponseModel>
    implements IGetGridAuditLogExportTracker {
  
  @Autowired
  protected IGetGridAuditLogExportTrackerService getGridAuditLogExportTrackerService;
  
  @Override
  protected IGetGridAuditLogExportResponseModel executeInternal(IGetGridAuditLogExportRequestModel model) throws Exception
  {
    return getGridAuditLogExportTrackerService.execute(model);
  }
}
