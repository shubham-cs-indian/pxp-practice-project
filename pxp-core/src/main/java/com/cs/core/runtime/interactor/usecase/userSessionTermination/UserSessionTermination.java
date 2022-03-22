package com.cs.core.runtime.interactor.usecase.userSessionTermination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;

@Service
public class UserSessionTermination implements IUserSessionTermination {
  
  @Autowired
  protected IUserSessionTerminationService userSessionTerminationService;
  
  @Override
  public IVoidModel execute(IUserSessionModel dataModel) throws Exception
  {
    return userSessionTerminationService.execute(dataModel);
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
