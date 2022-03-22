package com.cs.core.config.interactor.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.auditlog.IDeleteAuditLogExportService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteAuditLogExport extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteAuditLogExport {
  
  @Autowired
  protected IDeleteAuditLogExportService deleteAuditLogExportService;
  
  @Override
  protected IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return deleteAuditLogExportService.execute(model);
  }
}
