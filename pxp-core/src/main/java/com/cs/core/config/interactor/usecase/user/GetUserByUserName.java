package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.IGetUserByUserNameService;

@Service
public class GetUserByUserName extends AbstractGetConfigInteractor<IUserModel, IUserModel>
    implements IGetUserByUserName {
  
  @Autowired
  IGetUserByUserNameService neo4jGetUserByUsernameService;
  
  @Override
  public IUserModel executeInternal(IUserModel dataModel) throws Exception
  {
    return neo4jGetUserByUsernameService.execute(dataModel);
    
  }
}
