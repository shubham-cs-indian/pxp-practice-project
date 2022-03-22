package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IGetUserByUserNameStrategy;

@Service
public class GetUserByUserNameService extends AbstractGetConfigService<IUserModel, IUserModel>
    implements IGetUserByUserNameService {
  
  @Autowired
  IGetUserByUserNameStrategy neo4jGetUserByUsernameStrategy;
  
  @Override
  public IUserModel executeInternal(IUserModel dataModel) throws Exception
  {
    IUserModel model = neo4jGetUserByUsernameStrategy.execute(dataModel);
    if (model != null) {
      throw new Exception();
    }
    else {
      return dataModel;
    }
  }
}
