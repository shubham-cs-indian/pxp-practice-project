package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.GetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("createRoleStrategy")
public class OrientDBCreateRoleStrategy extends OrientDBBaseStrategy
    implements ICreateRoleStrategy {
  
  @Override
  public IGetRoleStrategyModel execute(ICreateRoleModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("role", model);
    return execute(CREATE_ROLE, requestMap, GetRoleStrategyModel.class);
  }
}
