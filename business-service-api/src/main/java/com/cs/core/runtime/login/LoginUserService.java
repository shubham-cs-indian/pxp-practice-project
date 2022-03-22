package com.cs.core.runtime.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class LoginUserService extends AbstractService<IUserSessionModel, IVoidModel> implements ILoginUserService {
  
  @Autowired
  protected SessionContextCustomProxy context;
  
  @Override
  protected IVoidModel executeInternal(IUserSessionModel dataModel) throws Exception
  {
    IUserSessionDTO userSessionDTO = RDBMSUtils.newUserSessionDAO().openSession(dataModel.getUserName(), dataModel.getSessionId());
    context.setUserSessionDTO(userSessionDTO);
    return null;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
