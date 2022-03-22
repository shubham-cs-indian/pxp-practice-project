package com.cs.core.config.strategy.usecase.standard.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.user.ICreateUserStrategy;

@Component("createStandardUserStrategy")
public class OrientDBCreateStandardUserStrategy extends OrientDBBaseStrategy
    implements ICreateUserStrategy {
  
  public static final String useCase = "CreateStandardUser";
  
  @Override
  public IUserModel execute(IUserModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("user", model);
    return execute(useCase, requestMap, UserModel.class);
  }
}
