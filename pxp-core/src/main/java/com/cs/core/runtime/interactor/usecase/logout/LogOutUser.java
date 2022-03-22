package com.cs.core.runtime.interactor.usecase.logout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.logout.ICloseUserSessionService;

@Component
public class LogOutUser implements ILogOutUser {
  
  @Autowired
  protected ICloseUserSessionService closeUserSessionService;
  
  @Override
  public IVoidModel execute(IUserSessionModel dataModel) throws Exception
  {
    return closeUserSessionService.execute(dataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
