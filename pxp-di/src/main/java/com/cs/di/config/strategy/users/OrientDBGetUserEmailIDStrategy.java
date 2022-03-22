package com.cs.di.config.strategy.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("getUseremailId")
public class OrientDBGetUserEmailIDStrategy extends OrientDBBaseStrategy implements IGetUserEmailIDStrategy {
  
  public static final String useCase = "GetUserEmailByUserName";
  
  public IUserModel execute(IUserModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IUserModel.USER_NAME, model.getUserName());
    return execute(useCase, requestMap, UserModel.class);
  }
}
