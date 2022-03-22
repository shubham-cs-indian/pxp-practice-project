package com.cs.core.config.strategy.usecase.user;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.auditlog.IGetAllAuditLogUserResponseModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogUserRequestModel;

public interface IGetAllAuditLogUser extends IGetConfigInteractor<IGetGridAuditLogUserRequestModel, IGetAllAuditLogUserResponseModel> {
  
}
