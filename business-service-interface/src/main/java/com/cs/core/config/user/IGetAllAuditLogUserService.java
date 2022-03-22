package com.cs.core.config.user;

import com.cs.config.businessapi.base.IConfigService;
import com.cs.core.config.interactor.model.auditlog.IGetAllAuditLogUserResponseModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogUserRequestModel;

public interface IGetAllAuditLogUserService
    extends IConfigService<IGetGridAuditLogUserRequestModel, IGetAllAuditLogUserResponseModel> {
  
}
