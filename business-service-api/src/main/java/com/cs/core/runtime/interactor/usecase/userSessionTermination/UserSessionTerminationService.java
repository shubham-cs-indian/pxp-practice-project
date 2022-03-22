package com.cs.core.runtime.interactor.usecase.userSessionTermination;

import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class UserSessionTerminationService extends AbstractRuntimeService<IUserSessionModel, IVoidModel> implements IUserSessionTerminationService {
  
  @Override
  public IVoidModel executeInternal(IUserSessionModel dataModel) throws Exception
  {
    RDBMSUtils.newUserSessionDAO()
        .shutdownSessions();
    return null;
  }
}
