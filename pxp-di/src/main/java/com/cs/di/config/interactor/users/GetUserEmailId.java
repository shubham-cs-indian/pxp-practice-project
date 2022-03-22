  package com.cs.di.config.interactor.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.di.config.strategy.users.IGetUserEmailIDStrategy;

@Component
public class GetUserEmailId extends AbstractGetConfigInteractor<IUserModel, IUserModel> implements IGetUserEmailId{

  @Autowired 
  IGetUserEmailIDStrategy getUserEmailIDStrategy;
  
  @Override
  protected IUserModel executeInternal(IUserModel model) throws Exception
  {
    return getUserEmailIDStrategy.execute(model);
  }
  
}
