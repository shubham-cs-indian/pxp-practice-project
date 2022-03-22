package com.cs.core.config.strategy.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.auditlog.IGetAllAuditLogUserResponseModel;
import com.cs.core.config.interactor.model.auditlog.IGetGridAuditLogUserRequestModel;
import com.cs.core.config.user.IGetAllAuditLogUserService;

@Service
public class GetAllAuditLogUser extends AbstractGetConfigInteractor<IGetGridAuditLogUserRequestModel, IGetAllAuditLogUserResponseModel> implements IGetAllAuditLogUser{

  @Autowired
  IGetAllAuditLogUserService getAllAuditLogUserService;
  
  @Override
  protected IGetAllAuditLogUserResponseModel executeInternal(IGetGridAuditLogUserRequestModel model) throws Exception
  {
    return getAllAuditLogUserService.execute(model);
  }
  
}
