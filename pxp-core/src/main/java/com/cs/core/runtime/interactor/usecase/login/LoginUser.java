package com.cs.core.runtime.interactor.usecase.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.login.ILoginUserService;

@Component
public class LoginUser implements ILoginUser {
  
  @Autowired
  protected ILoginUserService loginUserService;
  
  @Override
  public IVoidModel execute(IUserSessionModel dataModel) throws Exception
  {
    return loginUserService.execute(dataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
