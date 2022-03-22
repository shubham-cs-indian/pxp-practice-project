package com.cs.core.runtime.logout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CloseUserSessionService extends AbstractService<IUserSessionModel, IVoidModel> implements ICloseUserSessionService {
  
  @Autowired
  protected SessionContextCustomProxy context;
  
  @Override
  protected IVoidModel executeInternal(IUserSessionModel dataModel) throws Exception
  {
    RDBMSUtils.newUserSessionDAO().closeSession(dataModel.getSessionId(), dataModel.getLogoutType(), "");
    
    return new VoidModel();
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
