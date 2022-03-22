package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBResetPasswordStrategy extends OrientDBBaseStrategy
    implements IResetPasswordStrategy {
  
  public static final String useCase = "ResetPassword";
  
  @Override
  public IUserModel execute(IUserModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("user", model);
    return execute(useCase, requestMap, UserModel.class);
  }
}
