package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.IGetUserService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetUser extends AbstractGetConfigInteractor<IIdParameterModel, IUserModel>
    implements IGetUser {
  
  @Autowired
  IGetUserService getUserService;
  
  @Override
  public IUserModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getUserService.execute(dataModel);
  }
}
