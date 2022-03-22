package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.user.IGetCurrentUserService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetCurrentUser
    extends AbstractGetConfigInteractor<IIdParameterModel, IUserInformationModel>
    implements IGetCurrentUser {
  
  @Autowired
  IGetCurrentUserService getCurrentUserService;
  
  @Override
  public IUserInformationModel executeInternal(IIdParameterModel userIdModel) throws Exception
  {
    return getCurrentUserService.execute(userIdModel);
  }
}
