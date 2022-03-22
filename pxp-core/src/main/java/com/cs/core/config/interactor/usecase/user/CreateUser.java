package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.ICreateUserService;

@Service
public class CreateUser extends AbstractCreateConfigInteractor<IUserModel, IUserModel>
    implements ICreateUser {
  
  @Autowired
  ICreateUserService  createUserService;
  
  @Override
  public IUserModel executeInternal(IUserModel userModel) throws Exception
  {
    return createUserService.execute(userModel);
  }
}
