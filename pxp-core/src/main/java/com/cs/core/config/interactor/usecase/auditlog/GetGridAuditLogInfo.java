package com.cs.core.config.interactor.usecase.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.auditlog.IGetGridAuditLogInfoService;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogRequestModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogResponseModel;

@Service
public class GetGridAuditLogInfo extends AbstractGetConfigInteractor<IGetGridAuditLogRequestModel, IGetGridAuditLogResponseModel>
    implements IGetGridAuditLogInfo {
  
  @Autowired
  protected IGetGridAuditLogInfoService getGridAuditLogInfoService;
  
  @Override
  protected IGetGridAuditLogResponseModel executeInternal(IGetGridAuditLogRequestModel model) throws Exception
  {
    return getGridAuditLogInfoService.execute(model);
  }
  
}
