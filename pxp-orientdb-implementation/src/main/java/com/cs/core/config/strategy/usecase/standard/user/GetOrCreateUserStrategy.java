package com.cs.core.config.strategy.usecase.standard.user;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.store.strategy.base.user.IGetOrCreateUserStrategy;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("orientDBGetOrCreateStandardUserStrategy")
public class GetOrCreateUserStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateUserStrategy {
  
  public static final String useCase = "GetOrCreateUser";
  
  @Override
  public IUserModel execute(IUserModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("user", model);
    // System.out.println("strategy called for create std user");
    return execute(useCase, requestMap, UserModel.class);
  }
}
