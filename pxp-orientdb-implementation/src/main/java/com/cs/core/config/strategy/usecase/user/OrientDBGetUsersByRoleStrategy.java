package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.user.GetGridUsersResponseModel;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getUsersByRoleStrategy")
public class OrientDBGetUsersByRoleStrategy extends OrientDBBaseStrategy
    implements IGetUsersByRoleStrategy {
  
  @Override
  public IGetGridUsersResponseModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return (IGetGridUsersResponseModel) execute("GetUsersByRole", requestMap,
        GetGridUsersResponseModel.class);
  }
}
