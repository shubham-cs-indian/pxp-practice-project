package com.cs.core.config.strategy.usecase.standard.user;

import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.user.IGetUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getStandardUserStrategy")
public class OrientDBGetStandardUserStrategy extends OrientDBBaseStrategy
    implements IGetUserStrategy {
  
  public static final String useCase = "GetStandardUser";
  
  @Override
  public IUserModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, UserModel.class);
  }
}
